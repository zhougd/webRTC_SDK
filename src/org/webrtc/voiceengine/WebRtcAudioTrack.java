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
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.kinstalk.voip.sdk.logic.sip.utility.AudioIncallManager;
import com.kinstalk.voip.sdk.common.Log;

class WebRtcAudioTrack {
    private AudioTrack _audioTrack = null;

    private Context _context;
    private AudioManager _audioManager;

    private ByteBuffer _playBuffer;
    private byte[] _tempBufPlay;

    private final ReentrantLock _playLock = new ReentrantLock();

    private boolean _doPlayInit = true;
    private boolean _doRecInit = true;
    private boolean _isRecording = false;
    private boolean _isPlaying = false;

    private int _bufferedPlaySamples = 0;
    private int _playPosition = 0;

    private int _pre_audio_mode = -1;
    private int _pre_sampling = -1;

    WebRtcAudioTrack() {
        try {
            _playBuffer = ByteBuffer.allocateDirect(2 * 480); // Max 10 ms @ 48
                                                              // kHz
        } catch (Exception e) {
            DoLog(e.getMessage());
        }

        _tempBufPlay = new byte[2 * 480];
    }

    @SuppressWarnings("unused")
    private int InitPlayback(int sampleRate) {
        Log.d(logTag, "InitPlayback, sampleRate:" + sampleRate);
        // get the minimum buffer size that can be used
        int minPlayBufSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT);

        // DoLog("min play buf size is " + minPlayBufSize);

        int playBufSize = minPlayBufSize;
        if (playBufSize < 6000) {
            playBufSize *= 2;
        }
        _bufferedPlaySamples = 0;
        // DoLog("play buf size is " + playBufSize);

        // release the object
        if (_audioTrack != null) {
            _audioTrack.release();
            _audioTrack = null;
        }

        _pre_audio_mode = AudioIncallManager.getInstance(_context).getAudioStream();
         Log.d(logTag, "setAudioConfigue audiomode: " + _pre_audio_mode);
        _pre_sampling = sampleRate;

        try {
            _audioTrack = new AudioTrack(
                            AudioIncallManager.getInstance(_context).getAudioStream(),
                            sampleRate,
                            AudioFormat.CHANNEL_OUT_MONO,
                            AudioFormat.ENCODING_PCM_16BIT,
                            playBufSize, AudioTrack.MODE_STREAM);
        } catch (Exception e) {
            DoLog(e.getMessage());
            return -1;
        }

        // check that the audioRecord is ready to be used
        if (_audioTrack.getState() != AudioTrack.STATE_INITIALIZED) {
            DoLog("play not initialized " + sampleRate);
            return -1;
        }

        // DoLog("play sample rate set to " + sampleRate);

        if (_audioManager == null && _context != null) {
            _audioManager = (AudioManager)
                _context.getSystemService(Context.AUDIO_SERVICE);
        }

