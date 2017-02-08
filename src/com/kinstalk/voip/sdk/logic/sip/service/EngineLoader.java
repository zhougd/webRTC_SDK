package com.kinstalk.voip.sdk.logic.sip.service;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;

import android.content.Context;
import android.os.PowerManager;

import com.kinstalk.voip.sdk.EngineSdk;
import com.kinstalk.voip.sdk.EngineSdkAccountCallback;
import com.kinstalk.voip.sdk.EngineSdkAccountConfiguration;
import com.kinstalk.voip.sdk.EngineSdkAccountInformation;
import com.kinstalk.voip.sdk.EngineSdkAccountState;
import com.kinstalk.voip.sdk.EngineSdkAudioOutputMode;
import com.kinstalk.voip.sdk.EngineSdkCallConfiguration;
import com.kinstalk.voip.sdk.EngineSdkCallInformation;
import com.kinstalk.voip.sdk.EngineSdkCallState;
import com.kinstalk.voip.sdk.EngineSdkEngineAudioControlParameter;
import com.kinstalk.voip.sdk.EngineSdkEngineCallback;
import com.kinstalk.voip.sdk.EngineSdkEngineConfiguration;
import com.kinstalk.voip.sdk.EngineSdkEngineInformation;
import com.kinstalk.voip.sdk.EngineSdkEngineWorkingMode;
import com.kinstalk.voip.sdk.EngineSdkMsgSender;
import com.kinstalk.voip.sdk.EngineSdkNetworkType;
import com.kinstalk.voip.sdk.logic.sip.aidl.ISipServiceListener;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.AccountInfo;
import com.kinstalk.voip.sdk.logic.sip.delegate.CallDataListener;
import com.kinstalk.voip.sdk.logic.sip.delegate.CallDataReceiver;
import com.kinstalk.voip.sdk.logic.sip.delegate.EngineListener;
import com.kinstalk.voip.sdk.common.Log;

public class EngineLoader
{
	public static final String TAG = "EngineLoader";
	private LinkedList<WeakReference<EngineListener>> mEngineListeners = new LinkedList<WeakReference<EngineListener>>();
	private EngineSdkEngineInformation mCurrentEngineInfo;
	private EngineSdkAccountInformation mCurrentAccountInfo;
	private com.kinstalk.voip.sdk.logic.sip.aidl.model.AccountInfo mAccountInfo = null;
	private EngineSdkCallInformation mCurCallinfo;
	private static boolean gSoLoaded = false;
	private static boolean gInitialed = false;
	private EngineSdkEngineCallback mCallback;
	private CallDataReceiver mReceiver = null;// 临时方案
	private EngineSdkAccountCallback mAccountCallback;// 临时方案
	private EngineSdkNetworkType mLastNetworkType = EngineSdkNetworkType.ES_NETWORK_TYPE_UNKNOWN;
	private PowerManager.WakeLock mCpuWakeLock, mScreenWakeLock;
	private Object mCallInfoLock = new Object();

	// 为了让InCallActivity能够方便的去到EngineLoader的实例，暂时做成单例模式，以后可以考虑如何完善
	// EngineLoader实例的生命周期最好由Service来控制，因此Activity最好应该向Service获取EngineLoader的实例
	public static EngineLoader gInstance = null;

	// 为了实现让activity调用SipService的方法而增加的接口，感觉不适很合理
	private WeakReference<SipService> mService = null;

	public static synchronized final EngineLoader getInstance()
	{
		if (gInstance == null)
		{
			gInstance = new EngineLoader();
		}

		return gInstance;
	}

