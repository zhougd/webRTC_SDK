package com.kinstalk.voip.sdk.logic.sip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kinstalk.voip.sdk.logic.sip.service.EngineLoader;

public class AlarmReceiver extends BroadcastReceiver
{
	public static String ACTION_SIP_KEEP_ALIVE = "com.kinstalk.voip.sdk.repeatingAlarm";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(ACTION_SIP_KEEP_ALIVE))
		{
			EngineLoader.getInstance().refreshCurrentAccount();
		}
	}
}
