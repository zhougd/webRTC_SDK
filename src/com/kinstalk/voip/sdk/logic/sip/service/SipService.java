package com.kinstalk.voip.sdk.logic.sip.service;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.kinstalk.voip.sdk.EngineSdkAccountInformation;
import com.kinstalk.voip.sdk.EngineSdkAccountState;
import com.kinstalk.voip.sdk.EngineSdkCallConfiguration;
import com.kinstalk.voip.sdk.EngineSdkCallInformation;
import com.kinstalk.voip.sdk.EngineSdkCallState;
import com.kinstalk.voip.sdk.EngineSdkEngineConfiguration;
import com.kinstalk.voip.sdk.EngineSdkEngineInformation;
import com.kinstalk.voip.sdk.EngineSdkMsgSender;
import com.kinstalk.voip.sdk.EngineSdkNetworkType;
import com.kinstalk.voip.sdk.EngineSdkServerType;
import com.kinstalk.voip.sdk.EngineSdkSessionDirection;
import com.kinstalk.voip.sdk.common.CollectionUtility;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.UserPreferences;
import com.kinstalk.voip.sdk.logic.sip.aidl.ISipService;
import com.kinstalk.voip.sdk.logic.sip.aidl.ISipServiceListener;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.AccountConfiguration;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.AccountInfo;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.CallInfo;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.EngineConfiguration;
import com.kinstalk.voip.sdk.logic.sip.delegate.EngineListener;
import com.kinstalk.voip.sdk.logic.sip.receiver.AlarmReceiver;
import com.kinstalk.voip.sdk.logic.sip.receiver.EarphoneEventReceiver;
import com.kinstalk.voip.sdk.logic.sip.receiver.NetworkStatusReceiver;
import com.kinstalk.voip.sdk.logic.sip.receiver.PhoneStateReceiver;
import com.kinstalk.voip.sdk.logic.sip.utility.AudioIncallManager;
import com.kinstalk.voip.sdk.logic.sip.utility.MediaManager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * SipService用于取代以前的SipService4Phone。新的SipService将直接放入EngineSDK中，这样未来第三方也可以直接使用。<br>
 * 
 * SipService将提供Intent（无回调）和aidl（可回调）两套接口供客户端使用。
 * 
 * 目前还未完成的功能<br>
 * 1、Intent接口尚未提供（有两个回调是intent）<br>
 * 2、作为后台服务，SipService还需要完善的逻辑来保护自身活动，并且能够准确识别用户状态，以当客户端发起某个行为时能够准确地完成<br>
 * 
 * @author luolong1
 * 
 */
public class SipService extends Service implements EngineListener
{
	public static final String TAG = "SipService";
	private static EngineLoader mEngineLoader;
	private ISipServiceListener mISipServiceEngineListener;
	private ISipServiceListener mISipServiceEngineListenerLast;
	private LinkedList<ISipServiceListenerIdentifier> mISipServiceEngineListeners = new LinkedList<ISipServiceListenerIdentifier>();
	private Intent mIncallActivityAction = null;
	private Notification mNotification = null;
	private int mNotificationId;

	private String mIncallActivityClassName;
	private boolean mIsRandomCallMode;
	private EngineSdkServerType mServerType = EngineSdkServerType.ES_OFFICIAL_ONLINE_SERVER;
	
	private static final String REPORT_CALL_INFO_URL = "http://gslb.kinstalk.com:9000/biReport/CallInformation/";
	
	public class ISipServiceListenerIdentifier {
		public ISipServiceListener mISipServiceEngineListener;
		public String mProcessName;

		public ISipServiceListenerIdentifier(ISipServiceListener iSipServiceEngineListener, String processName)
		{
			mISipServiceEngineListener = iSipServiceEngineListener;
			mProcessName = processName;
		}
	}

