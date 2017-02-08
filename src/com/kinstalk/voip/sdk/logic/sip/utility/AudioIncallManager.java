package com.kinstalk.voip.sdk.logic.sip.utility;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONObject;

import com.kinstalk.voip.sdk.EngineSdkAudioOutputMode;
import com.kinstalk.voip.sdk.logic.sip.service.EngineLoader;
import com.kinstalk.voip.sdk.common.Compatibility;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.UserPreferences;

/**
 * @author tangrongjun 2014-5-8
 */
public class AudioIncallManager
{

	public static final Class<?> logTag = AudioIncallManager.class.getClass();

	private AudioManager mAudioManager;
	private boolean mInCallState = false;
	private boolean mInCallOperate = true;
	private int mCurrentValue = -1;
	private int mConfigureMode = -1;

	private int pre_mode = AudioManager.MODE_NORMAL;
	private boolean pre_On = false;

	private int pre_mode_pause = AudioManager.MODE_NORMAL;
	private boolean pre_On_pause = false;

	private Context mContext;
	private static AudioIncallManager instance = null;

	private boolean fetchrunning = false;
	private boolean fetchSatus = false;
	
	public static int preferredAudioMode = AudioManager.MODE_NORMAL;

	public static int preferredHeadsetMode = AudioManager.MODE_NORMAL;

	public static int preferredBluetoothMode = AudioManager.MODE_NORMAL;

	public static int alternativeAudioMode = AudioManager.MODE_IN_CALL;

	public static int xiaomiEarpieceAudioMode = AudioManager.MODE_IN_COMMUNICATION;

	public static int earpieceAudioMode = AudioManager.MODE_IN_CALL;

	public static int currentAudioMode = preferredAudioMode;

	public static int PREFERRED_AUDIO_STREAM = -1;

	private class ModeConfigure
	{
		public boolean misVoiceCall = false;
		public boolean mdisableMode = false;
		public boolean misReverseOrder = false;
		public boolean misCommunication = false;
		public boolean misEarphoneEnhance = false; // 音频增强到3倍
		public boolean misEarphoneEnhanceEX = false; // 会覆盖misAudioEnhance 多增强一倍
		public boolean misSpeakerEnhance = false; // 外放模式增强音频2倍
		public boolean misSpeakerEnhanceEX = false; // 会覆盖misSpeakerEnhance 多增强一倍
		public boolean misSpeakerSuppression = false; // 外放抑制降低0.1
		public boolean misSpeakerSuppressionBig = false; // 外放抑制降低0.2
		public boolean misSpeakerSuppressionEX = false; // 外放抑制降低0.3
		public boolean misOnlyInCall = false; // 只把模式设置为In_CALL
		public boolean misOnlyNormal = false; // 只把模式设置为Normal
		public boolean misOnlyCommunication = false; // 只把模式设置为Communication
		public boolean misUsingCommunicationInput = false; // 输入设置为voice_communication
		public boolean misDisablePostProcessing = false; // 禁用webrtc的前处理。
		public boolean misDisableAGCPostProcessing = false; // 禁用webrtc的前处理。
		public boolean misDisableAECPostProcessing = false; // 禁用webrtc的前处理。
		public boolean misDisableNSPostProcessing = false; // 禁用webrtc的前处理。
		public boolean misRingtoneUsingRingtoneMode = false; // 铃声使用normal。。
	}

	private ModeConfigure mAudioModeConfigure = new ModeConfigure();

	private int mPreMode = -100;
	private boolean mreInitRecording = true;
	private boolean mreInitPlayback = true;

	public static synchronized AudioIncallManager getInstance(Context context)
	{
		if (instance == null)
		{
			instance = new AudioIncallManager(context);
		}
		return instance;
	}

	private AudioIncallManager(Context context)
	{
		Context appContext = context.getApplicationContext();
		if (appContext != null)
		{
			this.mContext = appContext;
		}
		else
		{
			this.mContext = context;
		}
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		
		if(!fetchSatus) {
			if(!fetchrunning) {
				fetchrunning = true;
				fetchAudioConfig();
			}
		}
		
	}

