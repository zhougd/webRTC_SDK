package com.kinstalk.voip.sdk.logic.sip.utility;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import com.kinstalk.voip.sdk.common.Log;

public class MediaManager implements OnCompletionListener
{
	private static MediaManager gInstance;
	private static final String TAG = "MediaManager";
	
	public static final class RingtoneType
	{
		public static final int INCOMING = 0;
		public static final int OUTGOING = 1;
	}

	public static final class SpeakerMode
	{
		public static final int EAR_SPEAKER = 0;
		public static final int BACK_SPEAKER = 1;
		public static final int EARPHONE = 2;
		public static final int BLUETOOTH = 3;
	}

	public static final class InCallMode
	{
		public static final int AUDIO_ONLY = 0;
		public static final int AUDIO_VIDEO = 1;
	}

	private AudioManager mAudioManager;
	private static Context gContext;
	private static AssetFileDescriptor gLocalRingtoneFD;
	private MediaPlayer mPlayer = new MediaPlayer();
	private Vibrator mVibrator;
	private Uri mDefaultRingtoneUri;

	private boolean mIsVibratorOn = true;
	private boolean mIsMute = false;

	private int pre_mode = AudioManager.MODE_NORMAL;
	private boolean pre_On = false;

	private int mDefaultAudioMode = SpeakerMode.EAR_SPEAKER;

	private int mAudioMode = SpeakerMode.EAR_SPEAKER;

	private boolean mIsEarphoneConnected = false;
	private boolean mIsHandsFreeOn = false;
	private boolean mIsBluetoothConnected = false;

	private MediaManager()
	{
		mVibrator = (Vibrator) gContext.getSystemService(Context.VIBRATOR_SERVICE);
		mDefaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		mAudioManager = (AudioManager) gContext.getSystemService(Context.AUDIO_SERVICE);
		mPlayer.setOnCompletionListener(this);
	}

	public static final void init(Context context, AssetFileDescriptor localRingtoneFile)
	{
		gContext = context;
		gInstance = new MediaManager();
		gLocalRingtoneFD = localRingtoneFile;
		 Log.d(MediaManager.class.getClass(), "--------------11111---" + localRingtoneFile.toString());
	}

	public boolean isWiredHeadsetConnected()
	{
		return mAudioManager.isWiredHeadsetOn();
	}

	public static MediaManager getInstance()
	{
		return gInstance;
	}

	public synchronized void playNotification()
	{

	}

	public synchronized void playAudioFile(String filepath)
	{

	}

	public synchronized void playRingtone(int ringtoneType)
	{
		 Log.d(getClass(), "------------------start playing...");

		AudioManager pAudioManager = AudioIncallManager.getInstance(gContext).getmAudioManager();
		pre_mode = pAudioManager.getMode();
		pre_On = pAudioManager.isSpeakerphoneOn();

		if (!mIsMute)
		{
			try
			{
				if (ringtoneType == RingtoneType.INCOMING)
				{
					mPlayer.setDataSource(gContext, mDefaultRingtoneUri);
				}
				else
				{
					if (gLocalRingtoneFD == null)
					{
						 Log.d(getClass(), "gLocalRingtoneFD failed!");
						return;
					}
					else
					{
						 Log.d(getClass(), "------------22-11111---" + gLocalRingtoneFD.toString());
					}
					mPlayer.setDataSource(gLocalRingtoneFD.getFileDescriptor(), gLocalRingtoneFD.getStartOffset(), gLocalRingtoneFD.getLength());

					// mPlayer.setDataSource(gContext, mDefaultRingtoneUri);
				}
				mPlayer.setAudioStreamType(AudioManager.STREAM_RING);
				// mPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
				mPlayer.setLooping(true);
				mPlayer.prepare();
				mPlayer.start();

				// trj, 铃声应该不能被耳塞禁用掉。
				pAudioManager.setMode(AudioManager.MODE_NORMAL);
				 Log.d(getClass(), "set ringtong mode");

				if (AudioIncallManager.getInstance(gContext).IsRingtoneUsingRingtoneMode())
				{
					pAudioManager.setMode(AudioManager.MODE_RINGTONE);
					 Log.d(getClass(), "set ringtong mode ex");
				}
				/*
				 * if (!isWiredHeadsetConnected()) {
				 * AudioIncallManager.getInstance(gContext).operateRingtone(true); } else {
				 * AudioIncallManager.getInstance(gContext).operateRingtone(false); }
				 */
			}
			catch (Exception e)
			{
				 Log.d(getClass(), "playRingtone failed!");
			}

		}
	}

