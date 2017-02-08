package com.kinstalk.voip.sdk.logic.sip.receiver;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.kinstalk.voip.sdk.EngineSdkCallInformation;
import com.kinstalk.voip.sdk.EngineSdkCallState;
import com.kinstalk.voip.sdk.logic.sip.service.SipService;

public class PhoneStateReceiver extends PhoneStateListener
{
	private final SipService mSipService;

	public PhoneStateReceiver(SipService sipService)
	{
		mSipService = sipService;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber)
	{
		if (state != TelephonyManager.CALL_STATE_IDLE)
		{
			EngineSdkCallInformation call = mSipService.getLoader().getCurrentCallInfo();
			if (call != null && call.getCallState() != EngineSdkCallState.ES_STATE_ON_CALL_CALL_ENDED && call.getCallState() != EngineSdkCallState.ES_STATE_ON_CALL_REQUEST_CALLING_OUT)
			{
				mSipService.getLoader().endCall(call, "");
			}
		}
	}
}
