package com.kinstalk.voip.sdk.logic.sip.aidl;

import com.kinstalk.voip.sdk.logic.sip.aidl.model.EngineInfo;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.CallInfo;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.AccountInfo;

interface IEngineListener
{
	void onEngineStateChange(in EngineInfo engineInfo);

	void onCallStateChange(in CallInfo callInfo, int callState);

	void onAccountStateChange(in AccountInfo account, int accountState);

	void onNetworkQualtiyChange(int networkQualityIndication);

	void onMessage(int msgId, String msgGlobalId, String sender, String msgContent, String mimeType);

	void onMessageSentResult(int msgId, String msgGlobalId, String msgSentTime, String remote_number, boolean isSuccess, String msgContent, String im_mime_type);

	void onWaitWakeupTimeout(String wakeup_caller_num, String wakeup_callee_num, String last_waiting_sip_callID);

	void onInfo(String infoContent);
}