	private static boolean shouldModifyChannelConfig()
	{
		if (Compatibility.isLenovoK860() && Compatibility.getAndroidSDKVersion() >= 17)
		{
			 Log.d(logTag, "Lenovo K860 and the OS equals or newer than jellybean");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static int getRecordChannelConfig()
	{
		if (AudioIncallManager.shouldModifyChannelConfig())
		{
			return AudioFormat.CHANNEL_IN_MONO;
		}
		else
		{
			return AudioFormat.CHANNEL_CONFIGURATION_MONO;
		}
	}

	public static int getPlaybackChannelConfig()
	{
		if (AudioIncallManager.shouldModifyChannelConfig())
		{
			return AudioFormat.CHANNEL_OUT_MONO;
		}
		else
		{
			return AudioFormat.CHANNEL_CONFIGURATION_MONO;
		}
	}

	public boolean getIsUsingCommunicationInput()
	{
		return mAudioModeConfigure.misUsingCommunicationInput;
	}

	public boolean getIsReInitRecording()
	{
		return mreInitRecording;
	}

	public void setIsReInitRecording(boolean flag)
	{
		mreInitRecording = flag;
	}

	public boolean getIsReInitPlayback()
	{
		return mreInitPlayback;
	}

	public void setIsReInitPlayback(boolean flag)
	{
		mreInitPlayback = flag;
	}

	private void getMode()
	{
		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}
		Log.d(logTag, "Current AudioManager Mode: " + mAudioManager.getMode());
		int mode = UserPreferences.getInt(UserPreferences.Key.AUDIO_CONFIG, -1);
		if (mPreMode != mode)
		{
			mPreMode = mode;
			 Log.d(logTag, "setAudioConfigue getMode: " + mode);
		}
		if(mode <= -1) {
			mode = 8193;
		}
		setAudioConfigue(mode);
	}

	private void fetchAudioConfig() {
		HttpTask task = new HttpTask();
		task.setTaskHandler(new HttpTaskHandler(){
			public void taskSuccessful(String json) {
				Log.e(logTag, "setAudioConfigue fetchAudioConfig: " + json);
				try {
					JSONObject jsonObj = new JSONObject(json);
					String audio_configue = jsonObj.getString("audio_configue");
					Log.e(logTag, "setAudioConfigue fetchAudioConfig mode:" + audio_configue);
					if(audio_configue.compareToIgnoreCase("null") == 0) {
						fetchSatus = true;
						UserPreferences.setInt(UserPreferences.Key.AUDIO_CONFIG, -1);
						getMode();
					} else {
						try {
							int value = Integer.parseInt(audio_configue);
							UserPreferences.setInt(UserPreferences.Key.AUDIO_CONFIG, value);
							fetchSatus = true;
							getMode();
						} catch (Exception e) {
							Log.e(logTag, "setAudioConfigue fetchAudioConfig mode parse faied:" + audio_configue);
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				fetchrunning = false;
			}
			public void taskFailed() {
				fetchrunning = false;
			}
		});
		StringBuilder sb = new StringBuilder();
		sb.append(android.os.Build.BRAND);
		sb.append("_");
		sb.append(android.os.Build.MODEL);
		String model = sb.toString();
		model=model.replaceAll("[' ']+", " ");
		model=model.replaceAll(" ", "_");
		String url = "http://gslb.kinstalk.com:9000/getAudioconfigure/" + model;
		Log.e(logTag, "setAudioConfigue fetchAudioConfig url:" + url);
		task.execute(url);
	}
	
	private void setAudioConfigue(int mode)
	{
		// if (mConfigureMode == mode)
		// return ;
		if(!fetchSatus) {
			if(!fetchrunning) {
				fetchrunning = true;
				fetchAudioConfig();
			}
		}
		mConfigureMode = mode;
		Log.d(logTag, "setAudioConfigue mode " + mode);

		// mConfigureMode = -1;
		if (mConfigureMode < 0)
		{
			//  Log.d(logTag, "setAudioConfigue mode < -1 failed ");
			return;
		}

		mAudioModeConfigure.misVoiceCall = false;
		if ((mConfigureMode & 0x00000001) == 0x00000001)
		{
			mAudioModeConfigure.misVoiceCall = true;
		}
		//  Log.d(logTag, "setAudioConfigue misVoiceCall: " + mAudioModeConfigure.misVoiceCall);

		mAudioModeConfigure.mdisableMode = false;
		if ((mConfigureMode & 0x00000002) == 0x00000002)
		{
			mAudioModeConfigure.mdisableMode = true;
		}
		//  Log.d(logTag, "setAudioConfigue mdisableMode: " + mAudioModeConfigure.mdisableMode);

		mAudioModeConfigure.misReverseOrder = false;
		if ((mConfigureMode & 0x00000004) == 0x00000004)
		{
			mAudioModeConfigure.misReverseOrder = true;
		}
		//  Log.d(logTag, "setAudioConfigue misReverseOrder: " +
		// mAudioModeConfigure.misReverseOrder);

		mAudioModeConfigure.misCommunication = false;
		if ((mConfigureMode & 0x00000008) == 0x00000008)
		{
			mAudioModeConfigure.misCommunication = true;
		}
		//  Log.d(logTag, "setAudioConfigue misCommunication: " +
		// mAudioModeConfigure.misCommunication);

		mAudioModeConfigure.misEarphoneEnhance = false;
		if ((mConfigureMode & 0x00000010) == 0x00000010)
		{
			mAudioModeConfigure.misEarphoneEnhance = true;
		}
		//  Log.d(logTag, "setAudioConfigue misEarphoneEnhance: " +
		// mAudioModeConfigure.misEarphoneEnhance);

		mAudioModeConfigure.misEarphoneEnhanceEX = false;
		if ((mConfigureMode & 0x00000020) == 0x00000020)
		{
			mAudioModeConfigure.misEarphoneEnhanceEX = true;
		}
		//  Log.d(logTag, "setAudioConfigue misEarphoneEnhanceEX: " +
		// mAudioModeConfigure.misEarphoneEnhanceEX);

		mAudioModeConfigure.misSpeakerEnhance = false;
		if ((mConfigureMode & 0x00000040) == 0x00000040)
		{
			mAudioModeConfigure.misSpeakerEnhance = true;
		}
		//  Log.d(logTag, "setAudioConfigue misSpeakerEnhance: " +
		// mAudioModeConfigure.misSpeakerEnhance);

		mAudioModeConfigure.misSpeakerEnhanceEX = false;
		if ((mConfigureMode & 0x00000080) == 0x00000080)
		{
			mAudioModeConfigure.misSpeakerEnhanceEX = true;
		}
		//  Log.d(logTag, "setAudioConfigue misSpeakerEnhanceEX: " +
		// mAudioModeConfigure.misSpeakerEnhanceEX);

		mAudioModeConfigure.misSpeakerSuppression = false;
		if ((mConfigureMode & 0x00000100) == 0x00000100)
		{
			mAudioModeConfigure.misSpeakerSuppression = true;
		}
		//  Log.d(logTag, "setAudioConfigue misSpeakerSuppression: " +
		// mAudioModeConfigure.misSpeakerSuppression);

		mAudioModeConfigure.misSpeakerSuppressionBig = false;
		if ((mConfigureMode & 0x00000200) == 0x00000200)
		{
			mAudioModeConfigure.misSpeakerSuppressionBig = true;
		}
		//  Log.d(logTag, "setAudioConfigue misSpeakerSuppressionBig: " +
		// mAudioModeConfigure.misSpeakerSuppressionBig);

		mAudioModeConfigure.misSpeakerSuppressionEX = false;
		if ((mConfigureMode & 0x00000400) == 0x00000400)
		{
			mAudioModeConfigure.misSpeakerSuppressionEX = true;
		}
		//  Log.d(logTag, "setAudioConfigue misSpeakerSuppressionEX: " +
		// mAudioModeConfigure.misSpeakerSuppressionEX);

		mAudioModeConfigure.misOnlyInCall = false;
		if ((mConfigureMode & 0x00000800) == 0x00000800)
		{
			mAudioModeConfigure.misOnlyInCall = true;
		}
		//  Log.d(logTag, "setAudioConfigue misOnlyInCall: " + mAudioModeConfigure.misOnlyInCall);

		mAudioModeConfigure.misOnlyNormal = false;
		if ((mConfigureMode & 0x00001000) == 0x00001000)
		{
			mAudioModeConfigure.misOnlyNormal = true;
		}
		//  Log.d(logTag, "setAudioConfigue misOnlyNormal: " + mAudioModeConfigure.misOnlyNormal);

		mAudioModeConfigure.misOnlyCommunication = false;
		if ((mConfigureMode & 0x00002000) == 0x00002000)
		{
			mAudioModeConfigure.misOnlyCommunication = true;
		}
		//  Log.d(logTag, "setAudioConfigue misOnlyCommunication: " +
		// mAudioModeConfigure.misOnlyCommunication);

		mAudioModeConfigure.misUsingCommunicationInput = false;
		if ((mConfigureMode & 0x00004000) == 0x00004000)
		{
			mAudioModeConfigure.misUsingCommunicationInput = true;
		}
		//  Log.d(logTag, "setAudioConfigue misUsingCommunicationInput: " +
		// mAudioModeConfigure.misUsingCommunicationInput);

		mAudioModeConfigure.misDisablePostProcessing = false;
		if ((mConfigureMode & 0x00008000) == 0x00008000)
		{
			mAudioModeConfigure.misDisablePostProcessing = true;
		}
		//  Log.d(logTag, "setAudioConfigue misDisablePostProcessing: " +
		// mAudioModeConfigure.misDisablePostProcessing);

		mAudioModeConfigure.misDisableAGCPostProcessing = false;
		if ((mConfigureMode & 0x00010000) == 0x00010000)
		{
			mAudioModeConfigure.misDisableAGCPostProcessing = true;
		}
		//  Log.d(logTag, "setAudioConfigue misDisableAGCPostProcessing: " +
		// mAudioModeConfigure.misDisableAGCPostProcessing);

		mAudioModeConfigure.misDisableAECPostProcessing = false;
		if ((mConfigureMode & 0x00020000) == 0x00020000)
		{
			mAudioModeConfigure.misDisableAECPostProcessing = true;
		}
		//  Log.d(logTag, "setAudioConfigue misDisableAECPostProcessing: " +
		// mAudioModeConfigure.misDisableAECPostProcessing);

		mAudioModeConfigure.misDisableNSPostProcessing = false;
		if ((mConfigureMode & 0x00040000) == 0x00040000)
		{
			mAudioModeConfigure.misDisableNSPostProcessing = true;
		}
		//  Log.d(logTag, "setAudioConfigue misDisableNSPostProcessing: " +
		// mAudioModeConfigure.misDisableNSPostProcessing);

		mAudioModeConfigure.misRingtoneUsingRingtoneMode = false;
		if ((mConfigureMode & 0x00080000) == 0x00080000)
		{
			mAudioModeConfigure.misRingtoneUsingRingtoneMode = true;
		}
		//  Log.d(logTag, "setAudioConfigue misRingtoneUsingRingtoneMode: " +
		// mAudioModeConfigure.misRingtoneUsingRingtoneMode);
	}

	public boolean IsRingtoneUsingRingtoneMode()
	{
		return mAudioModeConfigure.misRingtoneUsingRingtoneMode;
	}

	public int getAudioStream()
	{
		// if (PREFERRED_AUDIO_STREAM != -1) {
		// return PREFERRED_AUDIO_STREAM;
		// }
		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}

		getMode();

		PREFERRED_AUDIO_STREAM = Compatibility.getCurrentCallModeDevice();
		//  Log.d(TAG, "setAudioConfigue getAudioStream: " + mAudioModeConfigure.misVoiceCall);
		if (PREFERRED_AUDIO_STREAM == AudioManager.STREAM_MUSIC && mAudioModeConfigure.misVoiceCall)
		{
			PREFERRED_AUDIO_STREAM = AudioManager.STREAM_VOICE_CALL;
		}
		return PREFERRED_AUDIO_STREAM;
	}

	public void operateRingtone(boolean open)
	{
		 Log.d(logTag, "operateRingtone: " + open);

		getMode();

		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}
		if (open)
		{
			setEarpieceMode(true);
			if (mAudioManager.isSpeakerphoneOn())
			{
				 Log.d(logTag, "operateRingtone No need to open speaker.");
			}
			else
			{
				mAudioManager.setSpeakerphoneOn(true);
			}
		}
		else
		{
			setEarpieceMode(false);
			if (!mAudioManager.isSpeakerphoneOn())
			{
				 Log.d(logTag, "operateRingtone No need to close speaker.");
			}
			else
			{
				mAudioManager.setSpeakerphoneOn(false);
			}
		}

		mAudioManager.setStreamVolume(AudioManager.STREAM_RING, mAudioManager.getStreamVolume(AudioManager.STREAM_RING), 0);

	}

	public void operateMediaPlay(boolean open)
	{
		 Log.d(logTag, "operateMediaPlay: " + open);

		getMode();

		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}
		if (open)
		{
			setEarpieceMode(true);
			if (mAudioManager.isSpeakerphoneOn())
			{
				 Log.d(logTag, "operateRingtone No need to open speaker.");
			}
			else
			{
				mAudioManager.setSpeakerphoneOn(true);
			}
		}
		else
		{
			setEarpieceMode(false);
			if (!mAudioManager.isSpeakerphoneOn())
			{
				 Log.d(logTag, "operateRingtone No need to close speaker.");
			}
			else
			{
				mAudioManager.setSpeakerphoneOn(false);
			}
		}

	}

	/**
	 * 
	 * @param open
	 *            false，处于连接耳塞状态。
	 */
	public void operateConnected(boolean open)
	{
		if (!mInCallState)
		{
			 Log.d(logTag, "operateConnected false, because not inCalling:" + open);
			return;
		}
		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}

		getMode();

		if (!open)
		{
			mAudioManager.setMode(AudioManager.MODE_NORMAL);
		}
		else
		{
			mAudioManager.setMode(AudioManager.MODE_NORMAL);
			// mAudioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, true);
		}
		// if (open) {
		// if (mAudioManager.isSpeakerphoneOn()) {
		//  Log.d(logTag, "No need to open speaker.");
		// } else {
		// mAudioManager.setSpeakerphoneOn(true);
		// }
		// EngineLoader el = EngineLoader.getInstance();
		// el.setAudioControl(0, null);
		// //initSpeakerVolume();
		// //updateSpeakerVolume();
		// initNonSpeakerVolume();
		// } else {
		if (!mAudioManager.isSpeakerphoneOn())
		{
			 Log.d(logTag, "No need to close speaker.");
		}
		else
		{
			mAudioManager.setSpeakerphoneOn(false);
		}
		EngineLoader el = EngineLoader.getInstance();
		el.setAudioOutputMode(EngineSdkAudioOutputMode.ES_AUDIO_OUTPUT_MODE_EARSET);

		// el.setAudioControl(2, null);
		initNonSpeakerVolume();

		// 耳塞或者蓝牙耳塞状态，禁用webrtc的前处理
		el.setAudioProcesingControlMode(0);
		// }
	}