	private final ISipService.Stub mIBbinder = new ISipService.Stub()
	{
		@Override
		public void deleteAccount() throws RemoteException
		{
			mEngineLoader.deleteAccount();
		}

		@Override
		public void destroyEngine() throws RemoteException
		{
			mEngineLoader.destroyEngine();
		}

		@Override
		public synchronized void init(EngineConfiguration arg0, String inCallActivityName, int outGoingCallRingtoneId) throws RemoteException
		{
			AssetFileDescriptor afd = getResources().openRawResourceFd(outGoingCallRingtoneId);
			MediaManager.init(SipService.this, afd);// 初始化去电铃音
			try
			{
				EngineSdkEngineConfiguration ec = arg0.getEsEngineConfiguration();
				mEngineLoader.init(ec, SipService.this, SipService.this);
				mServerType = ec.getServerType();
				 Log.d(TAG, "Set serverType to " + mServerType.toString());

				mIncallActivityClassName = inCallActivityName;
				if (mIncallActivityClassName != null && mIncallActivityClassName.contains("."))
				{
					try
					{
						mIncallActivityAction = new Intent(SipService.this, Class.forName(mIncallActivityClassName));
						mIncallActivityAction.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						if (mNotification != null && mNotification.contentIntent == null) {
							mNotification.contentIntent = PendingIntent.getActivity(SipService.this, 0, mIncallActivityAction, 0);
						}
					}
					catch (ClassNotFoundException e)
					{
						 Log.e(TAG, "", e);
					}
				}

				 Log.d(TAG, "setAudioConfigue by init: " + UserPreferences.getInt(UserPreferences.Key.AUDIO_CONFIG, -1));

				// AudioIncallManager.getInstance(SipService.this).setAudioConfigue(UserPreferences.getInt(UserPreferences.Key.AUDIO_CONFIG,
				// -1));

			}
			catch (Exception e)
			{
				 Log.e(TAG, "", e);
			}
		}
		
		@Override
		public ISipServiceListener setRPCListener(ISipServiceListener listener,
				String listenerProcessName) throws RemoteException {
			// TODO Auto-generated method stub
			ISipServiceListener lastObj = mISipServiceEngineListener;
			mISipServiceEngineListenerLast = mISipServiceEngineListener;
			mISipServiceEngineListener = listener;
			 Log.d(TAG, "ISipServiceListener setRPCListener: " + listener.toString() + "process: " + listenerProcessName);
			if (mISipServiceEngineListener != null)
			{
				AccountInfo info = mEngineLoader.getAccountInfo();
				if (info != null)
				{
					try
					{
						//AccountInfo info = new AccountInfo(mEngineLoader.getCurrentAccountInfo());
						mISipServiceEngineListener.onAccountStateChange(info, info.getAccountState().swigValue());
					}
					catch (RemoteException e)
					{
						 Log.e(TAG, "", e);
					}
				}
				 Log.d(TAG, "setRPCListener count before:" + mISipServiceEngineListeners.size());
				synchronized (mISipServiceEngineListeners)
				{
					if (mISipServiceEngineListeners.size() > 0)
					{
						Iterator<ISipServiceListenerIdentifier> i = mISipServiceEngineListeners.iterator();
						ISipServiceListenerIdentifier cur = null;
						while (i.hasNext())
						{
							cur = i.next();
							if (cur == null || cur.mProcessName.equals(listenerProcessName))
							{
								 Log.d(TAG, "setRPCListener found duplicate object: " + cur.mISipServiceEngineListener.toString() + "for process " +  listenerProcessName + " so removed.");
								i.remove();
							}
						}
					}
				}
				CollectionUtility.addStrongReference(mISipServiceEngineListeners, new ISipServiceListenerIdentifier(listener, listenerProcessName));
				 Log.d(TAG, "setRPCListener count after:" + mISipServiceEngineListeners.size());
			}

			return lastObj;
		}

		@Override
		public void makeCall(String arg0, String arg3, boolean arg1, boolean arg2) throws RemoteException
		{
			if (arg0 == null)
			{
				throw new IllegalArgumentException("Null target number!");
			}
			EngineSdkCallConfiguration conf = new EngineSdkCallConfiguration();
			if (mEngineLoader.getCurrentAccountInfo() != null)
			{
				conf.setLocalAccountId(mEngineLoader.getCurrentAccountInfo().getAccountConfiguration().getLocalAccountId());
			}
			conf.setRemoteAccountId(arg0);
			conf.setAudioChannelEnabled(arg1);
			Log.e(TAG, "setLocalDisplayName GID :"+arg3);
			conf.setLocalDisplayName(arg3);
			conf.setMainVideoChannelEnabled(arg2);
			conf.setSecondVideoChannelEnabled(false);
			conf.setDataVideoChannelEnabled(false);
			conf.setCallDirection(EngineSdkSessionDirection.OUTGOING);
			conf.setIsRandomCall(mIsRandomCallMode);
			 Log.d(TAG, "Start call with mIsRandomCallMode = " + mIsRandomCallMode);
			EngineSdkCallInformation call = new EngineSdkCallInformation();
			call.setCallConfiguration(conf);

			getLoader().setCurrentCallinfo(call);
			getLoader().makeCall(conf, null);
			startIncallActivity(mIncallActivityAction);
		}

		@Override
		public int sendMessage(String to, String message, String mimeType, int senderModule) throws RemoteException
		{
			return mEngineLoader.sendMessage(to, message, mimeType, EngineSdkMsgSender.swigToEnum(senderModule));
		}

		@Override
		public void setUserAccount(AccountConfiguration arg0) throws RemoteException
		{
			mEngineLoader.setUserAccount(arg0.getAppDomain(), arg0.getPhoneNumber(), arg0.getPassword());

			mIsRandomCallMode = arg0.isRandomCallMode();
		}
		
		@Override
		public void setDown2Audio(boolean pause) throws RemoteException
		{
			mEngineLoader.setDown2Audio(pause);
		}
		
		@Override
		public void setTransmitingVideoPaused(boolean pause) throws RemoteException
		{
			mEngineLoader.setTransmitingVideoPaused(pause);
		}
		
		@Override
		public void requestRemotePauseTransmitingVideo(boolean pause, boolean allowResetByPeer) throws RemoteException
		{
			mEngineLoader.requestRemotePauseTransmitingVideo(pause, allowResetByPeer);
		}
		
		@Override
		public void endCall(String reasonText) throws RemoteException
		{
			mEngineLoader.endCall(new EngineSdkCallInformation(), reasonText);
		}

		@Override
		public void setNetworkType(int netType) throws RemoteException
		{
			mEngineLoader.setNetworkType(EngineSdkNetworkType.swigToEnum(netType));
		}

		@Override
		public void addClientFeature(String featureName) throws RemoteException
		{
			mEngineLoader.addClientFeature(featureName);
		}

		@Override
		public void setAudioControlMode(int mode) throws RemoteException
		{
			 Log.d(TAG, "setAudioConfigue by setAudioControlMode: " + mode);

			UserPreferences.setInt(UserPreferences.Key.AUDIO_CONFIG, mode);
			// AudioIncallManager.getInstance(getApplicationContext()).setAudioConfigue(mode);
		}

		@Override
		public synchronized void setCustomNotificationView(int notificationId, Notification notification) throws RemoteException
		{
			setNotificationView(notificationId, notification);
		}

		@Override
		public void pauseInCallOperate() throws RemoteException
		{
			AudioIncallManager.getInstance(getApplicationContext()).puaseInCallOperate();
		}

		@Override
		public void resumeInCallOperate() throws RemoteException
		{
			AudioIncallManager.getInstance(getApplicationContext()).resumeInCallOperate();
		}

		@Override
		public void playRingtone(int ringtoneType) throws RemoteException {
			// TODO Auto-generated method stub
			MediaManager.getInstance().playRingtone(ringtoneType);
		}

		@Override
		public void stopPlaying() throws RemoteException {
			// TODO Auto-generated method stub
			MediaManager.getInstance().stopPlaying();
		}

		@Override
		public void setInCallingMode(int mode) throws RemoteException {
			// TODO Auto-generated method stub
			MediaManager.getInstance().setInCallingMode(mode);
		}

		@Override
		public void setHandfree(boolean isHandfreeOn) throws RemoteException {
			// TODO Auto-generated method stub
			MediaManager.getInstance().setHandfree(isHandfreeOn);
		}

		@Override
		public void setEarphoneConnected(boolean isEarphoneConnected) throws RemoteException {
			// TODO Auto-generated method stub
			MediaManager.getInstance().setEarphoneConnected(isEarphoneConnected);
		}
		
		@Override
		public void setBluetoothConnected(boolean isBluetoothConnected) throws RemoteException {
			// TODO Auto-generated method stub
			MediaManager.getInstance().setBluetoothConnected(isBluetoothConnected);
		}
		
		@Override
		public void operateSystemAudio() throws RemoteException {
			// TODO Auto-generated method stub
			MediaManager.getInstance().operateSystemAudio();
		}

		@Override
		public void entryInCallState() throws RemoteException {
			// TODO Auto-generated method stub
			AudioIncallManager.getInstance(getApplicationContext()).entryInCallState();
		}

		@Override
		public void leaveInCallState() throws RemoteException {
			// TODO Auto-generated method stub
			AudioIncallManager.getInstance(getApplicationContext()).leaveInCallState();
		}

		@Override
		public void answerCall(boolean isAudioOn, boolean isMainVideoOn)
				throws RemoteException {
			// TODO Auto-generated method stub

			EngineSdkCallInformation call = getLoader().getCurrentCallInfo();

			call.getCallConfiguration().setAudioChannelEnabled(isAudioOn);
			call.getCallConfiguration().setMainVideoChannelEnabled(isMainVideoOn);
			call.getCallConfiguration().setSecondVideoChannelEnabled(false);
			call.getCallConfiguration().setDataVideoChannelEnabled(false);
			call.getCallConfiguration().setIsRandomCall(mIsRandomCallMode);
			 Log.d(TAG, "Answer call with mIsRandomCallMode = " + mIsRandomCallMode);
		
			getLoader().answerCall(call, null);
			startIncallActivity(mIncallActivityAction);
		}

		@Override
		public void setForcedRelayPath(String relay_path)
				throws RemoteException {
			// TODO Auto-generated method stub
			getLoader().setForcedRelayPath(relay_path);
		}
	};

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		 Log.d(TAG, "SipService OnBind~~");
		return mIBbinder;
	}

	public ISipService.Stub getBinder()
	{
		return mIBbinder;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		mEngineLoader = EngineLoader.getInstance();
		mEngineLoader.setService(this);
		// startForeground(hashCode(), new Notification());

		String temppath = Environment.getExternalStorageDirectory().getAbsolutePath(); // this.getExternalFilesDir(null).getAbsolutePath();
		mEngineLoader.setAndroidFilePath(temppath);

		// 注册网络变化监听
		NetworkStatusReceiver receiver = new NetworkStatusReceiver(this);
		registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

		// 耳机事件
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_HEADSET_PLUG);
		filter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
		registerReceiver(new EarphoneEventReceiver(), filter);

		// 监听来电
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mTelephonyMgr.listen(new PhoneStateReceiver(this), PhoneStateListener.LISTEN_CALL_STATE);

		// 5分钟alarm
		AlarmManager manager = (AlarmManager) this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		Intent intent2 = new Intent(AlarmReceiver.ACTION_SIP_KEEP_ALIVE);
		PendingIntent pending2 = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent2, 0);
		manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5 * 60 * 1000, pending2);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		stopForeground();
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onEngineStateChange(EngineSdkEngineInformation p_state)
	{
		// TODO Auto-generated method stub

	}

	private void startIncallActivity(Intent intent)
	{
		if (intent != null)
		{
		    startActivity(intent);
		}
	}
	
	void processIncommingCallInWeaver(EngineSdkCallInformation callInfo)
	{
		if (mIsRandomCallMode == true)
		{
			if (callInfo.getCallConfiguration().getIsRandomCall() == false)
			{
				getLoader().endCall(callInfo, "General call arriving wrongly.");
				 Log.d(TAG, "General call arriving wrongly in random call mode, so reject directly.");
			}
			else
			{
				 Log.d(TAG, "Correct random call arriving.");
			}
		}
		else
		{
			if (callInfo.getCallConfiguration().getIsRandomCall() == false)
			{
				startIncallActivity(mIncallActivityAction);
				startForeground();
				 Log.d(TAG, "Correct general call arriving.");
			}
			else
			{
				getLoader().endCall(callInfo, "Random call arriving wrongly.");
				 Log.d(TAG, "Random call arriving wrongly in general call mode, so reject directly.");
			}
		}
	}

	void processIncommingCallInManGirlPapa(EngineSdkCallInformation callInfo)
	{
		if (!mIsRandomCallMode)
		{
			startIncallActivity(mIncallActivityAction);
			startForeground();
		}
	}
	
	private void callBackAllISipServiceCallStateListener(EngineSdkCallInformation callInfo, EngineSdkCallState state)
	{
		synchronized (mISipServiceEngineListeners)
		{
			 Log.d(TAG, "callBackAllISipServiceCallStateListener count:" + mISipServiceEngineListeners.size());
			if (mISipServiceEngineListeners.size() > 0)
			{
				Iterator<ISipServiceListenerIdentifier> i = mISipServiceEngineListeners.iterator();
				ISipServiceListenerIdentifier cur = null;
				while (i.hasNext())
				{
					cur = i.next();
					if (cur == null)
					{
						 Log.d(TAG, "callBackAllISipServiceCallStateListener:found dropped object: null");
						i.remove();
					}
					else
					{
						 Log.d(TAG, "callBackAllISipServiceCallStateListener:notifying object:" + cur.toString());
						try {
							cur.mISipServiceEngineListener.onCallStateChange(new CallInfo(callInfo), state.swigValue());
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							 Log.e(TAG, "", e);
						}
					}
				}
			}
			else
			{
				 Log.d(TAG, "Abandoned a onCallStateChange event due to null mEngineListener!");
			}
		}
	}

	@Override
	public void onCallStateChange(EngineSdkCallInformation callInfo, EngineSdkCallState state)
	{
		 Log.d(TAG, "Call State Change: " + toString() + " : call id: " + callInfo.getCallConfiguration().getCallLocalToken() + ", state: " + callInfo.getCallState());
		if (state == EngineSdkCallState.ES_STATE_ON_CALL_INCOMMING_CALL)
		{
			switch (mServerType)
			{
				case ES_PAPA_ONLINE_SERVER:
					processIncommingCallInManGirlPapa(callInfo);
					break;
				case ES_OFFICIAL_ONLINE_SERVER:
				case ES_TESTER_TESTING_SERVER:
				case ES_ENGINEER_DEVELOPMENT_SERVER:
				case ES_QINYOUYUE_ONLINE_SERVER:
				case ES_QINYOUYUE_TEST_SERVER:
				case ES_QINYOUYUE_DEV_SERVER:
					processIncommingCallInWeaver(callInfo);
					break;
			}
		}
		else if (state == EngineSdkCallState.ES_STATE_ON_CALL_CALL_ENDING || state == EngineSdkCallState.ES_STATE_ON_CALL_CALL_ENDED)
		{
			stopForeground();

			if (state == EngineSdkCallState.ES_STATE_ON_CALL_CALL_ENDED)
			{
				createSendBICallParam(callInfo);
			}
		}
		else if (state == EngineSdkCallState.ES_STATE_ON_CALL_CALLING_OUT)
		{
			startForeground();
		}
		callBackAllISipServiceCallStateListener(callInfo, state);
	}

	private void callBackAllISipServiceAccListener(EngineSdkAccountInformation account, EngineSdkAccountState state)
	{
		synchronized (mISipServiceEngineListeners)
		{
			 Log.d(TAG, "callBackAllISipServiceAccListener count:" + mISipServiceEngineListeners.size());
			if (mISipServiceEngineListeners.size() > 0)
			{
				Iterator<ISipServiceListenerIdentifier> i = mISipServiceEngineListeners.iterator();
				ISipServiceListenerIdentifier cur = null;
				while (i.hasNext())
				{
					cur = i.next();
					if (cur == null)
					{
						 Log.d(TAG, "callBackAllISipServiceAccListener:found dropped object: null");
						i.remove();
					}
					else
					{
						 Log.d(TAG, "callBackAllISipServiceAccListener:notifying object:" + cur.toString());
						try {
							cur.mISipServiceEngineListener.onAccountStateChange(new AccountInfo(account), state.swigValue());
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							 Log.e(TAG, "", e);
						}
					}
				}
			}
			else
			{
				 Log.d(TAG, "Abandoned a onAccountStateChange event due to null mEngineListener!");
			}
		}
	}
	@Override
	public void onAccountStateChange(EngineSdkAccountInformation account, EngineSdkAccountState state)
	{
		callBackAllISipServiceAccListener(account, state);
	}
	
	private void callBackAllISipServiceMessageRecvListener(int msgId, String msgGlobalId, String sentTime, String sender, String msgContent, String mimeType, EngineSdkMsgSender senderModule)
	{
		synchronized (mISipServiceEngineListeners)
		{
			 Log.d(TAG, "callBackAllISipServiceMessageRecvListener count:" + mISipServiceEngineListeners.size());
			if (mISipServiceEngineListeners.size() > 0)
			{
				Iterator<ISipServiceListenerIdentifier> i = mISipServiceEngineListeners.iterator();
				ISipServiceListenerIdentifier cur = null;
				while (i.hasNext())
				{
					cur = i.next();
					if (cur == null)
					{
						 Log.d(TAG, "callBackAllISipServiceMessageRecvListener:found dropped object: null");
						i.remove();
					}
					else
					{
						 Log.d(TAG, "callBackAllISipServiceMessageRecvListener:notifying object:" + cur.toString());
						try {
							cur.mISipServiceEngineListener.onMessage(msgId, msgGlobalId, sender, msgContent, mimeType);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							 Log.e(TAG, "", e);
						}
					}
				}
			}
			else
			{
				 Log.d(TAG, "Abandoned a onMessage event due to null mEngineListener!");
			}
		}
	}

	@Override
	public void onMessage(int msgId, String msgGlobalId, String sentTime, String sender, String msgContent, String mimeType, EngineSdkMsgSender senderModule)
	{
		// if ("text/x-surprise".equals(mimeType))
		// {
		// Intent it = new Intent("com.kinstalk.voip.sdk.surprise.get");
		// it.putExtra("id", msgContent);
		// it.putExtra("to", sender);
		// getApplication().sendBroadcast(it);
		// return;
		// }

		Intent msgIntent = new Intent("com.kinstalk.voip.sdk.message");
		msgIntent.putExtra("msgLocalId", msgId);
		msgIntent.putExtra("msgGlobalId", msgGlobalId);
		msgIntent.putExtra("sender", sender);
		msgIntent.putExtra("msgContent", msgContent);
		msgIntent.putExtra("mimeType", mimeType);
		msgIntent.putExtra("senderModule", senderModule.swigValue());
		msgIntent.putExtra("sentTime", sentTime);

		sendBroadcast(msgIntent);

		 Log.d(TAG, "-------------Sending a message intent~~~");

		callBackAllISipServiceMessageRecvListener(msgId, msgGlobalId, sentTime, sender, msgContent, mimeType, senderModule);
	}

	@Override
	public void onWaitWakeupTimeout(String wakeup_caller_num, String wakeup_callee_num, String last_waiting_sip_callID)
	{
		// TODO Auto-generated method stub

	}

	public EngineLoader getLoader()
	{
		return mEngineLoader;
	}

	private synchronized void startForeground()
	{
		if (mNotification != null)
		{
			// mNotification.when = System.currentTimeMillis();
			// mNotification.contentView.setLong(com.android.internal.R.id.time, "setTime",
			// System.currentTimeMillis());
			startForeground(mNotificationId, mNotification);
		}
	}

	private void stopForeground()
	{
		stopForeground(true);
	}

	public void callBackAllISipServiceMessageSentResultListener(int msgId, String msgGlobalId, String msgSentTime, String remote_number, boolean isSuccess, String msgContent, String im_mime_type, String reason,
			EngineSdkMsgSender senderModule)
	{
		synchronized (mISipServiceEngineListeners)
		{
			 Log.d(TAG, "callBackAllISipServiceMessageSentResultListener count:" + mISipServiceEngineListeners.size());
			if (mISipServiceEngineListeners.size() > 0)
			{
				Iterator<ISipServiceListenerIdentifier> i = mISipServiceEngineListeners.iterator();
				ISipServiceListenerIdentifier cur = null;
				while (i.hasNext())
				{
					cur = i.next();
					if (cur == null)
					{
						 Log.d(TAG, "callBackAllISipServiceMessageSentResultListener:found dropped object: null");
						i.remove();
					}
					else
					{
						 Log.d(TAG, "callBackAllISipServiceMessageSentResultListener:notifying object:" + cur.toString());
						try {
							cur.mISipServiceEngineListener.onMessageSentResult(msgId, msgGlobalId, msgSentTime, remote_number, isSuccess, msgContent, im_mime_type, reason);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							 Log.e(TAG, "", e);
						}
					}
				}
			}
			else
			{
				 Log.d(TAG, "Abandoned a onMessage event due to null mEngineListener!");
			}
		}
	}
	
	@Override
	public void onMessageSentResult(int msgId, String msgGlobalId, String msgSentTime, String remote_number, boolean isSuccess, String msgContent, String im_mime_type, String reason,
			EngineSdkMsgSender senderModule)
	{
		Intent msgIntent = new Intent("com.kinstalk.voip.sdk.message_sent_result");
		msgIntent.putExtra("msgLocalId", msgId);
		msgIntent.putExtra("msgGlobalId", msgGlobalId);
		msgIntent.putExtra("msgSentTime", msgSentTime);
		msgIntent.putExtra("receiver", remote_number);
		msgIntent.putExtra("isSuccess", isSuccess);
		msgIntent.putExtra("msgContent", msgContent);
		msgIntent.putExtra("reason", reason);
		msgIntent.putExtra("mimeType", im_mime_type);
		msgIntent.putExtra("senderModule", senderModule.swigValue());

		sendBroadcast(msgIntent);
		callBackAllISipServiceMessageSentResultListener(msgId, msgGlobalId, msgSentTime, remote_number, isSuccess, msgContent, im_mime_type, reason, senderModule);
	}

	public void setNotificationView(int notificationId, Notification customNotification)
	{
		mNotification = customNotification;
		mNotificationId = notificationId;
		
		if (customNotification != null && mIncallActivityAction != null)
		{			
			mNotification.contentIntent = PendingIntent.getActivity(this, 0, mIncallActivityAction, 0);
		}
		else if (mNotification != null)
		{
			mNotification.contentIntent = null;
		}
	}

	private void sendBICallParam(String param) {
		try {
			Log.e(TAG, "shaoyun: " + param);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(REPORT_CALL_INFO_URL);
			
			// 添加http头信息
			httppost.addHeader("Content-Type", "application/json");
			httppost.setEntity(new StringEntity(param));
			
			HttpResponse response;
			response = httpclient.execute(httppost);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				Log.d(TAG, "sendBICallParam sucess");
			} else {
				Log.e(TAG, "sendBICallParam error: " + code + ", " + response.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createSendBICallParam(EngineSdkCallInformation callInfo)
	{
		BIInfo biInfo = new BIInfo();
		biInfo.SetCallInfo(callInfo);
		
		String jsonParam = new Gson().toJson(biInfo);
		//sendBICallParam(jsonParam);
	}

	private long convertOsTimeToMs(long pSec, long pMs)
	{
		long lOsTime = 0;
		lOsTime = pSec * 1000 + pMs;
		return lOsTime;
	}
}