	private EngineLoader()
	{
		System.loadLibrary("videocodecload");
		System.loadLibrary("mediabase");
		System.loadLibrary("audioengine");
		System.loadLibrary("videoengine");
		System.loadLibrary("voipsdk");
		System.loadLibrary("voipsdkjni");

		gSoLoaded = true;
		 Log.d(TAG, "Libraries Loaded!");

		mReceiver = new CallDataReceiver();
		mCurrentAccountInfo = new EngineSdkAccountInformation();
		mAccountInfo = new AccountInfo(mCurrentAccountInfo);
		mAccountCallback = new EngineSdkAccountCallback()
		{

			@Override
			public void onInstanceMessageReceived(EngineSdkAccountInformation account, int im_id, String im_id_str, String im_time_str, String remote_number, String im_mime_type, String im_msg,
					EngineSdkMsgSender sender)
			{
				 Log.d(TAG, "Callback[" + this.toString() + "] received a message! sender:" + remote_number + "; mimeType: " + im_mime_type + "content:" + im_msg);
				super.onInstanceMessageReceived(account, im_id, im_id_str, im_time_str, remote_number, im_mime_type, im_msg, sender);
				synchronized (mEngineListeners)
				{
					Iterator<WeakReference<EngineListener>> i = mEngineListeners.iterator();
					WeakReference<EngineListener> cur = null;
					while (i.hasNext())
					{
						cur = i.next();
						if (cur.get() == null)
						{
							i.remove();
						}
						else
						{
							cur.get().onMessage(im_id, im_id_str, im_time_str, remote_number, im_msg, im_mime_type, sender);
						}
					}
				}
			}

			@Override
			public void onInstanceMessageDeliveredResult(EngineSdkAccountInformation account, int im_id, String im_id_str, String im_time_str, String remote_number, String im_mime_type,
					String im_msg, boolean is_success, String reason, EngineSdkMsgSender sender)
			{
				 Log.d(TAG, "sending message result: to:" + remote_number + "; content:" + im_msg + "; is_success:" + is_success + "; fail-reason:" + reason);
				super.onInstanceMessageDeliveredResult(account, im_id, im_id_str, im_time_str, remote_number, im_mime_type, im_msg, is_success, reason, sender);
				synchronized (mEngineListeners)
				{
					Iterator<WeakReference<EngineListener>> i = mEngineListeners.iterator();
					WeakReference<EngineListener> cur = null;
					while (i.hasNext())
					{
						cur = i.next();
						if (cur.get() == null)
						{
							i.remove();
						}
						else
						{
							cur.get().onMessageSentResult(im_id, im_id_str, im_time_str, remote_number, is_success, im_msg, im_mime_type, reason, sender);
						}
					}
				}
			}
		};

		mCallback = new EngineSdkEngineCallback()
		{
			@Override
			public void onEngineStateChanged(EngineSdkEngineInformation engineInfo)
			{
				 Log.d(TAG, "a engine state change notified! state:" + engineInfo.getEngineState());
				super.onEngineStateChanged(engineInfo);

				synchronized (mEngineListeners)
				{
					engineInfo = engineInfo.clone();
					engineInfo.swigTakeOwnership();
					mCurrentEngineInfo = engineInfo;
					WeakReference<EngineListener> cur = null;
					Iterator<WeakReference<EngineListener>> i = mEngineListeners.iterator();
					while (i.hasNext())
					{
						cur = i.next();
						if (cur.get() == null)
						{
							i.remove();
						}
						else
						{
							cur.get().onEngineStateChange(engineInfo);
						}
					}
				}
			}

			@Override
			public void onCallStateChanged(EngineSdkCallInformation callInfo)
			{
				super.onCallStateChanged(callInfo);
				 Log.d(TAG, "a call state change notified! id:" + callInfo.getCallConfiguration().getCallLocalToken() + "; state:" + callInfo.getCallState());
				if (callInfo.getCallState() == EngineSdkCallState.ES_STATE_ON_CALL_CALL_ENDED)
				{
					 Log.d(TAG, "Call End Reason:" + callInfo.getCallEndReason());
					mScreenWakeLock.release();
				}
				else
				{
					mScreenWakeLock.acquire();

					if (callInfo.getCallState() == EngineSdkCallState.ES_STATE_ON_CALL_IN_ACTIVE_SESSION)
					// 兼容TV旧版的代码，TV旧版依赖301INFO识别客户端类型，但是此规则已经在现在的SDK中移除
					{
						callInfo.sendInfo("{\"type\":301,\"info\":{\"devicetype\":3,\"camera\":1,\"facing\":1,\"rotate\":1}}");
					}
				}

				callInfo = callInfo.clone();
				callInfo.swigTakeOwnership();
				synchronized (mCallInfoLock)
				{
					mCurCallinfo = callInfo;
				}

				synchronized (mEngineListeners)
				{
					Iterator<WeakReference<EngineListener>> i = mEngineListeners.iterator();
					WeakReference<EngineListener> cur = null;
					while (i.hasNext())
					{
						cur = i.next();
						if (cur.get() == null)
						{
							i.remove();
						}
						else
						{

							cur.get().onCallStateChange(callInfo, callInfo.getCallState());
						}
					}
				}
			}

			@Override
			public void onAccountStateChanged(EngineSdkAccountInformation accountInfo)
			{
				super.onAccountStateChanged(accountInfo);
				 Log.d(TAG, "AccountStateChange: id:" + accountInfo.getAccountConfiguration().getLocalAccountId() + "; state:" + accountInfo.getAccountState());
				if (accountInfo.getAccountState() == EngineSdkAccountState.ES_STATE_ON_ACC_UNREGISTERED)
				{
					 Log.d(TAG, "UNREG_REASON: " + accountInfo.getAccountUnregisterReason());
				}
				synchronized (mEngineListeners)
				{
					accountInfo = accountInfo.clone();
					accountInfo.swigTakeOwnership();

					synchronized (mCurrentAccountInfo)
					{
						mCurrentAccountInfo.setAccountState(accountInfo.getAccountState());
						mCurrentAccountInfo.setAccountConfiguration(accountInfo.getAccountConfiguration());
						mAccountInfo = new AccountInfo(mCurrentAccountInfo);
					}

					assert mCpuWakeLock != null;

					if (accountInfo.getAccountState() == EngineSdkAccountState.ES_STATE_ON_ACC_REGISTERING)
					{
						 Log.d(TAG, "~~~~~~~~~~>>>>>>>>>Required Wakeloack");
						mCpuWakeLock.acquire(60000);
					}
					else if (mCpuWakeLock.isHeld() && accountInfo.getAccountState() == EngineSdkAccountState.ES_STATE_ON_ACC_REGISTERED)
					{
						 Log.d(TAG, "~~~~~~~~~~<<<<<<<<<Released Wakeloack");
						mCpuWakeLock.release();
					}

					Iterator<WeakReference<EngineListener>> i = mEngineListeners.iterator();
					WeakReference<EngineListener> cur = null;
					while (i.hasNext())
					{
						cur = i.next();
						if (cur.get() == null)
						{
							i.remove();
						}
						else
						{

							cur.get().onAccountStateChange(accountInfo, accountInfo.getAccountState());
						}
					}
				}
			}

		};
	}