	public synchronized void stopPlaying()
	{
		 Log.d(getClass(), "------------------stop playing...");

		AudioManager pAudioManager = AudioIncallManager.getInstance(gContext).getmAudioManager();
		pAudioManager.setMode(pre_mode);
		pAudioManager.setSpeakerphoneOn(pre_On);

		if (mPlayer.isPlaying())
		{
			mPlayer.stop();
			mPlayer.reset();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp)
	{

	}

	/**
	 * 设置通话模式 mode = 0 ， 语音通话 mode = 1， 视频通话
	 * 
	 */
	public void setInCallingMode(int mode)
	{
		if (mode == InCallMode.AUDIO_ONLY)
		{
			mDefaultAudioMode = SpeakerMode.EAR_SPEAKER;
		}

		if (mode == InCallMode.AUDIO_VIDEO)
		{
			mDefaultAudioMode = SpeakerMode.BACK_SPEAKER;
		}
	}

	/**
	 * 根据各种外部情况来选择最终的音频模式
	 * 
	 */
	private synchronized void updateAudioMode()
	{
		Log.d(TAG, "20151216 updateAudioMode into");
		int newMode = mDefaultAudioMode;
		if (mIsBluetoothConnected)
		{
			Log.d(TAG, "20151216 SpeakerMode.BACK_SPEAKER");
			newMode = SpeakerMode.BACK_SPEAKER; // SpeakerMode.BLUETOOTH;
		}
		else
		{
			if (mIsEarphoneConnected)
			{
				newMode = SpeakerMode.EARPHONE;
			}
			else
			{
				if (mIsHandsFreeOn)
				{
					newMode = SpeakerMode.BACK_SPEAKER;
				}
				else
				{
					newMode = SpeakerMode.EAR_SPEAKER;
				}
			}
		}

		// if (newMode != mAudioMode)
		{
			mAudioMode = newMode;
			operateSystemAudio();
			Log.d(TAG, "20151216 audio mode %d" +  mAudioMode);
		}
	}

	/**
	 * 返回当前音频的模式
	 */
	public int getCurrentAudioMode()
	{
		return mAudioMode;
	}

	/**
	 * 根据当前的音频模式调用系统音频接口，使其按照不同的模式来工作
	 */
	public void operateSystemAudio()
	{
		 Log.d(getClass(), "Call operateSystemAudio: " + mAudioMode);
		switch (mAudioMode)
		{
			case SpeakerMode.EAR_SPEAKER:
				 Log.d(getClass(), "Call operateSpeaker(false).");
				AudioIncallManager.getInstance(gContext).operateSpeaker(false);
				break;
			case SpeakerMode.BACK_SPEAKER:
				 Log.d(getClass(), "Call operateSpeaker(true).");
				AudioIncallManager.getInstance(gContext).operateSpeaker(true);
				break;
			case SpeakerMode.EARPHONE:
				 Log.d(getClass(), "Call operateConnected(false).");
				AudioIncallManager.getInstance(gContext).operateConnected(false);
				break;
			case SpeakerMode.BLUETOOTH:// 暂时将蓝牙和耳机做相同处理
				 Log.d(getClass(), "Call operateConnected(true).");
				AudioIncallManager.getInstance(gContext).operateConnected(false);
				break;
		}
	}

	/**
	 * 设置当前耳机状态
	 * 
	 * @param isEarphoneConnected
	 *            耳机是否已插入
	 */
	public void setEarphoneConnected(boolean isEarphoneConnected)
	{
		mIsEarphoneConnected = isEarphoneConnected;
		updateAudioMode();
	}

	/**
	 * 设置当前免提（外放扬声器）状态
	 * 
	 * @param isHandfreeOn
	 *            用户是否打开免提
	 */
	public void setHandfree(boolean isHandfreeOn)
	{
		mIsHandsFreeOn = isHandfreeOn;
		updateAudioMode();
	}

	/**
	 * 
	 * 设置当前蓝牙音频设备状态
	 * 
	 * @param isBluetoothConnected
	 *            蓝牙音频设备是否连接
	 */
	public void setBluetoothConnected(boolean isBluetoothConnected)
	{
		mIsBluetoothConnected = isBluetoothConnected;
		updateAudioMode();
		if (mIsBluetoothConnected)
		{
			// mAudioManager.set(true);
		}
		else
		{
			// mAudioManager.setBluetoothScoOn(false);
		}
	}

}
