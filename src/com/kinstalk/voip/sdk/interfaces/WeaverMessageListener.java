package com.kinstalk.voip.sdk.interfaces;

public interface WeaverMessageListener
{
	public void onMessage(int msgId, String msgGlobalId, final String sender, final String msgContent, String mimeType);
	
	public void onMessageSentResult(int msgId, String msgGlobalId, String msgSentTime, String remote_number, boolean isSuccess, String msgContent, String mimeType, String reason);
}
