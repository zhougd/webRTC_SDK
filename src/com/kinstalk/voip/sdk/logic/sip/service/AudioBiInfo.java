package com.kinstalk.voip.sdk.logic.sip.service;

import com.kinstalk.voip.sdk.EngineSdkAudioBiInfo;

public class AudioBiInfo {
	String fractionLost;
	String cumulativeLost;
	String extendedMax;
	String jitterSamples;
	String rttMs;

	String bpsSend;
	String bpsReceive;
	public String getFractionLost() {
		return fractionLost;
	}
	public void setFractionLost(String fractionLost) {
		this.fractionLost = fractionLost;
	}
	public String getCumulativeLost() {
		return cumulativeLost;
	}
	public void setCumulativeLost(String cumulativeLost) {
		this.cumulativeLost = cumulativeLost;
	}
	public String getExtendedMax() {
		return extendedMax;
	}
	public void setExtendedMax(String extendedMax) {
		this.extendedMax = extendedMax;
	}
	public String getJitterSamples() {
		return jitterSamples;
	}
	public void setJitterSamples(String jitterSamples) {
		this.jitterSamples = jitterSamples;
	}
	public String getRttMs() {
		return rttMs;
	}
	public void setRttMs(String rttMs) {
		this.rttMs = rttMs;
	}
	public String getBpsSend() {
		return bpsSend;
	}
	public void setBpsSend(String bpsSend) {
		this.bpsSend = bpsSend;
	}
	public String getBpsReceive() {
		return bpsReceive;
	}
	public void setBpsReceive(String bpsReceive) {
		this.bpsReceive = bpsReceive;
	}
	
	public void setAudioBiInfo(EngineSdkAudioBiInfo audioinfo) {
		setFractionLost(audioinfo.getFractionLost());
		setCumulativeLost(audioinfo.getCumulativeLost());
		setExtendedMax(audioinfo.getExtendedMax());
		setJitterSamples(audioinfo.getJitterSamples());
		setRttMs(audioinfo.getRttMs());
		setBpsSend(audioinfo.getBpsSend());
		setBpsReceive(audioinfo.getBpsReceive());
	}
}