        // Return max playout volume
        if (_audioManager == null) {
            // Don't know the max volume but still init is OK for playout,
            // so we should not return error.
            return 0;
        }
        Log.d(logTag, "Exit InitPlayback.");
        return _audioManager.getStreamMaxVolume(AudioIncallManager.getInstance(_context).getAudioStream());
    }

    @SuppressWarnings("unused")
    private int StartPlayback() {
        Log.d(logTag, "StartPlayback");
        // start playout
        try {
            _audioTrack.play();

        } catch (IllegalStateException e) {
            e.printStackTrace();
             Log.e(logTag, "StartPlayback Err.", e);
            return -1;
        }

        _isPlaying = true;
        Log.d(logTag, "Exit StartPlayback.");
        return 0;
    }

    @SuppressWarnings("unused")
    private int StopPlayback() {
        Log.d(logTag, "StopPlayback.");

        _playLock.lock();

        if(_audioTrack == null) {
            _playLock.unlock();
            return 0;
        }

        try {
            // only stop if we are playing
            if (_audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                // stop playout
                try {
                    Log.d(logTag, "call audioTrack.stop().");
                    _audioTrack.stop();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                     Log.e(logTag, "audioTrack.stop() exception.", e);
                    _doPlayInit = true;
                    _audioTrack = null;
                    //_playLock.unlock();
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
        catch (Exception e)
        {
            Log.e(logTag, "error on call audioTrack.stop().", e);
        }
        finally {
            // Ensure we always unlock, both for success, exception or error
            // return.
            _doPlayInit = true;
            _audioTrack = null;
            _playLock.unlock();
        }

        _isPlaying = false;
        Log.d(logTag, "Exit StopPlayback.");
        return 0;
    }

    @SuppressWarnings("unused")
    private int PlayAudio(int lengthInBytes) {

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
        try {
            if (_audioTrack == null) {
            	//_playLock.unlock();
            	return -2; // We have probably closed down while waiting for
                           // play lock
            }

            // Set priority, only do once
            if (_doPlayInit == true) {
                try {
                    android.os.Process.setThreadPriority(
                        android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                } catch (Exception e) {
                    DoLog("Set play thread priority failed: " + e.getMessage());
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
            if (pos < _playPosition) { // wrap or reset by driver
                _playPosition = 0; // reset
            }
            _bufferedPlaySamples -= (pos - _playPosition);
            _playPosition = pos;


            if (written != lengthInBytes) {
                DoLog("Could not write all data to sc (written = " + written + ", length = " + lengthInBytes + ")");
                //_playLock.unlock();
                return -1;
            }

        } 
        catch (Exception e)
        {
            Log.e(logTag, "error on call audioTrack.play().", e);
        }
        finally {
            // Ensure we always unlock, both for success, exception or error
            // return.
            _playLock.unlock();
        }

        return _bufferedPlaySamples;
    }

    @SuppressWarnings("unused")
    private int SetPlayoutSpeaker(boolean loudspeakerOn) {
        // create audio manager if needed
        /*if (_audioManager == null && _context != null) {
            _audioManager = (AudioManager)
                _context.getSystemService(Context.AUDIO_SERVICE);
        }

        if (_audioManager == null) {
            DoLogErr("Could not change audio routing - no audio manager");
            return -1;
        }

        int apiLevel = android.os.Build.VERSION.SDK_INT;

        if ((3 == apiLevel) || (4 == apiLevel)) {
            // 1.5 and 1.6 devices
            if (loudspeakerOn) {
                // route audio to back speaker
                _audioManager.setMode(AudioManager.MODE_NORMAL);
            } else {
                // route audio to earpiece
                _audioManager.setMode(AudioManager.MODE_IN_CALL);
            }
        } else {
            // 2.x devices
            if ((android.os.Build.BRAND.equals("Samsung") ||
                            android.os.Build.BRAND.equals("samsung")) &&
                            ((5 == apiLevel) || (6 == apiLevel) ||
                            (7 == apiLevel))) {
                // Samsung 2.0, 2.0.1 and 2.1 devices
                if (loudspeakerOn) {
                    // route audio to back speaker
                    _audioManager.setMode(AudioManager.MODE_IN_CALL);
                    _audioManager.setSpeakerphoneOn(loudspeakerOn);
                } else {
                    // route audio to earpiece
                    _audioManager.setSpeakerphoneOn(loudspeakerOn);
                    _audioManager.setMode(AudioManager.MODE_NORMAL);
                }
            } else {
                // Non-Samsung and Samsung 2.2 and up devices
                _audioManager.setSpeakerphoneOn(loudspeakerOn);
            }
        }*/

        return 0;
    }

    @SuppressWarnings("unused")
    private int SetPlayoutVolume(int level) {

        // create audio manager if needed
        if (_audioManager == null && _context != null) {
            _audioManager = (AudioManager)
                _context.getSystemService(Context.AUDIO_SERVICE);
        }

        int retVal = -1;

        if (_audioManager != null) {
            _audioManager.setStreamVolume(AudioIncallManager.getInstance(_context).getAudioStream(),
                            level, 0);
            retVal = 0;
        }

        return retVal;
    }

    @SuppressWarnings("unused")
    private int GetPlayoutVolume() {

        // create audio manager if needed
        if (_audioManager == null && _context != null) {
            _audioManager = (AudioManager)
                _context.getSystemService(Context.AUDIO_SERVICE);
        }

        int level = -1;

        if (_audioManager != null) {
            level = _audioManager.getStreamVolume(AudioIncallManager.getInstance(_context).getAudioStream());
        }

        return level;
    }

    final String logTag = "WebRTC AD java";

    private void DoLog(String msg) {
        Log.d(logTag, msg);
    }

    private void DoLogErr(String msg) {
         Log.d(logTag, msg);
    }
}
