package com.kinstalk.voip.sdk.logic.sip.service;

import com.kinstalk.voip.sdk.EngineSdkVideoBiInfo;

public class VideoBiInfo {
	String fpsCapture;
    String fpsStrategy;
    String fpsEncode;
    String fpsDecode;
    String fpsRender;

    String bpsStrategy;
    String bpsEncode;
    String bpsSend;
    String bpsReceive;
    String bpsDecode;

    String lossRateSend;
    String lossRateReceive;
    String rtt;
    String jitterSend;
    String jitterReceive;

    /**
     * 0-down, 1-up, 2-detecting, 3-stable 
     */
    String strategyTrend;
    String strategyDetect;

    String widthCapture;
    String widthStrategy;
    String widthEncode;
    String widthDecode;
    String widthRender;

    String heightCapture;
    String heightStrategy;
    String heightEncode;
    String heightDecode;
    String heightRender;
	public String getFpsCapture() {
		return fpsCapture;
	}
	public void setFpsCapture(String fpsCapture) {
		this.fpsCapture = fpsCapture;
	}
	public String getFpsStrategy() {
		return fpsStrategy;
	}
	public void setFpsStrategy(String fpsStrategy) {
		this.fpsStrategy = fpsStrategy;
	}
	public String getFpsEncode() {
		return fpsEncode;
	}
	public void setFpsEncode(String fpsEncode) {
		this.fpsEncode = fpsEncode;
	}
	public String getFpsDecode() {
		return fpsDecode;
	}
	public void setFpsDecode(String fpsDecode) {
		this.fpsDecode = fpsDecode;
	}
	public String getFpsRender() {
		return fpsRender;
	}
	public void setFpsRender(String fpsRender) {
		this.fpsRender = fpsRender;
	}
	public String getBpsStrategy() {
		return bpsStrategy;
	}
	public void setBpsStrategy(String bpsStrategy) {
		this.bpsStrategy = bpsStrategy;
	}
	public String getBpsEncode() {
		return bpsEncode;
	}
	public void setBpsEncode(String bpsEncode) {
		this.bpsEncode = bpsEncode;
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
	public String getBpsDecode() {
		return bpsDecode;
	}
	public void setBpsDecode(String bpsDecode) {
		this.bpsDecode = bpsDecode;
	}
	public String getLossRateSend() {
		return lossRateSend;
	}
	public void setLossRateSend(String lossRateSend) {
		this.lossRateSend = lossRateSend;
	}
	public String getLossRateReceive() {
		return lossRateReceive;
	}
	public void setLossRateReceive(String lossRateReceive) {
		this.lossRateReceive = lossRateReceive;
	}
	public String getRtt() {
		return rtt;
	}
	public void setRtt(String rtt) {
		this.rtt = rtt;
	}
	public String getJitterSend() {
		return jitterSend;
	}
	public void setJitterSend(String jitterSend) {
		this.jitterSend = jitterSend;
	}
	public String getJitterReceive() {
		return jitterReceive;
	}
	public void setJitterReceive(String jitterReceive) {
		this.jitterReceive = jitterReceive;
	}
	public String getStrategyTrend() {
		return strategyTrend;
	}
	public void setStrategyTrend(String strategyTrend) {
		this.strategyTrend = strategyTrend;
	}
	public String getStrategyDetect() {
		return strategyDetect;
	}
	public void setStrategyDetect(String strategyDetect) {
		this.strategyDetect = strategyDetect;
	}
	public String getWidthCapture() {
		return widthCapture;
	}
	public void setWidthCapture(String widthCapture) {
		this.widthCapture = widthCapture;
	}
	public String getWidthStrategy() {
		return widthStrategy;
	}
	public void setWidthStrategy(String widthStrategy) {
		this.widthStrategy = widthStrategy;
	}
	public String getWidthEncode() {
		return widthEncode;
	}
	public void setWidthEncode(String widthEncode) {
		this.widthEncode = widthEncode;
	}
	public String getWidthDecode() {
		return widthDecode;
	}
	public void setWidthDecode(String widthDecode) {
		this.widthDecode = widthDecode;
	}
	public String getWidthRender() {
		return widthRender;
	}
	public void setWidthRender(String widthRender) {
		this.widthRender = widthRender;
	}
	public String getHeightCapture() {
		return heightCapture;
	}
	public void setHeightCapture(String heightCapture) {
		this.heightCapture = heightCapture;
	}
	public String getHeightStrategy() {
		return heightStrategy;
	}
	public void setHeightStrategy(String heightStrategy) {
		this.heightStrategy = heightStrategy;
	}
	public String getHeightEncode() {
		return heightEncode;
	}
	public void setHeightEncode(String heightEncode) {
		this.heightEncode = heightEncode;
	}
	public String getHeightDecode() {
		return heightDecode;
	}
	public void setHeightDecode(String heightDecode) {
		this.heightDecode = heightDecode;
	}
	public String getHeightRender() {
		return heightRender;
	}
	public void setHeightRender(String heightRender) {
		this.heightRender = heightRender;
	}
    
	public void setVideoBiInfo(EngineSdkVideoBiInfo videoinfo) {
		setFpsCapture(videoinfo.getFpsCapture());
		setFpsStrategy(videoinfo.getFpsStrategy());
		setFpsEncode(videoinfo.getFpsEncode());
		setFpsDecode(videoinfo.getFpsDecode());
		setFpsRender(videoinfo.getFpsRender());
		setBpsStrategy(videoinfo.getBpsStrategy());
		setBpsEncode(videoinfo.getBpsEncode());
		setBpsSend(videoinfo.getBpsSend());
		setBpsReceive(videoinfo.getBpsReceive());
		setBpsDecode(videoinfo.getBpsDecode());
		setLossRateSend(videoinfo.getLossRateSend());
		setLossRateReceive(videoinfo.getLossRateReceive());
		setRtt(videoinfo.getRtt());
		setJitterSend(videoinfo.getJitterSend());
		setJitterReceive(videoinfo.getJitterReceive());
		setStrategyTrend(videoinfo.getStrategyTrend());
		setStrategyDetect(videoinfo.getStrategyDetect());
		setWidthCapture(videoinfo.getWidthCapture());
		setWidthStrategy(videoinfo.getWidthStrategy());
		setWidthEncode(videoinfo.getWidthEncode());
		setWidthDecode(videoinfo.getWidthDecode());
		setWidthRender(videoinfo.getWidthRender());
		setHeightCapture(videoinfo.getHeightCapture());
		setHeightStrategy(videoinfo.getHeightStrategy());
		setHeightEncode(videoinfo.getHeightEncode());
		setHeightDecode(videoinfo.getHeightDecode());
		setHeightRender(videoinfo.getHeightRender());
	}
}