	public void init(EngineSdkEngineConfiguration ec, Context context, EngineListener listener)
	{
		if(gInitialed) {
			return;
		}
		Log.d(TAG, "start initializeEngine: gInitialed = " + gInitialed);
		PowerManager powerManager = (PowerManager) (context.getSystemService(Context.POWER_SERVICE));
		mCpuWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Weaver EngineSDK");
		mCpuWakeLock.setReferenceCounted(false);// TODO true还是false需要再考虑

		mScreenWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "Weaver EngineSDK");
		mScreenWakeLock.setReferenceCounted(false);// TODO true还是false需要再考虑

		addEngineListener(listener);
		ec.setEngineCallback(mCallback);
		ec.setDeviceCurrentNetworkType(mLastNetworkType);
		EngineSdk.initializeEngine(ec, context);
		gInitialed = true;
		Log.d(TAG, "end initializeEngine: gInitialed = " + gInitialed);
	}

	public void setUserAccount(String domain, String id, String password)
	{
		 Log.d(TAG, "API_AccountSet started!");
		EngineSdkAccountConfiguration acount_conf = new EngineSdkAccountConfiguration();
		acount_conf.setLocalAccountId(id);
		acount_conf.setApplicationDomain(domain);
		acount_conf.setMAccountCallback(mAccountCallback);
		acount_conf.setRegisterReportTimeoutMs(20000);

		synchronized (mCurrentAccountInfo)
		{
			// 此处增加同步锁，防止潜在的并发将mCurrentAccountInfo意外置空
			mCurrentAccountInfo.setAccountConfiguration(acount_conf);
			EngineSdk.addOrUpdateAccount(acount_conf);
		}

		 Log.d(TAG, "API_AccountSet finished");
		 Log.d(TAG, "mCurrentAccountInfo == " + (mCurrentAccountInfo == null ? "null" : mCurrentAccountInfo.toString()));
	}

	public EngineSdkCallInformation makeCall_ex(String to, boolean isRandomcall, boolean isAudioOn, boolean isMainVideoOn, boolean is2ndVideoOn, boolean isDataOn, CallDataListener receiver)
	{
		EngineSdkCallConfiguration p_call_conf = new EngineSdkCallConfiguration();
		p_call_conf.setRemoteAccountId(to);
		p_call_conf.setMainVideoChannelEnabled(isMainVideoOn);
		p_call_conf.setAudioChannelEnabled(isAudioOn);
		p_call_conf.setSecondVideoChannelEnabled(is2ndVideoOn);
		p_call_conf.setDataVideoChannelEnabled(isDataOn);
		p_call_conf.setIsRandomCall(isRandomcall);

		return makeCall(p_call_conf, receiver);
	}

	public EngineSdkCallInformation makeCall(EngineSdkCallConfiguration conf, CallDataListener receiver)
	{
		 Log.d(TAG, "EngineLoader starting call: " + conf.getCallLocalToken() + " with IsRandomCall = " + conf.getIsRandomCall());
		EngineSdkCallInformation call = getCurrentCallInfo();
		if (call != null)
		{
			if (call.getCallState() != EngineSdkCallState.ES_STATE_ON_CALL_CALL_ENDING && call.getCallState() != EngineSdkCallState.ES_STATE_ON_CALL_CALL_ENDED
					&& call.getCallState() != EngineSdkCallState.ES_STATE_ON_CALL_REQUEST_CALLING_OUT)
			{
				 Log.d(TAG, "Last call is still in session, please try later! " + call.getCallConfiguration().getCallLocalToken() + "|" + call.getCallState());
				return null;
			}
		}

		if (receiver != null)
		{
			mReceiver.setListener(receiver);
		}

		conf.setMediaAudioCallback(mReceiver.getAudio());
		conf.setDataCallback(mReceiver.getData());
		conf.setMainVideoCallback(mReceiver.getMainVideo());
		conf.setSecondVideoCallback(mReceiver.getSecondVideo());
		conf.setCallCallback(mReceiver.getCall());

		synchronized (mCallInfoLock)
		{
			// clone为了防止底层将此对象释放掉
			mCurCallinfo = EngineSdk.startCall(conf).clone();
			mCurCallinfo.swigTakeOwnership();
		}
		
		return mCurCallinfo;
	}

	public synchronized void answerCall(EngineSdkCallInformation call, CallDataListener receiver)
	{
		 Log.d(TAG, "Answer Call~" + call.getCallConfiguration().getCallLocalToken());
		if (call != null)
		{
			EngineSdkCallConfiguration callConf = call.getCallConfiguration();
			if (receiver != null)
			{
				mReceiver.setListener(receiver);
			}
			
			callConf.setMediaAudioCallback(mReceiver.getAudio());
			callConf.setDataCallback(mReceiver.getData());
			callConf.setMainVideoCallback(mReceiver.getMainVideo());
			callConf.setSecondVideoCallback(mReceiver.getSecondVideo());
			callConf.setCallCallback(mReceiver.getCall());
			EngineSdk.answerIncomingCall(callConf);
		}
	}

	public void endCall(EngineSdkCallInformation info, String reasonText)
	{
		if (info != null)
		{
			EngineSdk.endCall(info.getCallConfiguration().getCallLocalToken(), reasonText);
			 Log.d(TAG, "End call by user~" + info.getCallConfiguration().getCallLocalToken());
		}
		else
		{
			 Log.d(TAG, "ERROR: null CallInfo！");
		}
	}

	public int sendMessage(String to, String message, String mimeType, EngineSdkMsgSender senderModule)
	{
		if (mCurrentAccountInfo != null)
		{
			return mCurrentAccountInfo.sendInstanceMessage(to, mimeType, message, senderModule);
		}
		else
		{
			 Log.d(TAG, "Can not send a MESSAGE w/out a valid accout!");
			return -1; // -1表示发送失败。正数表示msg_id。
			// throw new IllegalArgumentException("Can not send a MESSAGE w/out a valid accout!");
		}
	}

	public void setDown2Audio(boolean pause) {
		Log.d(TAG, "setTransmitingVideoPaused");
		if(mCurCallinfo != null) {
			Log.d(TAG, "call  setTransmitingVideoPaused");
			mCurCallinfo.setDown2Audio(pause);
		}
	}
	public void setTransmitingVideoPaused(boolean pause) {
		Log.d(TAG, "setTransmitingVideoPaused");
		if(mCurCallinfo != null) {
			Log.d(TAG, "call  setTransmitingVideoPaused");
			mCurCallinfo.setTransmitingVideoPaused(pause);
		}
	}
	public void requestRemotePauseTransmitingVideo(boolean pause, boolean allowResetByPeer) {
		Log.d(TAG, "requestRemotePauseTransmitingVideo");
		if(mCurCallinfo != null) {
			Log.d(TAG, "call requestRemotePauseTransmitingVideo");
			mCurCallinfo.requestRemotePauseTransmitingVideo(pause, allowResetByPeer);
		}
	}
	public void deleteAccount()
	{
		synchronized (mCurrentAccountInfo)
		{
			EngineSdkAccountConfiguration conf = mCurrentAccountInfo.getAccountConfiguration();
			if (conf != null)
			{
				EngineSdk.deleteAccount(conf);
				 Log.d(TAG, "Account Deleted");
			}
			else
			{
				 Log.d(TAG, "Delete Account Error:Account is null!");
			}
		}

	}
	
	public CallDataReceiver getDataReceiver()
	{
		return mReceiver;
	}

	public EngineSdkCallInformation getCurrentCallInfo()
	{
		return mCurCallinfo;
	}

	@Deprecated
	public EngineSdkAccountInformation getCurrentAccountInfo()
	{
		return mCurrentAccountInfo;
	}
	
	public AccountInfo getAccountInfo()
	{
	    return mAccountInfo;
	}

	public void addEngineListener(EngineListener e)
	{
		if (e == null)
		{
			return;
		}

		synchronized (mEngineListeners)
		{
			if (checkListener(e, false) == null)
			{
				if (mCurrentEngineInfo != null)
				{
					e.onEngineStateChange(mCurrentEngineInfo);
				}
				mEngineListeners.add(new WeakReference<EngineListener>(e));
			}
		}
	}

	public EngineListener removeEngineListener(EngineListener e)
	{
		if (e == null)
		{
			return null;
		}

		return checkListener(e, true);
	}

	private EngineListener checkListener(EngineListener e, boolean needRemove)
	{
		if (e == null)
		{
			return null;
		}

		synchronized (mEngineListeners)
		{
			WeakReference<EngineListener> cur = null;
			Iterator<WeakReference<EngineListener>> i = mEngineListeners.iterator();
			while (i.hasNext())
			{
				cur = i.next();
				if (cur.get() == null)
				{
					i.remove();
				}
				else
				{
					if (cur.get().equals(e))
					{
						if (needRemove)
						{
							i.remove();
						}
						return cur.get();
					}
				}
			}

			return null;
		}
	}

	public static boolean isEngineSoLoaded()
	{
		return gSoLoaded;
	}

	public void setFileSystemMounted(boolean isMounted)
	{
		if (gInitialed)
		{
			EngineSdk.informFileSystemHaveMounted(isMounted);
		}
	}

	public void setNetworkType(EngineSdkNetworkType netType)
	{
		mLastNetworkType = netType;
		if (gInitialed)
		{
			if (netType != null)
			{
				EngineSdk.informSystemNetworkResumed(netType);
			}
			else
			{
				 Log.d(TAG, "Unexpected null netType!");
			}
		}
	}

	public void setEconomicTrafficMode(boolean isEconomic)
	{
		if (gInitialed)
		{
			EngineSdk.setEconomicTrafficMode(isEconomic);
		}
	}

	public void addClientFeature(String featureName)
	{
		EngineSdk.addOneClientSupportFeatureName(featureName);
	}

	public void destroyEngine()
	{
		if (gInitialed)
		{
			EngineSdk.destroyEngine();
			gInitialed = false;
		}
	}

	public void setEngineMode(EngineSdkEngineWorkingMode mode)
	{
		if (gInitialed)
		{
			 Log.d(TAG, "SDK API Called: setEngineMode: " + mode);
			EngineSdk.setEngineWorkingMode(mode);
		}
	}

	public void setAudioControl(int mode, EngineSdkEngineAudioControlParameter reserve_param)
	{
		if (gInitialed)
		{
			 Log.d(TAG, "SDK API Called: setAudioControl: mode=" + mode + ", reserve_param=" + reserve_param);
			EngineSdk.setAudioControl(mode, reserve_param);
		}
	}

	public void setAudioOutputMode(EngineSdkAudioOutputMode audioOutputMode)
	{
		if (gInitialed == true)
		{
			 Log.d(TAG, "SDK API Called: setAudioOutputMode: " + audioOutputMode);
			EngineSdk.setAudioOutputMode(audioOutputMode);
		}
	}

	public void refreshCurrentAccount()
	{
		if (gInitialed)
		{
			 Log.d(TAG, "SDK API Called: refreshCurrentAccount");
			EngineSdk.refreshCurrentAccountRegisterNow();
		}
	}
	
	public void setForcedRelayPath(String relay_path)
	{
		if (gInitialed)
		{
			 Log.d(TAG, "SDK API Called: setForcedRelayPath");
			EngineSdk.SetForcedRelayPath(relay_path);
		}
	}

	public void setAndroidFilePath(String path)
	{
		 Log.d(TAG, "SDK API Called: setAndroidFilePath: " + path);
		EngineSdk.setAndroidFilePath(path);
	}

	public void setAudioProcesingControlMode(int mode)
	{
		if (gInitialed == true)
		{
			 Log.d(TAG, "SDK API Called: setAudioProcesingControlMode: mode=" + mode);
			EngineSdk.setAudioProcessingControl(mode);
		}
	}

	public void setCurrentCallinfo(EngineSdkCallInformation callinfo)
	{
		synchronized (mCallInfoLock)
		{
			if (mCurCallinfo != null)
			{
				if (mCurCallinfo.getCallState() != EngineSdkCallState.ES_STATE_ON_CALL_CALL_ENDED && mCurCallinfo.getCallState() != EngineSdkCallState.ES_STATE_ON_CALL_REQUEST_CALLING_OUT)
				{
					// if cur call is not finished, do not permit to start another call.
					 Log.d(TAG, "Current call is not finished, refused to set call info!");
					return;
				}
			}
			mCurCallinfo = callinfo;
		}
	}

	public ISipServiceListener setRPCListener(ISipServiceListener listener)
	{
//		if (mService != null && mService.get() != null)
//		{
//			try
//			{
//				return mService.get().getBinder().setRPCListener(listener);
//			}
//			catch (RemoteException e)
//			{
//				 Log.d(TAG, "", e);
//			}
//		}

		return null;
	}

	public void setService(SipService service)
	{
		mService = new WeakReference<SipService>(service);
	}
}
