package com.kinstalk.voip.sdk.logic.sip.service;

import com.kinstalk.voip.sdk.EngineSdkAudioBiInfo;
import com.kinstalk.voip.sdk.EngineSdkCallResultReport;
import com.kinstalk.voip.sdk.EngineSdkVideoBiInfo;

public class BIResultReport {
	float biRemoteVideoAvgFps;
    int biRemoteVideoLowerFpsTimes; //!< 1 time means that as video fps changes to lower than 2fps.
    String biVideoResolutionComposition; //!< Local video Width1*Height1,time1: Width2*Height2,time2:
    VideoBiInfo biVideoSampleInfo;
    AudioBiInfo biAudioSampleInfo;
    int biVideoLandscapeTimeInMs; //!< Local video landscape time:portrait time 0, 2
    int biVideoPortaitTimeInMs; //!< Local video landscape time:portrait time 1, 3
    int biAudioOutputLoudSpeakerTimeInMs; //!< Local audio output device mode: Loudspeaker:earset;Smallspeaker
    int biAudioOutputEarphoneTimeInMs;
    int biAudioOutputSmallerSpeakerTimeInMs;
    int biCameraOnTimeInMs; //!< Local camera on/off time composition: On time:Off time
    int biCameraOffTimeInMs;
    int biRemoteCameraOnTimeInMs; //!< Remote camera on/off time composition: On time:Off time
    int biRemoteCameraOffTimeInMs;
    int biMicOnTimeInMs; //!< Local camera on/off time composition: On time:Off time
    int biMicOffTimeInMs;
    int biNetTypeUnkownInMs;
    int biNetTypeAdslInMs;
    int biNetTypeWifiInMs;
    int biNetTypeFddiInMs;
    int biNetTypeGprsInMs;
    int biNetType3gChinaUnicomInMs;
    int biNetType3gChinaTelecomInMs;
    int biNetType3gChinaMobileInMs;
    int biNetType4gChinaMobileInMs;

    int biTpTypeUnknownInMs;
    int biTpTypeP2pPrivateInMs;
    int biTpTypeP2pPublicInMs;
    int biTpTypeRelayInMs;

    int biCallEndingTimeInMs;
    // Media related Stat. Created at pjmedia layer
    int biMaxAudioDelayInMs;
    int biMaxVideoDelayInMs;
    float biVideoPLR; //!< Remote video packet lost rate during the whole session. PLR=biVideoPLR/255
    float biAudioPLR; //!< Remote audio packet lost rate during the whole session. PLR=biAudioPLR/255
    long m_RemoteVideoAvgBitrate;

    // Call related Stat.
    long biCallerStartCallingTimeOfSecond; //!< Stat at caller: time of startcall;
    long biCallerStartCallingTimeOfMs;

    long biCalleeStartRingTimeOfSecond; //!< Stat at caller: time of receive ringback from callee;
    long biCalleeStartRingTimeOfMs;

    long biCalleeResponseBusyTimeOfSecond; //!< Stat at caller 
    long biCalleeResponseBusyTimeOfMs;

    long biCalleeAnswerTimeOfSecond; //!< Stat at caller
    long biCalleeAnswerTimeOfMs;

    long biSessionActiveTimeOfSecond;
    long biSessionActiveTimeOfMs;

    int biRemoteVideoFirstFrameTimeInMs;

    long biSessionEndTimeOfSecond;
    long biSessionEndTimeOfMs;

    long biMediaSetupTimeOfSecond; //!< Caller: from callee answer time to all media channels setup; callee: from answer time to all media channels setup
    long biMediaSetupTimeOfMs;
    
    boolean biLocalAccountOnlineDuringCall;
    boolean biRemoteAccountOnlineDuringCall;
    String biRelayPath;
    long biLocalPublicIP;
    boolean biAudChnEnabled;
    boolean biVidChnEnabled;
    boolean biVidSetup;
    boolean biAudSetup;
    
    public BIResultReport() {
    	biVideoSampleInfo = new VideoBiInfo();
    	biAudioSampleInfo = new AudioBiInfo();
	}
    