	/**
	 * 
	 * @param open
	 *            true:open speaker, false:close speaker
	 * @param Context
	 *            context
	 * @return true means indeed operate speaker, false means in fact do nothing with speaker.
	 */
	public boolean operateSpeaker(boolean open)
	{

		if (!mInCallState)
		{
			 Log.d(logTag, "operateSpeaker false, because not inCalling:" + open);
			return false;
		}

		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}
		 Log.d(logTag, "operate speaker, open : " + open);

		getMode();

		/*
		 * If wiredheadset or a2dp bluetooth device connected, don't change operate speakerphone and
		 * audio mode. While we have to reset the speakerphone when we exit the call to avoid the
		 * case that the speakphone is closed before wiredheadset connected.
		 */
		if (open)
		{
			this.setEarpieceMode(true);

			 Log.d(logTag, "setSpeakerphoneOn to true in operateSpeaker.");
			if (!mAudioManager.isSpeakerphoneOn())
			{
				mAudioManager.setSpeakerphoneOn(true);
			}

			 Log.d(logTag, "Exit setSpeakerphoneOn to true in operateSpeaker.");
			// MediaManager.restoreSpeakerphoneModeVolume(context);//Make sure
			// recover the speaker phone mode volume last.
			// tang
			EngineLoader el = EngineLoader.getInstance();
			el.setAudioOutputMode(EngineSdkAudioOutputMode.ES_AUDIO_OUTPUT_MODE_LOUD_SPEAKER);

			// el.setAudioControl(0, null);
			initSpeakerVolume();
			updateSpeakerVolume();
			// if(Compatibility.getCurrentCallModeDevice() ==
			// AudioManager.STREAM_MUSIC) {
			// el.setAudioControl(13, null); // scale 0.6
			// }

			// 扬声器模式，根据配置是否禁用webrtc的前处理
			if (mAudioModeConfigure.misDisablePostProcessing)
			{
				el.setAudioProcesingControlMode(0);
			}
			else
			{
				el.setAudioProcesingControlMode(1);
				if (mAudioModeConfigure.misDisableAGCPostProcessing)
				{
					el.setAudioProcesingControlMode(2);
				}

				if (mAudioModeConfigure.misDisableAECPostProcessing)
				{
					el.setAudioProcesingControlMode(3);
				}

				if (mAudioModeConfigure.misDisableNSPostProcessing)
				{
					el.setAudioProcesingControlMode(4);
				}
			}
		}
		else
		{
			this.setEarpieceMode(false);

			 Log.d(logTag, "setSpeakerphoneOn to false in operateSpeaker.");
			if (mAudioManager.isSpeakerphoneOn())
			{
				mAudioManager.setSpeakerphoneOn(false);
			}
			 Log.d(logTag, "Exit setSpeakerphoneOn to false in operateSpeaker.");
			// MediaManager.setEarpieceModeVolume(context);
			// tang
			EngineLoader el = EngineLoader.getInstance();
			el.setAudioOutputMode(EngineSdkAudioOutputMode.ES_AUDIO_OUTPUT_MODE_SMALL_SPEAKER);

			// el.setAudioControl(2, null);
			initNonSpeakerVolume();

			// 听筒模式，禁用webrtc的前处理
			el.setAudioProcesingControlMode(0);
			//
		}
		 Log.d(logTag, "Exit operateSpeaker, open : " + open);
		return true;
	}

	/**
	 * 
	 * @param context
	 * @param isRecover
	 *            false: set to earpiece mode. true:recover to speakerphone mode.
	 */
	private void setEarpieceMode(boolean isRecover)
	{

		if (mAudioModeConfigure.mdisableMode)
		{// 不需要设置mode
			 Log.d(logTag, "setAudioConfigue, Disable Set Mode");
			return;
		}

		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}

		if (mAudioModeConfigure.misOnlyInCall)
		{
			mAudioManager.setMode(AudioManager.MODE_IN_CALL);
			 Log.d(logTag, "setAudioConfigue, Only In Call");
			return;
		}
		if (mAudioModeConfigure.misOnlyNormal)
		{
			mAudioManager.setMode(AudioManager.MODE_NORMAL);
			 Log.d(logTag, "setAudioConfigue, Only In Noraml");
			return;
		}

		if (mAudioModeConfigure.misOnlyCommunication)
		{
			mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
			 Log.d(logTag, "setAudioConfigue, Only In Communicatiion");
			return;
		}

		 Log.d(logTag, "setEarpieceMode, isRecover:" + isRecover);
		if (isRecover)
		{
			 Log.d(logTag, "__Before set mode to : " + AudioManager.MODE_NORMAL + " when switch to speakerphone.");
			String model = android.os.Build.MODEL;
			if (model != null)
			{
				String lowerCaseModel = model.toLowerCase();
				if (lowerCaseModel.contains("sm-n9008v") || lowerCaseModel.contains("sm-n9009") || lowerCaseModel.contains("sch-i939") || mAudioModeConfigure.misReverseOrder)
				{
					int mode = AudioManager.MODE_IN_CALL;
					if (lowerCaseModel.contains("sm-n9008v") || lowerCaseModel.contains("sm-n9009") || lowerCaseModel.contains("sch-i939") || mAudioModeConfigure.misCommunication)
					{
						mode = AudioManager.MODE_IN_COMMUNICATION;
					}
					// if(mAudioManager.getMode() != mode) {
					mAudioManager.setMode(mode);
					// }
				}
				else
				{
					// if(mAudioManager.getMode() != AudioManager.MODE_NORMAL) {
					mAudioManager.setMode(AudioManager.MODE_NORMAL);
					// }
				}
			}
			// mAudioManager.setMode(acquriedInCallMode);
			 Log.d(logTag, "__After set mode to : " + AudioManager.MODE_NORMAL + " when switch to speakerphone.");
		}
		else
		{
			if (Compatibility.getBrand() == Compatibility.BRAND.XIAOMI)
			{
				mAudioManager.setMode(AudioIncallManager.xiaomiEarpieceAudioMode);
			}
			else
			{
				 Log.d(logTag, "Before set mode to : " + AudioIncallManager.earpieceAudioMode + " when switch to earpiece.");
				String model = android.os.Build.MODEL;
				if (model != null)
				{
					String lowerCaseModel = model.toLowerCase();
					if (lowerCaseModel.contains("sm-n9008v") || lowerCaseModel.contains("sm-n9009") || mAudioModeConfigure.misReverseOrder)
					{
						// if(mAudioManager.getMode() != AudioManager.MODE_NORMAL) {
						mAudioManager.setMode(AudioManager.MODE_NORMAL);
						// }
					}
					else
					{
						int mode = AudioManager.MODE_IN_CALL;
						if (mAudioModeConfigure.misCommunication)
						{
							mode = AudioManager.MODE_IN_COMMUNICATION;
						}
						// if(mAudioManager.getMode() != mode) {
						mAudioManager.setMode(mode);
						// }
					}
				}
				 Log.d(logTag, "After set mode to : " + AudioManager.MODE_IN_CALL + " when switch to earpiece.");
			}
		}
	}

	public AudioManager getmAudioManager()
	{
		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}
		return mAudioManager;
	}

	public boolean getInCallState()
	{
		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}
		return mInCallState;
	}

	public void entryInCallState()
	{
		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}

		pre_mode = mAudioManager.getMode();
		pre_On = mAudioManager.isSpeakerphoneOn();

		mInCallState = true;
		mCurrentValue = -1;
	}

	public void leaveInCallState()
	{
		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}

		mAudioManager.setMode(pre_mode);
		mAudioManager.setSpeakerphoneOn(pre_On);

		mInCallState = false;
		mCurrentValue = -1;
	}

	public boolean getInCallOperate()
	{
		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}
		return mInCallOperate;
	}

	public void puaseInCallOperate()
	{
		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}
		mInCallOperate = false;

		pre_mode_pause = mAudioManager.getMode();
		pre_On_pause = mAudioManager.isSpeakerphoneOn();
		// mCurrentValue = mAudioManager.getStreamVolume(this.getAudioStream());
	}

	public void resumeInCallOperate()
	{
		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}
		mInCallOperate = true;

		mAudioManager.setMode(pre_mode_pause);
		mAudioManager.setSpeakerphoneOn(pre_On_pause);

		// if (mCurrentValue != -1 && mInCallState)
		// {
		// mAudioManager.setStreamVolume(this.getAudioStream(), mCurrentValue, 0);
		// updateSpeakerVolume();
		// }
	}

	private void initSpeakerVolume()
	{
		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}

		mAudioManager.setStreamVolume(getAudioStream(), mAudioManager.getStreamMaxVolume(getAudioStream()) * 4 / 5, 0);

		// 外放增强
		if (mAudioModeConfigure.misSpeakerEnhance || mAudioModeConfigure.misSpeakerEnhanceEX)
		{

			EngineLoader el = EngineLoader.getInstance();
			int scale = 1;
			if (mAudioModeConfigure.misSpeakerEnhanceEX)
			{
				scale = 2;
			}
			el.setAudioControl(scale, null);
		}
		else
		{
			EngineLoader el = EngineLoader.getInstance();
			el.setAudioControl(0, null);
		}

		// 外放抑制
		// if (getAudioStream() == AudioManager.STREAM_MUSIC && mInCallState &&
		// mAudioManager.isSpeakerphoneOn()) {
		if ((mAudioModeConfigure.misSpeakerSuppression || mAudioModeConfigure.misSpeakerSuppressionBig || mAudioModeConfigure.misSpeakerSuppressionEX) && mInCallState
				&& mAudioManager.isSpeakerphoneOn())
		{
			mAudioManager.setStreamVolume(getAudioStream(), mAudioManager.getStreamMaxVolume(getAudioStream()) * 3 / 5, 0);
			int add = 1;
			if (mAudioModeConfigure.misSpeakerSuppressionBig)
			{
				add = 2;
			}
			if (mAudioModeConfigure.misSpeakerSuppressionEX)
			{
				add = 3;
			}
			EngineLoader el = EngineLoader.getInstance();
			el.setAudioControl(11 + add, null);
		}
	}

	public void updateSpeakerVolume()
	{
		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}
		/*
		 * getMode(); //外放抑制 //if (getAudioStream() == AudioManager.STREAM_MUSIC && mInCallState &&
		 * mAudioManager.isSpeakerphoneOn()) { if ((mAudioModeConfigure.misSpeakerSuppression ||
		 * mAudioModeConfigure.misSpeakerSuppressionBig ||
		 * mAudioModeConfigure.misSpeakerSuppressionEX ) && mInCallState &&
		 * mAudioManager.isSpeakerphoneOn()) { int max =
		 * mAudioManager.getStreamMaxVolume(getAudioStream()); int current =
		 * mAudioManager.getStreamVolume(getAudioStream()); int add = 1;
		 * if(mAudioModeConfigure.misSpeakerSuppressionBig) { add = 2; }
		 * if(mAudioModeConfigure.misSpeakerSuppressionEX) { add = 3; } if (current < (max * 3 / 5))
		 * { EngineLoader el = EngineLoader.getInstance(); el.setAudioControl(0, null); } else if
		 * (current < (max * 4 / 5)) { EngineLoader el = EngineLoader.getInstance();
		 * el.setAudioControl(11 + add, null); } else if (current < (max * 5 / 5)) { EngineLoader el
		 * = EngineLoader.getInstance(); el.setAudioControl(12 + add, null); } else if (current ==
		 * max) { EngineLoader el = EngineLoader.getInstance(); el.setAudioControl(13 + add, null);
		 * } }
		 */
	}

	private void initNonSpeakerVolume()
	{
		if (mAudioManager == null)
		{
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		}

		mAudioManager.setStreamVolume(getAudioStream(), mAudioManager.getStreamMaxVolume(getAudioStream()), 0);
		// if (getAudioStream() == AudioManager.STREAM_MUSIC && mInCallState) {

		if (mAudioModeConfigure.misEarphoneEnhance || mAudioModeConfigure.misEarphoneEnhanceEX)
		{
			EngineLoader el = EngineLoader.getInstance();
			int scale = 1;
			if (mAudioModeConfigure.misEarphoneEnhanceEX)
			{
				scale = 2;
			}
			el.setAudioControl(scale, null);
		}
		else
		{
			EngineLoader el = EngineLoader.getInstance();
			el.setAudioControl(0, null);
		}
	}

	public int HardwareError(int type)
	{
		if (type == 0)
		{
			;
		}
		return 0;
	}

	public class HttpTask extends AsyncTask<String, Integer, String> {
	    private static final String TAG = "HTTP_TASK";

	    @Override
	    protected String doInBackground(String... params) {
	        // Performed on Background Thread
	        String url = params[0];
	        try {
	            String json = new NetworkTool().getContentFromUrl(url);
	            return json;
	        } catch (Exception e) {
	            // TODO handle different exception cases
	            Log.e(TAG, e.toString());
	            e.printStackTrace();
	            return null;
	        }
	    }

	    @Override
	    protected void onPostExecute(String json) {
	        // Done on UI Thread
	        if (json != null && json != "") {
	            Log.e(TAG, "taskSuccessful");
	            int i1 = json.indexOf("["), i2 = json.indexOf("{"), i = i1 > -1
	                    && i1 < i2 ? i1 : i2;
	            if (i > -1) {
	                json = json.substring(i);
	                taskHandler.taskSuccessful(json);
	            } else {
	                Log.e(TAG, "taskFailed");
	                taskHandler.taskFailed();
	            }
	        } else {
	            Log.e(TAG, "taskFailed");
	            taskHandler.taskFailed();
	        }
	    }
	    HttpTaskHandler taskHandler;

	    public void setTaskHandler(HttpTaskHandler taskHandler) {
	        this.taskHandler = taskHandler;
	    }

	}
	
	public static interface HttpTaskHandler {
        void taskSuccessful(String json);

        void taskFailed();
    }
	
	public class NetworkTool {
	    public String getContentFromUrl(String url) {
	        StringBuilder sb = new StringBuilder();
	        try {
	            InputStream is = new URL(url).openStream();
	            InputStreamReader isr = new InputStreamReader(is, "utf-8");
	            BufferedReader br = new BufferedReader(isr);
	            String line = null;
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
	            is.close();
	        } catch (final IOException e) {
	            return null;
	        }
	        return sb.toString();
	    }
	}
	
}
