package com.kinstalk.voip.sdk.logic.sip;

import java.net.URI;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;

import com.kinstalk.voip.sdk.EngineSdkAccountState;
import com.kinstalk.voip.sdk.EngineSdkCPUInstruction;
import com.kinstalk.voip.sdk.EngineSdkCPUManufacturer;
import com.kinstalk.voip.sdk.EngineSdkCallState;
import com.kinstalk.voip.sdk.EngineSdkEngineState;
import com.kinstalk.voip.sdk.EngineSdkMediaChannelSetupMode;
import com.kinstalk.voip.sdk.EngineSdkMsgSender;
import com.kinstalk.voip.sdk.EngineSdkOperatingSystemType;
import com.kinstalk.voip.sdk.EngineSdkServerType;
import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.api.WeaverService;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.logic.WeaverAbstractLogic;
import com.kinstalk.voip.sdk.logic.sip.aidl.ISipService;
import com.kinstalk.voip.sdk.logic.sip.aidl.ISipServiceListener;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.AccountConfiguration;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.AccountInfo;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.CallInfo;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.CpuInfo;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.EngineConfiguration;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.EngineInfo;
import com.kinstalk.voip.sdk.logic.sip.delegate.CallDataListener;
import com.kinstalk.voip.sdk.logic.sip.service.SipService;
import com.kinstalk.voip.sdk.logic.user.UserConstants;
import com.kinstalk.voip.sdk.logic.user.json.UserLoginJsonObject;
import com.kinstalk.voip.sdk.http.WeaverBaseAPI;
import com.kinstalk.voip.sdk.common.DeviceInfo;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;
import com.kinstalk.voip.sdk.common.UserPreferences;