	public float getBiRemoteVideoAvgFps() {
		return biRemoteVideoAvgFps;
	}
	public void setBiRemoteVideoAvgFps(float biRemoteVideoAvgFps) {
		this.biRemoteVideoAvgFps = biRemoteVideoAvgFps;
	}
	public int getBiRemoteVideoLowerFpsTimes() {
		return biRemoteVideoLowerFpsTimes;
	}
	public void setBiRemoteVideoLowerFpsTimes(int biRemoteVideoLowerFpsTimes) {
		this.biRemoteVideoLowerFpsTimes = biRemoteVideoLowerFpsTimes;
	}
	public String getBiVideoResolutionComposition() {
		return biVideoResolutionComposition;
	}
	public void setBiVideoResolutionComposition(String biVideoResolutionComposition) {
		this.biVideoResolutionComposition = biVideoResolutionComposition;
	}
	public VideoBiInfo getBiVideoSampleInfo() {
		return biVideoSampleInfo;
	}
	public void setBiVideoSampleInfo(EngineSdkVideoBiInfo biVideoSampleInfo) {
		this.biVideoSampleInfo.setVideoBiInfo(biVideoSampleInfo);
	}
	public AudioBiInfo getBiAudioSampleInfo() {
		return biAudioSampleInfo;
	}
	public void setBiAudioSampleInfo(EngineSdkAudioBiInfo biAudioSampleInfo) {
		this.biAudioSampleInfo.setAudioBiInfo(biAudioSampleInfo);
	}
	public int getBiVideoLandscapeTimeInMs() {
		return biVideoLandscapeTimeInMs;
	}
	public void setBiVideoLandscapeTimeInMs(int biVideoLandscapeTimeInMs) {
		this.biVideoLandscapeTimeInMs = biVideoLandscapeTimeInMs;
	}
	public int getBiVideoPortaitTimeInMs() {
		return biVideoPortaitTimeInMs;
	}
	public void setBiVideoPortaitTimeInMs(int biVideoPortaitTimeInMs) {
		this.biVideoPortaitTimeInMs = biVideoPortaitTimeInMs;
	}
	public int getBiAudioOutputLoudSpeakerTimeInMs() {
		return biAudioOutputLoudSpeakerTimeInMs;
	}
	public void setBiAudioOutputLoudSpeakerTimeInMs(int biAudioOutputLoudSpeakerTimeInMs) {
		this.biAudioOutputLoudSpeakerTimeInMs = biAudioOutputLoudSpeakerTimeInMs;
	}
	public int getBiAudioOutputEarphoneTimeInMs() {
		return biAudioOutputEarphoneTimeInMs;
	}
	public void setBiAudioOutputEarphoneTimeInMs(int biAudioOutputEarphoneTimeInMs) {
		this.biAudioOutputEarphoneTimeInMs = biAudioOutputEarphoneTimeInMs;
	}
	public int getBiAudioOutputSmallerSpeakerTimeInMs() {
		return biAudioOutputSmallerSpeakerTimeInMs;
	}
	public void setBiAudioOutputSmallerSpeakerTimeInMs(int biAudioOutputSmallerSpeakerTimeInMs) {
		this.biAudioOutputSmallerSpeakerTimeInMs = biAudioOutputSmallerSpeakerTimeInMs;
	}
	public int getBiCameraOnTimeInMs() {
		return biCameraOnTimeInMs;
	}
	public void setBiCameraOnTimeInMs(int biCameraOnTimeInMs) {
		this.biCameraOnTimeInMs = biCameraOnTimeInMs;
	}
	public int getBiCameraOffTimeInMs() {
		return biCameraOffTimeInMs;
	}
	public void setBiCameraOffTimeInMs(int biCameraOffTimeInMs) {
		this.biCameraOffTimeInMs = biCameraOffTimeInMs;
	}
	public int getBiRemoteCameraOnTimeInMs() {
		return biRemoteCameraOnTimeInMs;
	}
	public void setBiRemoteCameraOnTimeInMs(int biRemoteCameraOnTimeInMs) {
		this.biRemoteCameraOnTimeInMs = biRemoteCameraOnTimeInMs;
	}
	public int getBiRemoteCameraOffTimeInMs() {
		return biRemoteCameraOffTimeInMs;
	}
	public void setBiRemoteCameraOffTimeInMs(int biRemoteCameraOffTimeInMs) {
		this.biRemoteCameraOffTimeInMs = biRemoteCameraOffTimeInMs;
	}
	public int getBiMicOnTimeInMs() {
		return biMicOnTimeInMs;
	}
	public void setBiMicOnTimeInMs(int biMicOnTimeInMs) {
		this.biMicOnTimeInMs = biMicOnTimeInMs;
	}
	public int getBiMicOffTimeInMs() {
		return biMicOffTimeInMs;
	}
	public void setBiMicOffTimeInMs(int biMicOffTimeInMs) {
		this.biMicOffTimeInMs = biMicOffTimeInMs;
	}
	public int getBiNetTypeUnkownInMs() {
		return biNetTypeUnkownInMs;
	}
	public void setBiNetTypeUnkownInMs(int biNetTypeUnkownInMs) {
		this.biNetTypeUnkownInMs = biNetTypeUnkownInMs;
	}
	public int getBiNetTypeAdslInMs() {
		return biNetTypeAdslInMs;
	}
	public void setBiNetTypeAdslInMs(int biNetTypeAdslInMs) {
		this.biNetTypeAdslInMs = biNetTypeAdslInMs;
	}
	public int getBiNetTypeWifiInMs() {
		return biNetTypeWifiInMs;
	}
	public void setBiNetTypeWifiInMs(int biNetTypeWifiInMs) {
		this.biNetTypeWifiInMs = biNetTypeWifiInMs;
	}
	public int getBiNetTypeFddiInMs() {
		return biNetTypeFddiInMs;
	}
	public void setBiNetTypeFddiInMs(int biNetTypeFddiInMs) {
		this.biNetTypeFddiInMs = biNetTypeFddiInMs;
	}
	public int getBiNetTypeGprsInMs() {
		return biNetTypeGprsInMs;
	}
	public void setBiNetTypeGprsInMs(int biNetTypeGprsInMs) {
		this.biNetTypeGprsInMs = biNetTypeGprsInMs;
	}
	public int getBiNetType3gChinaUnicomInMs() {
		return biNetType3gChinaUnicomInMs;
	}
	public void setBiNetType3gChinaUnicomInMs(int biNetType3gChinaUnicomInMs) {
		this.biNetType3gChinaUnicomInMs = biNetType3gChinaUnicomInMs;
	}
	public int getBiNetType3gChinaTelecomInMs() {
		return biNetType3gChinaTelecomInMs;
	}
	public void setBiNetType3gChinaTelecomInMs(int biNetType3gChinaTelecomInMs) {
		this.biNetType3gChinaTelecomInMs = biNetType3gChinaTelecomInMs;
	}
	public int getBiNetType3gChinaMobileInMs() {
		return biNetType3gChinaMobileInMs;
	}
	public void setBiNetType3gChinaMobileInMs(int biNetType3gChinaMobileInMs) {
		this.biNetType3gChinaMobileInMs = biNetType3gChinaMobileInMs;
	}
	public int getBiNetType4gChinaMobileInMs() {
		return biNetType4gChinaMobileInMs;
	}
	public void setBiNetType4gChinaMobileInMs(int biNetType4gChinaMobileInMs) {
		this.biNetType4gChinaMobileInMs = biNetType4gChinaMobileInMs;
	}
	public int getBiTpTypeUnknownInMs() {
		return biTpTypeUnknownInMs;
	}
	public void setBiTpTypeUnknownInMs(int biTpTypeUnknownInMs) {
		this.biTpTypeUnknownInMs = biTpTypeUnknownInMs;
	}
	public int getBiTpTypeP2pPrivateInMs() {
		return biTpTypeP2pPrivateInMs;
	}
	public void setBiTpTypeP2pPrivateInMs(int biTpTypeP2pPrivateInMs) {
		this.biTpTypeP2pPrivateInMs = biTpTypeP2pPrivateInMs;
	}
	public int getBiTpTypeP2pPublicInMs() {
		return biTpTypeP2pPublicInMs;
	}
	public void setBiTpTypeP2pPublicInMs(int biTpTypeP2pPublicInMs) {
		this.biTpTypeP2pPublicInMs = biTpTypeP2pPublicInMs;
	}
	public int getBiTpTypeRelayInMs() {
		return biTpTypeRelayInMs;
	}
	public void setBiTpTypeRelayInMs(int biTpTypeRelayInMs) {
		this.biTpTypeRelayInMs = biTpTypeRelayInMs;
	}
	public int getBiCallEndingTimeInMs() {
		return biCallEndingTimeInMs;
	}
	public void setBiCallEndingTimeInMs(int biCallEndingTimeInMs) {
		this.biCallEndingTimeInMs = biCallEndingTimeInMs;
	}
	public int getBiMaxAudioDelayInMs() {
		return biMaxAudioDelayInMs;
	}
	public void setBiMaxAudioDelayInMs(int biMaxAudioDelayInMs) {
		this.biMaxAudioDelayInMs = biMaxAudioDelayInMs;
	}
	public int getBiMaxVideoDelayInMs() {
		return biMaxVideoDelayInMs;
	}
	public void setBiMaxVideoDelayInMs(int biMaxVideoDelayInMs) {
		this.biMaxVideoDelayInMs = biMaxVideoDelayInMs;
	}
	public float getBiVideoPLR() {
		return biVideoPLR;
	}
	public void setBiVideoPLR(float biVideoPLR) {
		this.biVideoPLR = biVideoPLR;
	}
	public float getBiAudioPLR() {
		return biAudioPLR;
	}
	public void setBiAudioPLR(float biAudioPLR) {
		this.biAudioPLR = biAudioPLR;
	}
	public long getM_RemoteVideoAvgBitrate() {
		return m_RemoteVideoAvgBitrate;
	}
	public void setM_RemoteVideoAvgBitrate(long m_RemoteVideoAvgBitrate) {
		this.m_RemoteVideoAvgBitrate = m_RemoteVideoAvgBitrate;
	}
	public long getBiCallerStartCallingTimeOfSecond() {
		return biCallerStartCallingTimeOfSecond;
	}
	public void setBiCallerStartCallingTimeOfSecond(long biCallerStartCallingTimeOfSecond) {
		this.biCallerStartCallingTimeOfSecond = biCallerStartCallingTimeOfSecond;
	}
	public long getBiCallerStartCallingTimeOfMs() {
		return biCallerStartCallingTimeOfMs;
	}
	public void setBiCallerStartCallingTimeOfMs(long biCallerStartCallingTimeOfMs) {
		this.biCallerStartCallingTimeOfMs = biCallerStartCallingTimeOfMs;
	}
	public long getBiCalleeStartRingTimeOfSecond() {
		return biCalleeStartRingTimeOfSecond;
	}
	public void setBiCalleeStartRingTimeOfSecond(long biCalleeStartRingTimeOfSecond) {
		this.biCalleeStartRingTimeOfSecond = biCalleeStartRingTimeOfSecond;
	}
	public long getBiCalleeStartRingTimeOfMs() {
		return biCalleeStartRingTimeOfMs;
	}
	public void setBiCalleeStartRingTimeOfMs(long biCalleeStartRingTimeOfMs) {
		this.biCalleeStartRingTimeOfMs = biCalleeStartRingTimeOfMs;
	}
	public long getBiCalleeResponseBusyTimeOfSecond() {
		return biCalleeResponseBusyTimeOfSecond;
	}
	public void setBiCalleeResponseBusyTimeOfSecond(long biCalleeResponseBusyTimeOfSecond) {
		this.biCalleeResponseBusyTimeOfSecond = biCalleeResponseBusyTimeOfSecond;
	}
	public long getBiCalleeResponseBusyTimeOfMs() {
		return biCalleeResponseBusyTimeOfMs;
	}
	public void setBiCalleeResponseBusyTimeOfMs(long biCalleeResponseBusyTimeOfMs) {
		this.biCalleeResponseBusyTimeOfMs = biCalleeResponseBusyTimeOfMs;
	}
	public long getBiCalleeAnswerTimeOfSecond() {
		return biCalleeAnswerTimeOfSecond;
	}
	public void setBiCalleeAnswerTimeOfSecond(long biCalleeAnswerTimeOfSecond) {
		this.biCalleeAnswerTimeOfSecond = biCalleeAnswerTimeOfSecond;
	}
	public long getBiCalleeAnswerTimeOfMs() {
		return biCalleeAnswerTimeOfMs;
	}
	public void setBiCalleeAnswerTimeOfMs(long biCalleeAnswerTimeOfMs) {
		this.biCalleeAnswerTimeOfMs = biCalleeAnswerTimeOfMs;
	}
	public long getBiSessionActiveTimeOfSecond() {
		return biSessionActiveTimeOfSecond;
	}
	public void setBiSessionActiveTimeOfSecond(long biSessionActiveTimeOfSecond) {
		this.biSessionActiveTimeOfSecond = biSessionActiveTimeOfSecond;
	}
	public long getBiSessionActiveTimeOfMs() {
		return biSessionActiveTimeOfMs;
	}
	public void setBiSessionActiveTimeOfMs(long biSessionActiveTimeOfMs) {
		this.biSessionActiveTimeOfMs = biSessionActiveTimeOfMs;
	}
	public int getBiRemoteVideoFirstFrameTimeInMs() {
		return biRemoteVideoFirstFrameTimeInMs;
	}
	public void setBiRemoteVideoFirstFrameTimeInMs(int biRemoteVideoFirstFrameTimeInMs) {
		this.biRemoteVideoFirstFrameTimeInMs = biRemoteVideoFirstFrameTimeInMs;
	}
	public long getBiSessionEndTimeOfSecond() {
		return biSessionEndTimeOfSecond;
	}
	public void setBiSessionEndTimeOfSecond(long biSessionEndTimeOfSecond) {
		this.biSessionEndTimeOfSecond = biSessionEndTimeOfSecond;
	}
	public long getBiSessionEndTimeOfMs() {
		return biSessionEndTimeOfMs;
	}
	public void setBiSessionEndTimeOfMs(long biSessionEndTimeOfMs) {
		this.biSessionEndTimeOfMs = biSessionEndTimeOfMs;
	}
	public long getBiMediaSetupTimeOfSecond() {
		return biMediaSetupTimeOfSecond;
	}
	public void setBiMediaSetupTimeOfSecond(long biMediaSetupTimeOfSecond) {
		this.biMediaSetupTimeOfSecond = biMediaSetupTimeOfSecond;
	}
	public long getBiMediaSetupTimeOfMs() {
		return biMediaSetupTimeOfMs;
	}
	public void setBiMediaSetupTimeOfMs(long biMediaSetupTimeOfMs) {
		this.biMediaSetupTimeOfMs = biMediaSetupTimeOfMs;
	}
	public boolean isBiLocalAccountOnlineDuringCall() {
		return biLocalAccountOnlineDuringCall;
	}
	public void setBiLocalAccountOnlineDuringCall(boolean biLocalAccountOnlineDuringCall) {
		this.biLocalAccountOnlineDuringCall = biLocalAccountOnlineDuringCall;
	}
	public boolean isBiRemoteAccountOnlineDuringCall() {
		return biRemoteAccountOnlineDuringCall;
	}
	public void setBiRemoteAccountOnlineDuringCall(boolean biRemoteAccountOnlineDuringCall) {
		this.biRemoteAccountOnlineDuringCall = biRemoteAccountOnlineDuringCall;
	}
	public String getBiRelayPath() {
		return biRelayPath;
	}
	public void setBiRelayPath(String biRelayPath) {
		this.biRelayPath = biRelayPath;
	}
	public long getBiLocalPublicIP() {
		return biLocalPublicIP;
	}
	public void setBiLocalPublicIP(long biLocalPublicIP) {
		this.biLocalPublicIP = biLocalPublicIP;
	}
	public boolean isBiAudChnEnabled() {
		return biAudChnEnabled;
	}
	public void setBiAudChnEnabled(boolean biAudChnEnabled) {
		this.biAudChnEnabled = biAudChnEnabled;
	}
	public boolean isBiVidChnEnabled() {
		return biVidChnEnabled;
	}
	public void setBiVidChnEnabled(boolean biVidChnEnabled) {
		this.biVidChnEnabled = biVidChnEnabled;
	}
	public boolean isBiVidSetup() {
		return biVidSetup;
	}
	public void setBiVidSetup(boolean biVidSetup) {
		this.biVidSetup = biVidSetup;
	}
	public boolean isBiAudSetup() {
		return biAudSetup;
	}
	public void setBiAudSetup(boolean biAudSetup) {
		this.biAudSetup = biAudSetup;
	}
    
