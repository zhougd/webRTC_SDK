/*
 * Copyright (c) 2012 The WebRTC project authors. All Rights Reserved.
 * 
 * Use of this source code is governed by a BSD-style license that can be found in the LICENSE file
 * in the root of the source tree. An additional intellectual property rights grant can be found in
 * the file PATENTS. All contributing project authors may be found in the AUTHORS file in the root
 * of the source tree.
 */

package org.webrtc.voiceengine;

import java.nio.ByteBuffer;
import java.util.Arrays; // delia 20131030
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;

import com.kinstalk.voip.sdk.logic.sip.utility.AudioIncallManager;
import com.kinstalk.voip.sdk.common.Log;


public class WebRTCAudioDevice
{ // delia 20131030
	private AudioTrack _audioTrack = null;
	private AudioRecord _audioRecord = null;

	private Context _context;
	private AudioManager _audioManager;

	private ByteBuffer _playBuffer;
	private ByteBuffer _recBuffer;
	private byte[] _tempBufPlay;
	private byte[] _tempBufRec;

	private final ReentrantLock _playLock = new ReentrantLock();
	private final ReentrantLock _recLock = new ReentrantLock();

	private boolean _doPlayInit = true;
	private boolean _doRecInit = true;
	private boolean _isRecording = false;
	private boolean _isPlaying = false;

	private int _bufferedRecSamples = 0;
	private int _bufferedPlaySamples = 0;
	private int _playPosition = 0;

	private int _pre_audio_mode = -1;
	private int _pre_sampling = -1;

	private int _pre_recording_audioSource = 1;
	private int _pre_recording_sampleRate = 16000;

	private static boolean isRecording = false; // delia 20131030
	private static int recordChannelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO; // delia
																						// 20131030
	private static int playbackChannelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO; // delia
																						// 20131030

	WebRTCAudioDevice()
	{
		try
		{
			_playBuffer = ByteBuffer.allocateDirect(2 * 480); // Max 10 ms @ 48
																// kHz
			_recBuffer = ByteBuffer.allocateDirect(2 * 480); // Max 10 ms @ 48
																// kHz
		}
		catch (Exception e)
		{
			Log.d(logTag, e.getMessage());
		}

		_tempBufPlay = new byte[2 * 480];
		_tempBufRec = new byte[2 * 480];

		recordChannelConfig = AudioIncallManager.getRecordChannelConfig(); // delia 20131030
		playbackChannelConfig = AudioIncallManager.getPlaybackChannelConfig(); // delia 20131030
	}

	@SuppressWarnings("unused")
	private int InitRecording(int audioSource, int sampleRate)
	{
		Log.d(logTag, "init record, audioSource:" + audioSource + "sampleRate:" + sampleRate);

		_pre_recording_audioSource = audioSource;
		_pre_recording_sampleRate = sampleRate;

		// get the minimum buffer size that can be used
		int minRecBufSize = AudioRecord.getMinBufferSize(sampleRate, recordChannelConfig, // delia
																							// 20131030
				AudioFormat.ENCODING_PCM_16BIT);

		// DoLog("min rec buf size is " + minRecBufSize);

		// double size to be more safe
		int recBufSize = minRecBufSize * 2;
		_bufferedRecSamples = (5 * sampleRate) / 200;
		// DoLog("rough rec delay set to " + _bufferedRecSamples);

		// release the object
		if (_audioRecord != null)
		{
			Log.d(logTag, "Release audioRecord...");
			_audioRecord.release();
			_audioRecord = null;
		}

		int sourceindex = audioSource;
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase(Locale.ENGLISH).contains("lenovo p780"))
		{
			sourceindex = 5;
		}
		try
		{
			_audioRecord = new AudioRecord(sourceindex, sampleRate, recordChannelConfig,// 1,//2,
																						// //delia
																						// 20131030
					AudioFormat.ENCODING_PCM_16BIT, recBufSize);

		}
		catch (Exception e)
		{
			Log.d(logTag, "Create AudioRecord instance fail, " + e.getMessage());
			return -1;
		}

		// check that the audioRecord is ready to be used
		if (_audioRecord.getState() != AudioRecord.STATE_INITIALIZED)
		{
			Log.d(logTag, "rec not initialized, sampleRate : " + sampleRate);
			return -1;
		}