public class SipLogic extends WeaverAbstractLogic
{
	public static final String TAG = "SipLigic";
	private URI mSipLogicUri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, null);

	private ISipService mSipService;
	private boolean mIsServiceWorking = false;
	private SipServiceHookerThread mServiceHooker = null;
	private SipServiceListenerImpl mServiceListenerImpl = new SipServiceListenerImpl();

	private CallInfo mCurrentCallInfo;
	private AccountInfo mCurrenAccountInfo;
	private EngineInfo mCurrentEngineInfo = new EngineInfo();
	private String mInCallActivityName = null;
	private int mOutGoingCallRingtoneId = 0;
	private Notification mCustomNotification;
	private boolean mIsRandomCallMode = false;

	private Context mContext;
	private String mProcessName = "unknownProcessName";

	public SipLogic(Context context)
	{
		super(context);
		mContext = context.getApplicationContext();
		WeaverService.getInstance().registerLogicHandler(mSipLogicUri, this);
		String curProcessName = this.getCurProcessName(mContext); 
		mProcessName = (curProcessName != null && curProcessName.length() > 0)?curProcessName:mProcessName;
	}

	@Override
	public void doHandleRequest(WeaverRequest req)
	{
		 Log.d(getClass(), "ContactLogic handle request:" + req.toString());
		String path = req.getURI().getPath();
		if (SipConstants.LogicPath.INIT_SDK.equals(path))
		{
			Context context = (Context) req.getParameter(SipConstants.LogicParam.CONTEXT);
			String userAgent = (String) req.getParameter(SipConstants.LogicParam.USER_AGENT);
			String inCallActivityName = (String) req.getParameter(SipConstants.LogicParam.INCALL_ACTIVITY_NAME);
			Notification customNotification = (Notification) req.getParameter(SipConstants.LogicParam.CUSTOMIZED_NOTIFICATION);
			Boolean isRandomCallMode = (Boolean) req.getParameter(SipConstants.LogicParam.RANDOMCALL_MODE);
			int outGoingCallRingtoneId = (Integer) req.getParameter(SipConstants.LogicParam.OUTGOINGCALL_RINGTONE_ID);
			if (isRandomCallMode != null)
			{
				mIsRandomCallMode = isRandomCallMode.booleanValue();
			}
			else
			{
				mIsRandomCallMode = false;
			}
			 Log.d(getClass(), "doHandleRequest: SipConstants.LogicPath.INIT_SDK: mIsRandomCallMode is set to " + mIsRandomCallMode);

			init(context, generateEngineConfiguration(context, userAgent), inCallActivityName, customNotification, outGoingCallRingtoneId);

		}else if (SipConstants.LogicPath.DESTROY_ENGINE.equals(path))
		{
			callDestroyEngine();
		}
		else if (SipConstants.LogicPath.SET_USER_ACCOUNT.equals(path))
		{
			String userId = (String) req.getParameter(SipConstants.LogicParam.USER_ID);
			String userPassword = (String) req.getParameter(SipConstants.LogicParam.USER_PASSOWRD);
			String userDomain = (String) req.getParameter(SipConstants.LogicParam.USER_DOMAIN);
			setUserAccount(userId, userPassword, userDomain);
		}
		else if (SipConstants.LogicPath.DELETE_USER_ACCOUNT.equals(path))
		{
			deleteAccount();
		}
		else if (SipConstants.LogicPath.SET_FORCED_RELAY_PATH.equals(path))
		{
			String relay_path = (String) req.getParameter(SipConstants.LogicParam.FORCED_RELAY_PATH);
			setForcedRelayPath(relay_path);
		}
		else if (SipConstants.LogicPath.SEND_MESSAGE.equals(path))
		{
			String to = (String) req.getParameter(SipConstants.LogicParam.TO);
			String message = (String) req.getParameter(SipConstants.LogicParam.MESSAGE);
			String mimeType = (String) req.getParameter(SipConstants.LogicParam.MIME_TYPE);
			EngineSdkMsgSender senderModule = (EngineSdkMsgSender) req.getParameter(SipConstants.LogicParam.SENDER_MODULE);
			int msgLocalId = sendMessage(to, message, mimeType, senderModule);
			req.setResponse(WeaverConstants.RequestReturnCode.OK, msgLocalId);
			return;
		}
		else if (SipConstants.LogicPath.MAKE_CALL.equals(path))
		{
			String to = (String) req.getParameter(SipConstants.LogicParam.TO);
			String gid = (String) req.getParameter(SipConstants.LogicParam.GID);
			Log.e(TAG, "logic GID :"+gid);
			boolean isAudioOnly = (Boolean) req.getParameter(SipConstants.LogicParam.IS_AUDIO_ONLY);
			makeCall(to, gid, isAudioOnly);
		}
		else if (SipConstants.LogicPath.ANSWER_CALL.equals(path))
		{
			boolean isAudioOnly = (Boolean) req.getParameter(SipConstants.LogicParam.IS_AUDIO_ONLY);
			answerCall(isAudioOnly);
		}
		else if (SipConstants.LogicPath.END_CALL.equals(path))
		{
			int callId = (Integer) req.getParameter(SipConstants.LogicParam.CALL_ID);
			endCall(callId);
		}
		else if (SipConstants.LogicPath.SET_AUDIO_MODE.equals(path))
		{
			int mode = (Integer) req.getParameter(SipConstants.LogicParam.AUDIO_MODE);
			setAudioControlMode(mode);
		}
		else if (SipConstants.LogicPath.PAUSE_INCALL_OPERATE.equals(path))
		{
			pauseInCallOperate();
		}
		else if (SipConstants.LogicPath.RESUME_INCALL_OPERATE.equals(path))
		{
			resumeInCallOperate();
		}
		else if (SipConstants.LogicPath.SET_NOTIFICATIONVIEW.equals(path))
		{
			 Log.d("zhangqiang78", "set null");
			Notification customNotification = (Notification) req.getParameter(SipConstants.LogicParam.CUSTOMIZED_NOTIFICATION);
			setNotificationView(customNotification);
		}
		else if (SipConstants.LogicPath.PLAY_RINGTONE.equals(path))
		{
			int ringtoneType = (Integer)req.getParameter(SipConstants.LogicParam.RINGTONE_TYPE);
			playRingtone(ringtoneType);
		}
		else if (SipConstants.LogicPath.STOP_PLAYING.equals(path))
		{
			stopPlaying();
		}
		else if (SipConstants.LogicPath.SET_INCALLING_MODE.equals(path))
		{
			int mode = (Integer) req.getParameter(SipConstants.LogicParam.INCALLING_MODE);
			setInCallingMode(mode);
		}
		else if (SipConstants.LogicPath.SET_HANDFREE.equals(path))
		{
			boolean isHandfreeOn = (Boolean) req.getParameter(SipConstants.LogicParam.IS_HANDFREE_ON);
			setHandfree(isHandfreeOn);
		}
		else if (SipConstants.LogicPath.SET_EARPHONECONNECTED.equals(path))
		{
			boolean isEarphoneConnected = (Boolean) req.getParameter(SipConstants.LogicParam.IS_EARPHONE_CONNECTEd);
			setEarphoneConnected(isEarphoneConnected);
		}
		else if (SipConstants.LogicPath.SET_BLUETOOTHCONNECTED.equals(path))
		{
			boolean isBluetoothConnected = (Boolean) req.getParameter(SipConstants.LogicParam.IS_BLUETOOTH_CONNECTED);
			setBluetoothConnected(isBluetoothConnected);
		}
		else if (SipConstants.LogicPath.OPERATE_SYSTEMAUDIO.equals(path))
		{
			operateSystemAudio();
		}
		else if (SipConstants.LogicPath.ENTRY_INCALLSTATE.equals(path))
		{
			entryInCallState();
		}
		else if (SipConstants.LogicPath.LEAVE_INCALLSTATE.equals(path))
		{
			leaveInCallState();
		} else if(SipConstants.LogicPath.SET_TRANSMITING_VIDEO_PAUSED.equals(path))
		{
			boolean pause = (Boolean) req.getParameter(SipConstants.LogicParam.VIDEO_PAUSED);
			setTransmitingVideoPaused(pause);
		} else if(SipConstants.LogicPath.REQUEST_REMOTE_PAUSE_TRANSMITING_VIDEO.equals(path))
		{
			boolean pause = (Boolean) req.getParameter(SipConstants.LogicParam.VIDEO_PAUSED);
			boolean allowresetbypeer = (Boolean) req.getParameter(SipConstants.LogicParam.ALLOW_RESET_BY_PEER);
			requestRemotePauseTransmitingVideo(pause, allowresetbypeer);
		} else if(SipConstants.LogicPath.SET_DOWN_TO_AUDIO.equals(path))
		{
			boolean pause = (Boolean) req.getParameter(SipConstants.LogicParam.DOWN_TO_AUDIO);
			setDown2Audio(pause);
		} 
		
		req.setResponse(WeaverConstants.RequestReturnCode.OK, "");
	}

	public EngineConfiguration generateEngineConfiguration(Context context, String ua)
	{
		CpuInfo info = new CpuInfo();
		info.setCoreNum(DeviceInfo.getNumCores());
		info.setInstruction(Build.CPU_ABI.equals("x86") ? EngineSdkCPUInstruction.ES_CPU_INSTRUCTION_INTELX86 : EngineSdkCPUInstruction.ES_CPU_INSTRUCTION_NEON);

		if (Build.BOARD.toLowerCase(Locale.ENGLISH).startsWith("msm") || Build.HARDWARE.toLowerCase(Locale.ENGLISH).startsWith("qcom"))
		{
			info.setManufacturer(EngineSdkCPUManufacturer.ES_CPU_MANUFACTURER_QCOM);
		}
		else if (Build.BOARD.toLowerCase(Locale.ENGLISH).startsWith("mt"))
		{
			info.setManufacturer(EngineSdkCPUManufacturer.ES_CPU_MANUFACTURER_MTK);
		}
		else if (Build.CPU_ABI.toLowerCase(Locale.ENGLISH).equals("x86"))
		{
			info.setManufacturer(EngineSdkCPUManufacturer.ES_CPU_MANUFACTURER_INTEL);
		}
		else
		{
			info.setManufacturer(EngineSdkCPUManufacturer.ES_CPU_MANUFACTURER_UNKNOWN);
		}

		try
		{
			info.setFrequencyMhz(Integer.parseInt(DeviceInfo.getMaxCpuFreq()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		EngineConfiguration ec = new EngineConfiguration();

		EngineSdkServerType serverType = WeaverBaseAPI.getENV().getEsServerType();
		 Log.d(TAG, "generateEngineConfiguration: serverType" + serverType.toString());

		ec.setServerConfType(serverType);
		ec.setLogLevel(3);
		ec.setLogPathName(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sip_log.txt");
		ec.setUserAgent(ua);

		String s = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + context.getApplicationInfo().packageName + "/config.cfg";
		ec.setConfigCachePathFrame(s);

		ec.setCpuInfo(info);
		ec.setOs(EngineSdkOperatingSystemType.ES_OS_TYPE_ANDROID4);
		ec.setMaxReceiveResolutionWidth(320);
		ec.setMaxReceiveResolutionHeight(240);
		ec.setDeviceId(StringUtility.getInstanceId(context));

		ec.setAudioMediaMode(EngineSdkMediaChannelSetupMode.ES_MEDIA_CHANNEL_PRE_LOADED);
		ec.setMainVideoMediaMode(EngineSdkMediaChannelSetupMode.ES_MEDIA_CHANNEL_PRE_LOADED);
		ec.setSecondVideoMediaMode(EngineSdkMediaChannelSetupMode.ES_MEDIA_CHANNEL_DISABLED);
		ec.setDataMediaMode(EngineSdkMediaChannelSetupMode.ES_MEDIA_CHANNEL_DISABLED);

		ec.setMaxTransmitResolutionWidth(320);
		ec.setMaxTransmitResolutionHeight(240);

		return ec;
	}
	
	private String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	private ServiceConnection mServiceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1)
		{
			mCurrentEngineInfo.setEngineState(EngineSdkEngineState.ES_STATE_ON_ENGINE_CONNECTED);
			mServiceListenerImpl.onEngineStateChange(mCurrentEngineInfo);

			mIsServiceWorking = true;
			mSipService = ISipService.Stub.asInterface(arg1);

			synchronized (mCurrentEngineInfo)
			{
				try
				{
					 Log.d(TAG, "--------------init: onServiceConnected");
					mSipService.init(mCurrentEngineInfo.getEngineConfiguration(), mInCallActivityName, mOutGoingCallRingtoneId);
					if (mServiceListenerImpl != null)
					{
						 Log.d(TAG, "mSipService.setRPCListener: " + mServiceListenerImpl.toString());
						mSipService.setRPCListener(mServiceListenerImpl, mProcessName);
					}
				}
				catch (RemoteException e)
				{
					 Log.e(TAG, "", e);
				}
			}

			//if (mCustomNotification != null)
			{
				try
				{
					mSipService.setCustomNotificationView(188, mCustomNotification);// 188为临时设置的一个幻数
				}
				catch (RemoteException e)
				{
					 Log.e(TAG, "", e);
				}
			}
			// 如果已经保存了用户名并且没有主动退出，则自动注册
			if (UserPreferences.getBool(UserPreferences.Key.IS_AUTO_REGISTER, true)) {
				UserLoginJsonObject userLoginJsonObject = WeaverService.getInstance().getUserLogic().getmCurrentUserLoginJsonObject();
				if (userLoginJsonObject != null) {
					 Log.d(TAG, "AutoRegister flag available, try to auto register...");
					String userId = userLoginJsonObject.getUserId();
					String userPassword = userLoginJsonObject.getPassword();
					String userDomain = userLoginJsonObject.getUserDomain();
					if (!"".equals(userId) && !"".equals(userPassword) && !"".equals(userDomain)) {
						setUserAccount(userId, userPassword, userDomain);
					} else {
						 Log.d(TAG, "AutoRegister failed: userId=" + userId + ", userDomain=" + userDomain);
					}
				} else {
					 Log.d(TAG, "AutoRegister flag disabled, skip to auto register...");
				}
			} else {
				 Log.d(TAG, "AutoRegister flag disabled, skip to auto register...");
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0)
		{
			if (mCurrentCallInfo != null)
			{
				mCurrentCallInfo.setCallState(EngineSdkCallState.ES_STATE_ON_CALL_CALL_ENDED);
				mServiceListenerImpl.onCallStateChange(mCurrentCallInfo, mCurrentCallInfo.getCallState().swigValue());
			}

			if (mCurrenAccountInfo != null)
			{
				mCurrenAccountInfo.setAccountState(EngineSdkAccountState.ES_STATE_ON_ACC_REGISTERED);
				mServiceListenerImpl.onAccountStateChange(mCurrenAccountInfo, mCurrenAccountInfo.getAccountState().swigValue());
			}

			if (mCurrentEngineInfo != null)
			{
				mCurrentEngineInfo.setEngineState(EngineSdkEngineState.ES_STATE_ON_ENGINE_DISCONNECTED);
				mServiceListenerImpl.onEngineStateChange(mCurrentEngineInfo);
			}

			mIsServiceWorking = false;
			synchronized (mServiceHooker)
			{
				mServiceHooker.notifyAll();
			}
			 Log.d(TAG, "SipService onServiceDisconnected~");
		}

	};

	private class SipServiceListenerImpl extends ISipServiceListener.Stub
	{
		@Override
		public void onMessage(int msgId, String msgGlobalId, String sender, String msgContent, String mimeType) throws RemoteException {
			// TODO Auto-generated method stub

			 Log.d(TAG, "[Phone]received a message! sender:" + sender + "; mimeType: " + mimeType + "content:" + msgContent);
			WeaverService.getInstance().onMessage(msgId, msgGlobalId, sender, msgContent, mimeType);
		}

		@Override
		public void onMessageSentResult(int msgId, String msgGlobalId, String msgSentTime, String remote_number, boolean isSuccess, String msgContent, String mimeType, String reason)
		{
			 Log.d(TAG, "[Phone]Message sent result! id: " + msgId + "to:" + remote_number + "; msgGlobalId: " + msgGlobalId + "; mimeType: " + mimeType + "; reason:" + reason);
			WeaverService.getInstance().onMessageSentResult(msgId, msgGlobalId, msgSentTime, remote_number, isSuccess, msgContent, mimeType, reason);
		}

		@Override
		public void onCallStateChange(CallInfo callInfo, int state)
		{
			 Log.d(TAG, "[Phone]Call State Change: " + toString() + " : call id: " + callInfo.getCallConfiguration().getCallToken() + ", state: " + EngineSdkCallState.swigToEnum(state));

			mCurrentCallInfo = callInfo;

			WeaverService.getInstance().onCallStateChange(callInfo, state);
		}

		@Override
		public synchronized void onAccountStateChange(AccountInfo account, int accountState)
		{
			 Log.d(TAG,
					"[Phone]Account State Change: " + toString() + " : account id: " + account.getAccountConfiguration().getPhoneNumber() + ", state: "
							+ EngineSdkAccountState.swigToEnum(accountState));

			mCurrenAccountInfo = account;

			if (accountState == EngineSdkAccountState.ES_STATE_ON_ACC_REGISTERED.swigValue())
			{
				WeaverService.getInstance().onUserStatusChange(UserConstants.UserStatus.ONLINE);
			}
			else
			{
				WeaverService.getInstance().onUserStatusChange(UserConstants.UserStatus.OFFLINE);
			}
		}

		@Override
		public void onNetworkQualtiyChange(int networkQualityIndication)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onEngineStateChange(EngineInfo arg0)
		{
			 Log.d(TAG, "[Phone]Engine State Change: " + toString() + ", state: " + arg0.getEngineState());

			mCurrentEngineInfo = arg0;

		}

		@Override
		public void onInfo(String arg0)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onWaitWakeupTimeout(String arg0, String arg1, String arg2)
		{
			// TODO Auto-generated method stub

		}
	}

	private class SipServiceHookerThread extends Thread
	{
		private final Context mContext;
		private final Intent mServiceIntent;

		public SipServiceHookerThread(Context context)
		{
			if (context == null)
			{
				throw new IllegalArgumentException("Context should not be null！");
			}
			mContext = context;
			mServiceIntent = new Intent(context, SipService.class);
		}

		@Override
		public void run()
		{
			while (true)
			{
				if (!mIsServiceWorking)
				{
					 Log.d(TAG, "SipService hooker->startService()!");
					mContext.getApplicationContext().startService(mServiceIntent);
					mContext.getApplicationContext().bindService(mServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
					try
					{
						Thread.sleep(10000);
					}
					catch (InterruptedException e)
					{
					}
				}
				else
				{
					synchronized (this)
					{
						try
						{
							 Log.d(TAG, "SipService hooker->wait()!");
							wait();
						}
						catch (InterruptedException e)
						{
							// waked-up, means return to work
							 Log.d(TAG, "SipService hooker->notify()!");
						}
					}
				}
			}
		}
	}

	/**
	 * @param context
	 * @param ec
	 * @param inCallActivityName
	 *            传入null则不会修改
	 */
	public void init(Context context, EngineConfiguration ec, String inCallActivityName, Notification notification, int outGoingCallRingtoneId)
	{
		synchronized (mCurrentEngineInfo)
		{
			if (context == null || ec == null)
			{
				throw new IllegalArgumentException("SipServiceForPhone can not be initialized with null params!");
			}

			if (inCallActivityName != null && inCallActivityName.contains("."))
			{
				// 如果传入参数为null则不修改该值
				mInCallActivityName = inCallActivityName;
			}
			else
			{
				mInCallActivityName = null;
			}

			if (mIsRandomCallMode)
			{
				mCustomNotification = null;
			}
			else if (notification != null)
			{
				// 如果传入参数为null则不修改该值
				mCustomNotification = notification;
			}
			
			mOutGoingCallRingtoneId = outGoingCallRingtoneId;

			mCurrentEngineInfo.setEngineConfiguration(ec);
			mCurrentEngineInfo.setEngineState(EngineSdkEngineState.ES_STATE_ON_ENGINE_DISCONNECTED);

			// 如果sipservice已经连接，直接调用init
			if (mIsServiceWorking && mSipService != null)
			{
				try
				{
					 Log.d(TAG, "--------------init: sip4phone1");
					mSipService.init(mCurrentEngineInfo.getEngineConfiguration(), mInCallActivityName, mOutGoingCallRingtoneId);
					if (mServiceListenerImpl != null)
					{
						 Log.d(TAG, "mSipService.setRPCListener: " + mServiceListenerImpl.toString());
						mSipService.setRPCListener(mServiceListenerImpl, mProcessName);
					}
					if (mCustomNotification != null)
					{
						mSipService.setCustomNotificationView(188, mCustomNotification);// 188为临时设置的一个幻数
					}
				}
				catch (RemoteException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				// 否则初始化连接线程
				if (mServiceHooker == null)
				{
					mServiceHooker = new SipServiceHookerThread(context);
					mServiceHooker.start();
				}
			}
		}
	}
	
	public void setNotificationView(Notification notification) {
		mCustomNotification = notification;
		try {
			 Log.d("zhangqiang78", "ini");
			if(mSipService != null) {
				mSipService.setCustomNotificationView(188, mCustomNotification);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			 Log.d("zhangqiang78", "RemoteException");
			e.printStackTrace();
		}
	}
	
	private void playRingtone(int ringtoneType)
	{
		if (SipServiceAvailableCheck("playRingtone") == false)
		{
			return;
		}

		try
		{
			mSipService.playRingtone(ringtoneType);
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when playRingtone", e);
		}
	}
	
	private void stopPlaying()
	{
		if (SipServiceAvailableCheck("stopPlaying") == false)
		{
			return;
		}

		try
		{
			mSipService.stopPlaying();
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when stopPlaying", e);
		}
	}

	private void setInCallingMode(int mode)
	{
		if (SipServiceAvailableCheck("setInCallingMode") == false)
		{
			return;
		}

		try
		{
			mSipService.setInCallingMode(mode);
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when setInCallingMode", e);
		}
	}
	
	private void setHandfree(boolean isHandfreeOn)
	{
		if (SipServiceAvailableCheck("setHandfree") == false)
		{
			return;
		}

		try
		{
			mSipService.setHandfree(isHandfreeOn);
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when setHandfree", e);
		}
	}
	
	private void setEarphoneConnected(boolean isEarphoneConnected)
	{
		if (SipServiceAvailableCheck("setEarphoneConnected") == false)
		{
			return;
		}

		try
		{
			mSipService.setEarphoneConnected(isEarphoneConnected);
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when setEarphoneConnected", e);
		}
	}
	
	private void setBluetoothConnected(boolean isBluetoothConnected)
	{
		if (SipServiceAvailableCheck("setBluetoothConnected") == false)
		{
			return;
		}

		try
		{
			mSipService.setBluetoothConnected(isBluetoothConnected);
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when setBluetoothConnected", e);
		}
	}
	
	private void operateSystemAudio()
	{
		if (SipServiceAvailableCheck("operateSystemAudio") == false)
		{
			return;
		}

		try
		{
			mSipService.operateSystemAudio();
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when operateSystemAudio", e);
		}
	}
	
	private void entryInCallState()
	{
		if (SipServiceAvailableCheck("entryInCallState") == false)
		{
			return;
		}

		try
		{
			mSipService.entryInCallState();
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when entryInCallState", e);
		}
	}
	
	private void leaveInCallState()
	{
		if (SipServiceAvailableCheck("leaveInCallState") == false)
		{
			return;
		}

		try
		{
			mSipService.leaveInCallState();
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when leaveInCallState", e);
		}
	}

	public void setUserAccount(String userId, String userPassword, String userDomain)
	{
		 Log.d(TAG, "setUserAccount:id=" + userId + ", password=" + userPassword + ", domain=" + userDomain);
		//UserPreferences.setBool(UserPreferences.Key.IS_AUTO_REGISTER, true);

		if (mSipService == null)
		{
			 Log.d(TAG, "[setUserAccount]SipService is null! Check whether init() has been called!");
			return;
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[setUserAccount]SipService is not working!");
			return;
		}

		AccountConfiguration configuration = new AccountConfiguration();
		configuration.setPhoneNumber(userId);
		configuration.setPassword(userPassword);
		configuration.setAppDomain(userDomain);
		configuration.setRandomCallMode(mIsRandomCallMode);
		 Log.d(TAG, "setUserAccount: setRandomCallMode  " + mIsRandomCallMode);
		try
		{
			mSipService.setUserAccount(configuration);
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when setUserAccount", e);
		}
	}

	public void deleteAccount()
	{
		 Log.d(TAG, "[SipServiceForPhone]deleteAccount, token deleted also, will not automatically login until user manually login.");
		//UserPreferences.setBool(UserPreferences.Key.IS_AUTO_REGISTER, false);
		if (mSipService == null)
		{
			 Log.d(TAG, "[deleteAccount]SipService is null! Check whether init() has been called!");
			return;
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[deleteAccount]SipService is not working!");
			return;
		}

		try
		{
			mSipService.deleteAccount();
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when deleteAccount", e);
		}

	}
	
	public void setForcedRelayPath(String relay_path)
	{
		 Log.d(TAG, "[SipServiceForPhone]setForcedRelayPath:" + relay_path);
		//UserPreferences.setBool(UserPreferences.Key.IS_AUTO_REGISTER, false);
		if (mSipService == null)
		{
			 Log.d(TAG, "[setForcedRelayPath]SipService is null! Check whether init() has been called!");
			return;
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[setForcedRelayPath]SipService is not working!");
			return;
		}

		try
		{
			mSipService.setForcedRelayPath(relay_path);
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when setForcedRelayPath", e);
		}
	}

	public void setDown2Audio(boolean pause)
	{
		 Log.d(TAG, "[SipServiceForPhone]setDown2Audio:" + pause);
		if (mSipService == null)
		{
			 Log.d(TAG, "[setDown2Audio]SipService is null! Check whether init() has been called!");
			return;
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[setDown2Audio]SipService is not working!");
			return;
		}

		try
		{
			mSipService.setDown2Audio(pause);
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when setDown2Audio", e);
		}
	}
	
	public void setTransmitingVideoPaused(boolean pause)
	{
		 Log.d(TAG, "[SipServiceForPhone]setTransmitingVideoPaused:" + pause);
		if (mSipService == null)
		{
			 Log.d(TAG, "[setTransmitingVideoPaused]SipService is null! Check whether init() has been called!");
			return;
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[setTransmitingVideoPaused]SipService is not working!");
			return;
		}

		try
		{
			mSipService.setTransmitingVideoPaused(pause);
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when setTransmitingVideoPaused", e);
		}
	}
	
	
	public void requestRemotePauseTransmitingVideo(boolean pause, boolean allowResetByPeer)
	{
		 Log.d(TAG, "[SipServiceForPhone]requestRemotePauseTransmitingVideo:" + pause);
		if (mSipService == null)
		{
			 Log.d(TAG, "[requestRemotePauseTransmitingVideo]SipService is null! Check whether init() has been called!");
			return;
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[requestRemotePauseTransmitingVideo]SipService is not working!");
			return;
		}

		try
		{
			mSipService.requestRemotePauseTransmitingVideo(pause, allowResetByPeer);
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when requestRemotePauseTransmitingVideo", e);
		}
	}
	
	public void makeCall(String to, String gid, boolean isAudioOnly)
	{
		if (mSipService == null)
		{
			 Log.d(TAG, "[makeCall]SipService is null! Check whether init() has been called!");
			return;
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[makeCall]SipService is not working!");
			return;
		}

		try
		{
			mSipService.makeCall(to, gid, true, !isAudioOnly);
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when makeCall", e);
		}
	}

	public void answerCall(boolean isAudioOnly)
	{
		if (mSipService == null)
		{
			 Log.d(TAG, "[makeCall]SipService is null! Check whether init() has been called!");
			return;
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[makeCall]SipService is not working!");
			return;
		}

		try
		{
			mSipService.answerCall(true, !isAudioOnly);
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when makeCall", e);
		}
	}
	
	public void endCall(long callToken)
	{
		if (mSipService == null)
		{
			 Log.d(TAG, "[endCall]SipService is null! Check whether init() has been called!");
			return;
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[endCall]SipService is not working!");
			return;
		}

		try
		{
			mSipService.endCall("");
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when makeCall", e);
		}
	}

	public int sendMessage(String to, String message, String mimeType, EngineSdkMsgSender senderModule)
	{
		if (mSipService == null)
		{
			 Log.d(TAG, "[sendMessage]SipService is null! Check whether init() has been called!");
			return -1;
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[sendMessage]SipService is not working!");
			return -1;
		}

		try
		{
			return mSipService.sendMessage(to, message, mimeType, senderModule.swigValue());
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when sendMessage", e);
			return -1;
		}
	}

	public void setAudioControlMode(int mode)
	{
		if (mSipService == null)
		{
			 Log.d(TAG, "[setAudioControlMode]SipService is null! Check whether init() has been called!");
			return;
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[setAudioControlMode]SipService is not working!");
			return;
		}

		UserPreferences.setInt(UserPreferences.Key.AUDIO_CONFIG, mode);
	}

	private void pauseInCallOperate()
	{
		if (mSipService == null)
		{
			 Log.d(TAG, "[puaseInCallOperate]SipService is null! Check whether init() has been called!");
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[puaseInCallOperate]SipService is not working!");
		}

		try
		{
			mSipService.pauseInCallOperate();
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when puaseInCallOperate", e);
		}
	}

	private void resumeInCallOperate()
	{
		if (mSipService == null)
		{
			 Log.d(TAG, "[resumeInCallOperate]SipService is null! Check whether init() has been called!");
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[resumeInCallOperate]SipService is not working!");
		}

		try
		{
			mSipService.resumeInCallOperate();
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when resumeInCallOperate", e);
		}
	}
	
	private void callDestroyEngine()
	{
		if (mSipService == null)
		{
			 Log.d(TAG, "[callDestroyEngine]SipService is null! Check whether init() has been called!");
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[callDestroyEngine]SipService is not working!");
		}

		try
		{
			Log.d(TAG, "[callDestroyEngine] is calling!");
			mSipService.destroyEngine();
		}
		catch (RemoteException e)
		{
			 Log.e(TAG, "RemoteException when callDestroyEngine", e);
		}
	}
	
	public CallInfo getCurrentCallInfo()
	{
		return mCurrentCallInfo;
	}

	public synchronized AccountInfo getCurrentAccountInfo()
	{
		return mCurrenAccountInfo;
	}

	public EngineInfo getCurrentEngineInfo()
	{
		return mCurrentEngineInfo;
	}
	
	private boolean SipServiceAvailableCheck(String functionName)
	{
		 Log.d(TAG, "[SipServiceForPhone]" + functionName);
		if (mSipService == null)
		{
			 Log.d(TAG, "[" + functionName + "]SipService is null! Check whether init() has been called!");
			return false;
		}

		if (!mIsServiceWorking)
		{
			 Log.d(TAG, "[" + functionName + "]SipService is not working!");
			return false;
		}
		return true;
	}
}
