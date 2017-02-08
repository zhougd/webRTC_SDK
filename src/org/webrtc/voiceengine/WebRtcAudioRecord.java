/*
 *  Copyright (c) 2013 The WebRTC project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

package org.webrtc.voiceengine;

import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;

import com.kinstalk.voip.sdk.logic.sip.utility.AudioIncallManager;
import com.kinstalk.voip.sdk.common.Log;

class WebRtcAudioRecord {
    private AudioRecord _audioRecord = null;

    private Context _context;

    private ByteBuffer _recBuffer;
    private byte[] _tempBufRec;

    private final ReentrantLock _recLock = new ReentrantLock();

    private boolean _doRecInit = true;
    private boolean _isRecording = false;

    private int _bufferedRecSamples = 0;

    private int _pre_recording_audioSource = 1;
    private int _pre_recording_sampleRate = 16000;

    WebRtcAudioRecord() {
        try {
            _recBuffer = ByteBuffer.allocateDirect(2 * 480); // Max 10 ms @ 48
                                                             // kHz
        } catch (Exception e) {
            DoLog(e.getMessage());
        }

        _tempBufRec = new byte[2 * 480];
    }

    @SuppressWarnings("unused")
    private int InitRecording(int audioSource, int sampleRate) {
        
    	Log.d(logTag, "init record, audioSource:" + audioSource + "sampleRate:" + sampleRate);
        audioSource = AudioSource.VOICE_COMMUNICATION;//AudioSource.MIC;//
        
        _pre_recording_audioSource = audioSource;
        _pre_recording_sampleRate = sampleRate;

        // get the minimum buffer size that can be used
        int minRecBufSize = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT);

        // DoLog("min rec buf size is " + minRecBufSize);

        // double size to be more safe
        int recBufSize = minRecBufSize * 2;
        // On average half of the samples have been recorded/buffered and the
        // recording interval is 1/100s.
        _bufferedRecSamples = sampleRate / 200;
        // DoLog("rough rec delay set to " + _bufferedRecSamples);

        // release the object
        if (_audioRecord != null) {
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

        try {
        	Log.d(logTag, "audioSource:" + sourceindex);
            _audioRecord = new AudioRecord(
                            sourceindex,
                            sampleRate,
                            AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT,
                            recBufSize);

        } catch (Exception e) {
            DoLog(e.getMessage());
            return -1; 
        }

        // check that the audioRecord is ready to be used
        if (_audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            DoLog("rec not initialized " + sampleRate);
            return -1;
        }

        // DoLog("rec sample rate set to " + sampleRate);
        Log.d(logTag, "Exit InitRecording,sampleRate: " + sampleRate);
        return _bufferedRecSamples;
    }

    @SuppressWarnings("unused")
    private int StartRecording() {
        Log.d(logTag, "StartRecording");
        // start recording
        try {
            _audioRecord.startRecording();

        } catch (IllegalStateException e) {
            e.printStackTrace();
            return -1;
        }

        _isRecording = true;

        Log.d(logTag, "Exit StartRecording");

        return 0;
    }

    @SuppressWarnings("unused")
    private int StopRecording() {
        Log.d(logTag, "StopRecording");
        _recLock.lock();

        if(_audioRecord == null) {
            _recLock.unlock();
            return 0;
        }

        try {
            // only stop if we are recording
            if (_audioRecord.getRecordingState() ==
              AudioRecord.RECORDSTATE_RECORDING) {
                // stop recording
                try {
                    Log.d(logTag, "call audioRecord.stop().");
                    _audioRecord.stop();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                     Log.e(logTag, "audioRecord.stop() exception.", e);
                    _audioRecord = null;
                    //_recLock.unlock();
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
        finally {
            // Ensure we always unlock, both for success, exception or error
            // return.
            _doRecInit = true;
            _audioRecord = null;
            _recLock.unlock();
        }

        _isRecording = false;
        Log.d(logTag, "Exit StopRecording.");
        return 0;
    }

    @SuppressWarnings("unused")
    private int RecordAudio(int lengthInBytes) {
        if (AudioIncallManager.getInstance(_context).getIsReInitRecording())
        {
            Log.d(logTag, "reinit record, audioSource:" + _pre_recording_audioSource + "sampleRate:" + _pre_recording_sampleRate);
            AudioIncallManager.getInstance(_context).setIsReInitRecording(false);
            StopRecording();
            InitRecording(_pre_recording_audioSource, _pre_recording_sampleRate);
            StartRecording();
        }

        _recLock.lock();

        try {
            if (_audioRecord == null) {
            	 //_recLock.unlock();
                return -2; // We have probably closed down while waiting for rec
                           // lock
            }

            // Set priority, only do once
            if (_doRecInit == true) {
                try {
                    android.os.Process.setThreadPriority(
                        android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                } catch (Exception e) {
                    DoLog("Set rec thread priority failed: " + e.getMessage());
                }
                _doRecInit = false;
            }

            int readBytes = 0;
            _recBuffer.rewind(); // Reset the position to start of buffer
            readBytes = _audioRecord.read(_tempBufRec, 0, lengthInBytes);
            // DoLog("read " + readBytes + "from SC");
            _recBuffer.put(_tempBufRec);

            if (readBytes != lengthInBytes) {
                DoLog("Could not read all data from sc (read = " + readBytes + ", length = " + lengthInBytes + ")");
//                AudioIncallManager.getInstance(_context).HardwareError(0);
                //_recLock.unlock();
                return -1;
            }

        } catch (Exception e) {
            DoLogErr("RecordAudio try failed: " + e.getMessage());

        } finally {
            // Ensure we always unlock, both for success, exception or error
            // return.
            _recLock.unlock();
        }

        return _bufferedRecSamples;
    }

    final String logTag = "WebRTC AD java";

    private void DoLog(String msg) {
        Log.d(logTag, msg);
    }

    private void DoLogErr(String msg) {
         Log.d(logTag, msg);
    }
}