		// DoLog("rec sample rate set to " + sampleRate);
		Log.d(logTag, "Exit InitRecording,sampleRate: " + sampleRate);
		return _bufferedRecSamples;
	}

	@SuppressWarnings("unused")
	private int StartRecording()
	{
		Log.d(logTag, "StartRecording");
		if (_isPlaying == false)
		{
			SetAudioMode(true);
		}

		isRecording = true; // delia 20131030
		// start recording
		try
		{
			_audioRecord.startRecording();

		}
		catch (IllegalStateException e)
		{
			 Log.e(logTag, "StartRecording Err.", e);
			return -1;
		}

		_isRecording = true;
		// AudioIncallManager.getInstance(_context).setInCallMode(); //delia 20131030

		Log.d(logTag, "Exit StartRecording");
		return 0;
	}

	@SuppressWarnings("unused")
	private int InitPlayback(int sampleRate)
	{
		Log.d(logTag, "InitPlayback, sampleRate:" + sampleRate);
		// get the minimum buffer size that can be used
		// sampleRate = 16000;
		int minPlayBufSize = // //delia 20131030
		AudioTrack.getMinBufferSize(sampleRate, playbackChannelConfig, AudioFormat.ENCODING_PCM_16BIT);

		// DoLog("min play buf size is " + minPlayBufSize);

		int playBufSize = minPlayBufSize;
		if (playBufSize < 6000)
		{
			playBufSize *= 2;
		}
		_bufferedPlaySamples = 0;
		// DoLog("play buf size is " + playBufSize);

		// release the object
		if (_audioTrack != null)
		{
			Log.d(logTag, "Release audioTrack...");
			_audioTrack.release();
			_audioTrack = null;
		}

		int sampling = _audioTrack.getNativeOutputSampleRate(AudioIncallManager.getInstance(_context).getAudioStream());
		 Log.d("Audio_Effect", "_" + sampling);

		_pre_audio_mode = AudioIncallManager.getInstance(_context).getAudioStream();
		 Log.d(logTag, "setAudioConfigue audiomode: " + _pre_audio_mode);
		_pre_sampling = sampleRate;
		try
		{
			_audioTrack = new AudioTrack(AudioIncallManager.getInstance(_context).getAudioStream(), sampleRate, playbackChannelConfig, // delia
																																		// 20131030
					AudioFormat.ENCODING_PCM_16BIT, playBufSize, AudioTrack.MODE_STREAM);
		}
		catch (Exception e)
		{
			 Log.e(logTag, "Create audioTrack instance err.", e);
			return -1;
		}

		// check that the audioRecord is ready to be used
		if (_audioTrack.getState() != AudioTrack.STATE_INITIALIZED)
		{
			Log.d(logTag, "play not initialized, sampleRate:" + sampleRate);
			return -1;
		}

		// DoLog("play sample rate set to " + sampleRate);

		if (_audioManager == null && _context != null)
		{
			_audioManager = (AudioManager) _context.getSystemService(Context.AUDIO_SERVICE);
		}

		// Return max playout volume
		if (_audioManager == null)
		{
			// Don't know the max volume but still init is OK for playout,
			// so we should not return error.
			return 0;
		}
		Log.d(logTag, "Exit InitPlayback.");
		/*
		 * if(AudioIncallManager.getInstance(_context).getAudioStream() ==
		 * AudioManager.STREAM_MUSIC) {
		 * _audioManager.setStreamVolume(AudioIncallManager.getInstance(_context).getAudioStream(),
		 * _audioManager
		 * .getStreamMaxVolume(AudioIncallManager.getInstance(_context).getAudioStream())*3/5, 0);
		 * EngineLoader el = EngineLoader.getInstance(); el.setAudioControl(11, null); }
		 */
		return _audioManager.getStreamMaxVolume(AudioIncallManager.getInstance(_context).getAudioStream());
	}

	@SuppressWarnings("unused")
	private int StartPlayback()
	{
		Log.d(logTag, "StartPlayback");
		if (_isRecording == false)
		{
			SetAudioMode(true);
		}

		// start playout
		try
		{
			_audioTrack.play();

		}
		catch (IllegalStateException e)
		{
			 Log.e(logTag, "StartPlayback Err.", e);
			return -1;
		}

		_isPlaying = true;
		Log.d(logTag, "Exit StartPlayback.");
		return 0;
	}

	@SuppressWarnings("unused")
	private int StopRecording()
	{
		Log.d(logTag, "StopRecording");
		
		_recLock.lock();
		
		isRecording = false; // delia 20131030

		// AudioIncallManager.getInstance(_context).restoreInCallMode(); //delia 20131030
		if(_audioRecord == null) {
			_recLock.unlock();
			return 0;
		}
		
		try
		{
			// only stop if we are recording
			if (_audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING)
			{
				// stop recording
				try
				{
					Log.d(logTag, "call audioRecord.stop().");
					_audioRecord.stop();
				}
				catch (IllegalStateException e)
				{
					 Log.e(logTag, "audioRecord.stop() exception.", e);
					_doRecInit = true;
					_audioRecord = null;
					_recLock.unlock();
					return -1;
				}
			}

			// release the object
			Log.d(logTag, "call audioRecord.release().");
			_audioRecord.release();
			_audioRecord = null;

		}
		catch (Exception e)
		{
			Log.e(logTag, "error on call audioRecord.stop().", e);
		}
		finally
		{
			// Ensure we always unlock, both for success, exception or error
			// return.
			_doRecInit = true;
			_audioRecord = null;
			_recLock.unlock();
		}

		if (_isPlaying == false)
		{
			SetAudioMode(false);
		}

		_isRecording = false;
		Log.d(logTag, "Exit StopRecording.");
		return 0;
	}

	@SuppressWarnings("unused")
	private int StopPlayback()
	{
		Log.d(logTag, "StopPlayback.");
		
		_playLock.lock();
		
		if(_audioTrack == null) {
			_playLock.unlock();
			return 0;
		}
		
		try
		{
			// only stop if we are playing
			if (_audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)
			{
				// stop playout
				try
				{
					Log.d(logTag, "call audioTrack.stop().");
					_audioTrack.stop();
				}
				catch (IllegalStateException e)
				{
					 Log.e(logTag, "audioTrack.stop() exception.", e);
					_doPlayInit = true;
					_audioTrack = null;
					_playLock.unlock();
					return -1;
				}

				// flush the buffers
				_audioTrack.flush();
			}

			// release the object
			Log.d(logTag, "call audioTrack.release().");
			_audioTrack.release();
			_audioTrack = null;

		}
		finally
		{
			// Ensure we always unlock, both for success, exception or error
			// return.
			_doPlayInit = true;
			_audioTrack = null;
			_playLock.unlock();
		}

		if (_isRecording == false)
		{
			SetAudioMode(false);
		}

		_isPlaying = false;
		Log.d(logTag, "Exit StopPlayback.");
		return 0;
	}

	@SuppressWarnings("unused")
	private int PlayAudio(int lengthInBytes)
	{

		int bufferedSamples = 0;

		if (_pre_audio_mode != AudioIncallManager.getInstance(_context).getAudioStream() || AudioIncallManager.getInstance(_context).getIsReInitPlayback())
		{
			 Log.d(logTag, "setAudioConfigue reinit: ");
			if (AudioIncallManager.getInstance(_context).getIsReInitPlayback())
			{
				AudioIncallManager.getInstance(_context).setIsReInitPlayback(false);
			}
			StopPlayback();
			InitPlayback(_pre_sampling);
			StartPlayback();
		}

		_playLock.lock();
		try
		{
			if (_audioTrack == null)
			{
				Log.d(logTag, "Could not write _audioTrack == null");
				_playLock.unlock();
				return -2; // We have probably closed down while waiting for
							// play lock
			}

			// Set priority, only do once
			if (_doPlayInit == true)
			{
				try
				{
					android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
				}
				catch (Exception e)
				{
					Log.d(logTag, "Set play thread priority failed: " + e.getMessage());
				}
				_doPlayInit = false;
			}

			int written = 0;
			_playBuffer.get(_tempBufPlay);
			written = _audioTrack.write(_tempBufPlay, 0, lengthInBytes);
			_playBuffer.rewind(); // Reset the position to start of buffer

			// DoLog("Wrote data to sndCard");

			// increase by number of written samples
			_bufferedPlaySamples += (written >> 1);

			// decrease by number of played samples
			int pos = _audioTrack.getPlaybackHeadPosition();
			if (pos < _playPosition)
			{ // wrap or reset by driver
				_playPosition = 0; // reset
			}
			_bufferedPlaySamples -= (pos - _playPosition);
			_playPosition = pos;

			if (!_isRecording)
			{
				bufferedSamples = _bufferedPlaySamples;
			}

			if (written != lengthInBytes)
			{
				Log.d(logTag, "Could not write all data to sc (written = " + written + ", length = " + lengthInBytes + ")");
				_playLock.unlock();
				return -1;
			}

		}
		finally
		{
			// Ensure we always unlock, both for success, exception or error
			// return.
			_playLock.unlock();
		}

		return bufferedSamples;
	}

	public static int enableMic = 1;

	@SuppressWarnings("unused")
	private int RecordAudio(int lengthInBytes)
	{
		if (AudioIncallManager.getInstance(_context).getIsReInitRecording())
		{
			Log.d(logTag, "reinit record, audioSource:" + _pre_recording_audioSource + "sampleRate:" + _pre_recording_sampleRate);
			AudioIncallManager.getInstance(_context).setIsReInitRecording(false);
			StopRecording();
			InitRecording(_pre_recording_audioSource, _pre_recording_sampleRate);
			StartRecording();
		}
		_recLock.lock();

		try
		{
			if (_audioRecord == null)
			{
				Log.d(logTag, "Could not read _audioRecord = null");
				_recLock.unlock();
				return -2; // We have probably closed down while waiting for rec
							// lock
			}

			// Set priority, only do once
			if (_doRecInit == true)
			{
				try
				{
					android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
				}
				catch (Exception e)
				{
					Log.d(logTag, "Set rec thread priority failed: " + e.getMessage());
				}
				_doRecInit = false;
			}

			int readBytes = 0;
			_recBuffer.rewind(); // Reset the position to start of buffer
			readBytes = _audioRecord.read(_tempBufRec, 0, lengthInBytes);
			if (enableMic == 0)
			{ // delia 20131030
				byte[] raw = _recBuffer.array();
				Arrays.fill(raw, (byte) 0);
			}
			else
			{
				// DoLog("read " + readBytes + "from SC");
				_recBuffer.put(_tempBufRec);
			}

			if (readBytes != lengthInBytes)
			{
				Log.d(logTag, "Could not read all data from sc (read = " + readBytes + ", length = " + lengthInBytes + ")");
//				AudioIncallManager.getInstance(_context).HardwareError(0);
				_recLock.unlock();
				return -1;
			}

		}
		catch (Exception e)
		{
			 Log.d(logTag, "RecordAudio try failed: " + e.getMessage());

		}
		finally
		{
			// Ensure we always unlock, both for success, exception or error
			// return.
			_recLock.unlock();
		}

		return (_bufferedPlaySamples);
	}

	@SuppressWarnings("unused")
	private int SetPlayoutSpeaker(boolean loudspeakerOn)
	{
		// create audio manager if needed
		/*
		 * if (_audioManager == null && _context != null) { _audioManager = (AudioManager)
		 * _context.getSystemService(Context.AUDIO_SERVICE); }
		 * 
		 * if (_audioManager == null) {  Log.d(logTag,
		 * "Could not change audio routing - no audio manager"); return -1; }
		 * 
		 * int apiLevel = Integer.parseInt(android.os.Build.VERSION.SDK);
		 * 
		 * if ((3 == apiLevel) || (4 == apiLevel)) { // 1.5 and 1.6 devices if (loudspeakerOn) { //
		 * route audio to back speaker _audioManager.setMode(AudioManager.MODE_NORMAL); } else { //
		 * route audio to earpiece _audioManager.setMode(AudioManager.MODE_IN_CALL); } } else { //
		 * 2.x devices if ((android.os.Build.BRAND.equals("Samsung") ||
		 * android.os.Build.BRAND.equals("samsung")) && ((5 == apiLevel) || (6 == apiLevel) || (7 ==
		 * apiLevel))) { // Samsung 2.0, 2.0.1 and 2.1 devices if (loudspeakerOn) { // route audio
		 * to back speaker _audioManager.setMode(AudioManager.MODE_IN_CALL);
		 * _audioManager.setSpeakerphoneOn(loudspeakerOn); } else { // route audio to earpiece
		 * _audioManager.setSpeakerphoneOn(loudspeakerOn);
		 * _audioManager.setMode(AudioManager.MODE_NORMAL); } } else { // Non-Samsung and Samsung
		 * 2.2 and up devices // _audioManager.setSpeakerphoneOn(loudspeakerOn); //delia 20131030 }
		 * }
		 */
		return 0;
	}

	@SuppressWarnings("unused")
	private int SetPlayoutVolume(int level)
	{

		// create audio manager if needed
		if (_audioManager == null && _context != null)
		{
			_audioManager = (AudioManager) _context.getSystemService(Context.AUDIO_SERVICE);
		}

		int retVal = -1;

		if (_audioManager != null)
		{
			_audioManager.setStreamVolume(AudioIncallManager.getInstance(_context).getAudioStream(), level, 0);
			retVal = 0;
		}

		return retVal;
	}

	@SuppressWarnings("unused")
	private int GetPlayoutVolume()
	{

		// create audio manager if needed
		if (_audioManager == null && _context != null)
		{
			_audioManager = (AudioManager) _context.getSystemService(Context.AUDIO_SERVICE);
		}

		int level = -1;

		if (_audioManager != null)
		{
			level = _audioManager.getStreamVolume(AudioIncallManager.getInstance(_context).getAudioStream());
		}

		return level;
	}

	/*
	 * private void SetAudioMode(boolean startCall) { int apiLevel =
	 * Integer.parseInt(android.os.Build.VERSION.SDK);
	 * 
	 * if (_audioManager == null && _context != null) { _audioManager = (AudioManager)
	 * _context.getSystemService(Context.AUDIO_SERVICE); }
	 * 
	 * if (_audioManager == null) {  Log.d(logTag, "Could not set audio mode - no audio manager");
	 * return; }
	 * 
	 * // ***IMPORTANT*** When the API level for honeycomb (H) has been // decided, // the condition
	 * should be changed to include API level 8 to H-1. if
	 * ((android.os.Build.BRAND.equals("Samsung") || android.os.Build.BRAND.equals("samsung")) && (8
	 * == apiLevel)) { // Set Samsung specific VoIP mode for 2.2 devices // 4 is VoIP mode int mode
	 * = (startCall ? 4 : AudioManager.MODE_NORMAL); _audioManager.setMode(mode); if
	 * (_audioManager.getMode() != mode) {  Log.d(logTag,
	 * "Could not set audio mode for Samsung device"); } } }
	 */

	private void SetAudioMode(boolean startCall)
	{
		return;/*
				 * if (_audioManager == null && _context != null) { _audioManager = (AudioManager)
				 * _context.getSystemService(Context.AUDIO_SERVICE); }
				 * 
				 * if (_audioManager == null) {
				 * DoLogErr("Could not change audio routing - no audio manager"); return ; }
				 * 
				 * int apiLevel = android.os.Build.VERSION.SDK_INT;
				 * 
				 * if ((3 == apiLevel) || (4 == apiLevel)) { // 1.5 and 1.6 devices if (startCall) {
				 * // route audio to back speaker _audioManager.setMode(AudioManager.MODE_NORMAL); }
				 * else { // route audio to earpiece
				 * _audioManager.setMode(AudioManager.MODE_IN_CALL); } } else { // 2.x devices if
				 * ((android.os.Build.BRAND.equals("Samsung") ||
				 * android.os.Build.BRAND.equals("samsung")) && ((5 == apiLevel) || (6 == apiLevel)
				 * || (7 == apiLevel))) { // Samsung 2.0, 2.0.1 and 2.1 devices if (startCall) { //
				 * route audio to back speaker _audioManager.setMode(AudioManager.MODE_IN_CALL);
				 * _audioManager.setSpeakerphoneOn(startCall); } else { // route audio to earpiece
				 * _audioManager.setSpeakerphoneOn(startCall);
				 * _audioManager.setMode(AudioManager.MODE_NORMAL); } } else {
				 * if(android.os.Build.MODEL.equals("Lenovo A860e") ||
				 * android.os.Build.MODEL.equals("Lenovo S880") ||
				 * android.os.Build.MODEL.equals("SCH-N719") ||
				 * android.os.Build.MODEL.equals("Lenovo S899t") ||
				 * android.os.Build.MODEL.equals("GT-N7100")) { if(!_audioManager.isSpeakerphoneOn()
				 * && startCall) { //!_audioManager.isSpeakerphoneOn() &&
				 * _audioManager.setSpeakerphoneOn(startCall);
				 * //_audioManager.setMode(AudioManager.MODE_IN_CALL); }
				 * if(_audioManager.isSpeakerphoneOn() && !startCall) {
				 * //_audioManager.isSpeakerphoneOn() && _audioManager.setSpeakerphoneOn(startCall);
				 * //_audioManager.setMode(AudioManager.MODE_NORMAL); } } else {
				 * _audioManager.setSpeakerphoneOn(startCall); } } }
				 * 
				 * return ;
				 */
	}

	@SuppressWarnings("unused")
	private int NativeAECIsEnable()
	{
		return 0;
	}

	@SuppressWarnings("unused")
	private int NativeAGCIsEnable()
	{
		return 0;
	}

	@SuppressWarnings("unused")
	private int NativeNSIsEnable()
	{
		return 0;
	}

	@SuppressWarnings("unused")
	private int SetNativeAudioProcssingUsing(int flag)
	{
		return 0;
	}

	final String logTag = "WebRTC AD java";

	public static boolean isRecording()
	{
		return isRecording;
	}

	private void DoLog(String msg)
	{
		Log.d(logTag, msg);
	}

	private void DoLogErr(String msg)
	{
		 Log.d(logTag, msg);
	}

}