	public void setResultReport(EngineSdkCallResultReport info) {
		setBiRemoteVideoAvgFps(info.getBiRemoteVideoAvgFps());
		setBiRemoteVideoLowerFpsTimes(info.getBiRemoteVideoLowerFpsTimes());
		setBiVideoResolutionComposition(info.getBiVideoResolutionComposition());
		setBiVideoSampleInfo(info.getBiVideoSampleInfo());
		setBiAudioSampleInfo(info.getBiAudioSampleInfo());
		setBiVideoLandscapeTimeInMs(info.getBiVideoLandscapeTimeInMs());
		setBiVideoPortaitTimeInMs(info.getBiVideoPortaitTimeInMs());
		setBiAudioOutputLoudSpeakerTimeInMs(info.getBiAudioOutputLoudSpeakerTimeInMs());
		setBiAudioOutputEarphoneTimeInMs(info.getBiAudioOutputEarphoneTimeInMs());
		setBiAudioOutputSmallerSpeakerTimeInMs(info.getBiAudioOutputSmallerSpeakerTimeInMs());
		setBiCameraOnTimeInMs(info.getBiCameraOnTimeInMs());
		setBiCameraOffTimeInMs(info.getBiCameraOffTimeInMs());
		setBiRemoteCameraOnTimeInMs(info.getBiRemoteCameraOnTimeInMs());
		setBiRemoteCameraOffTimeInMs(info.getBiRemoteCameraOffTimeInMs());
		setBiMicOnTimeInMs(info.getBiMicOnTimeInMs());
		setBiMicOffTimeInMs(info.getBiMicOffTimeInMs());
		setBiNetTypeUnkownInMs(info.getBiNetTypeUnkownInMs());
		setBiNetTypeAdslInMs(info.getBiNetTypeAdslInMs());
		setBiNetTypeWifiInMs(info.getBiNetTypeWifiInMs());
		setBiNetTypeFddiInMs(info.getBiNetTypeFddiInMs());
		setBiNetTypeGprsInMs(info.getBiNetTypeGprsInMs());
		setBiNetType3gChinaUnicomInMs(info.getBiNetType3gChinaUnicomInMs());
		setBiNetType3gChinaTelecomInMs(info.getBiNetType3gChinaTelecomInMs());
		setBiNetType3gChinaMobileInMs(info.getBiNetType3gChinaMobileInMs());
		setBiNetType4gChinaMobileInMs(info.getBiNetType4gChinaMobileInMs());
		setBiTpTypeUnknownInMs(info.getBiTpTypeUnknownInMs());
		setBiTpTypeP2pPrivateInMs(info.getBiTpTypeP2pPrivateInMs());
		setBiTpTypeP2pPublicInMs(info.getBiTpTypeP2pPublicInMs());
		setBiTpTypeRelayInMs(info.getBiTpTypeRelayInMs());
		setBiCallEndingTimeInMs(info.getBiCallEndingTimeInMs());
		setBiMaxAudioDelayInMs(info.getBiMaxAudioDelayInMs());
		setBiMaxVideoDelayInMs(info.getBiMaxVideoDelayInMs());
		setBiVideoPLR(info.getBiVideoPLR());
		setBiAudioPLR(info.getBiAudioPLR());
		setM_RemoteVideoAvgBitrate(info.getM_RemoteVideoAvgBitrate());
		setBiCallerStartCallingTimeOfSecond(info.getBiCallerStartCallingTimeOfSecond());
		setBiCallerStartCallingTimeOfMs(info.getBiCallerStartCallingTimeOfMs());
		setBiCalleeStartRingTimeOfSecond(info.getBiCalleeStartRingTimeOfSecond());
		setBiCalleeStartRingTimeOfMs(info.getBiCalleeStartRingTimeOfMs());
		setBiCalleeResponseBusyTimeOfSecond(info.getBiCalleeResponseBusyTimeOfSecond());
		setBiCalleeResponseBusyTimeOfMs(info.getBiCalleeResponseBusyTimeOfMs());
		setBiCalleeAnswerTimeOfSecond(info.getBiCalleeAnswerTimeOfSecond());
		setBiCalleeAnswerTimeOfMs(info.getBiCalleeAnswerTimeOfMs());
		setBiSessionActiveTimeOfSecond(info.getBiSessionActiveTimeOfSecond());
		setBiSessionActiveTimeOfMs(info.getBiSessionActiveTimeOfMs());
		setBiRemoteVideoFirstFrameTimeInMs(info.getBiRemoteVideoFirstFrameTimeInMs());
		setBiSessionEndTimeOfSecond(info.getBiSessionEndTimeOfSecond());
		setBiSessionEndTimeOfMs(info.getBiSessionEndTimeOfMs());
		setBiMediaSetupTimeOfSecond(info.getBiMediaSetupTimeOfSecond());
		setBiMediaSetupTimeOfMs(info.getBiMediaSetupTimeOfMs());
		setBiLocalAccountOnlineDuringCall(info.getBiLocalAccountOnlineDuringCall());
	    setBiRemoteAccountOnlineDuringCall(info.getBiRemoteAccountOnlineDuringCall());
	    setBiRelayPath(info.getBiRelayPath());
	    setBiLocalPublicIP(info.getBiLocalPublicIP());
	    setBiAudChnEnabled(info.getBiAudChnEnabled());
	    setBiVidChnEnabled(info.getBiVidChnEnabled());
	    setBiVidSetup(info.getBiVidSetup());
	    setBiAudSetup(info.getBiAudSetup());
	}
}
