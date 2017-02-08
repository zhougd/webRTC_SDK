package com.kinstalk.voip.sdk.logic.sip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kinstalk.voip.sdk.common.Log;

public class NetChangeReceiver extends BroadcastReceiver
{
	public static final String TAG = "NetChangeReceiver";
	@Override
	public void onReceive(Context context, Intent intent)
	{
		// 什么都不用做。。。只要收到通知，在App的onCreate里面就会启动SipServiceForPhone
		 Log.d(TAG, "Received action:" + intent.getAction());
	}

}