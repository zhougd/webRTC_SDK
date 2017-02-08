package com.kinstalk.voip.sdk.interfaces;

import com.kinstalk.voip.sdk.logic.sip.aidl.model.CallInfo;

public interface WeaverCallStateListener
{
	public void onCallStateChange(CallInfo call, int state);
}
