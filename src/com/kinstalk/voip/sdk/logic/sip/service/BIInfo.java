package com.kinstalk.voip.sdk.logic.sip.service;

import com.kinstalk.voip.sdk.EngineSdkCallInformation;
import com.kinstalk.voip.sdk.EngineSdkCallResultReport;

public class BIInfo {
	String callSessionId;
	String localAccountId;
	String remoteAccountId;
	BIResultReport callResultReport;
	
	public BIInfo() {
		callResultReport = new BIResultReport();
	}
	
	public String getCallSessionId() {
		return callSessionId;
	}
	public void setCallSessionId(String callSessionId) {
		this.callSessionId = callSessionId;
	}
	public String getLocalAccountId() {
		return localAccountId;
	}
	public void setLocalAccountId(String localAccountId) {
		this.localAccountId = localAccountId;
	}
	public String getRemoteAccountId() {
		return remoteAccountId;
	}
	public void setRemoteAccountId(String remoteAccountId) {
		this.remoteAccountId = remoteAccountId;
	}
	public BIResultReport getCallResultReport() {
		return callResultReport;
	}
	public void setCallResultReport(EngineSdkCallResultReport callResultReport) {
		this.callResultReport.setResultReport(callResultReport);
	}
	
	public void SetCallInfo(EngineSdkCallInformation callInfo) {
		this.setCallSessionId(callInfo.getCallSessionId());
		this.setLocalAccountId(callInfo.getCallConfiguration().getLocalAccountId());
		this.setRemoteAccountId(callInfo.getCallConfiguration().getRemoteAccountId());
		this.setCallResultReport(callInfo.getCallResultReport());
	}
};
