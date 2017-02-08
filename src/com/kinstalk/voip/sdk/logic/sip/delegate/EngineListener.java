package com.kinstalk.voip.sdk.logic.sip.delegate;

import com.kinstalk.voip.sdk.EngineSdkAccountInformation;
import com.kinstalk.voip.sdk.EngineSdkAccountState;
import com.kinstalk.voip.sdk.EngineSdkCallInformation;
import com.kinstalk.voip.sdk.EngineSdkCallState;
import com.kinstalk.voip.sdk.EngineSdkEngineInformation;
import com.kinstalk.voip.sdk.EngineSdkMsgSender;

public interface EngineListener
{
	public void onEngineStateChange(EngineSdkEngineInformation p_state);

	public void onCallStateChange(EngineSdkCallInformation callInfo, EngineSdkCallState state);

	public void onAccountStateChange(EngineSdkAccountInformation account, EngineSdkAccountState state);

	public void onMessage(int msgId, String msgGlobalId, String sentTime, String sender, String msgContent, String mimeType, EngineSdkMsgSender senderModule);

	public void onMessageSentResult(int msgId, String msgGlobalId, String msgSentTime, String remote_number, boolean isSuccess, String msgContent, String im_mime_type, String reason,
			EngineSdkMsgSender senderModule);

	public void onWaitWakeupTimeout(String wakeup_caller_num, String wakeup_callee_num, String last_waiting_sip_callID);
}
