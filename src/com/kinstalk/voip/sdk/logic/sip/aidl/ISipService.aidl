package com.kinstalk.voip.sdk.logic.sip.aidl;

import com.kinstalk.voip.sdk.logic.sip.aidl.model.CallInfo;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.CallConfiguration;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.AccountConfiguration;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.EngineConfiguration;
import com.kinstalk.voip.sdk.logic.sip.aidl.ISipServiceListener;
interface ISipService
{
	void init(in EngineConfiguration ec,in String inCallActivityName, int outGoingCallRingtoneId);
	ISipServiceListener setRPCListener(ISipServiceListener listener, String listenerProcessName);
	void setUserAccount(in AccountConfiguration configuration);
	void makeCall(String to, String gid, boolean isAudioOn, boolean isMainVideoOn);
	void answerCall(boolean isAudioOn, boolean isMainVideoOn);
	int sendMessage(String to, String message, String mimeType, int senderModule);
	void deleteAccount();
    void destroyEngine();
    void endCall(String reasonText);
  	void setNetworkType(int netType);
    void addClientFeature(String featureName);
    void setAudioControlMode(int mode);
    void setCustomNotificationView(int notificationId,in Notification notification);
    void setForcedRelayPath(String relay_path);
    
    void setDown2Audio(boolean pause);
    void setTransmitingVideoPaused(boolean pause);
    void requestRemotePauseTransmitingVideo(boolean pause, boolean allowResetByPeer);
    
    // API for MediaManager
    void pauseInCallOperate();
    void resumeInCallOperate();
    void entryInCallState();
    void leaveInCallState();
    void playRingtone(int ringtoneType);
    void stopPlaying();
    void setInCallingMode(int mode);
    void setHandfree(boolean isHandfreeOn);
    void setEarphoneConnected(boolean isEarphoneConnected);
    void setBluetoothConnected(boolean isBluetoothConnected);
    void operateSystemAudio();
}