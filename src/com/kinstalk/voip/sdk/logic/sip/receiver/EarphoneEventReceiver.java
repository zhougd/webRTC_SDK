package com.kinstalk.voip.sdk.logic.sip.receiver;

import android.bluetooth.BluetoothA2dp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kinstalk.voip.sdk.logic.sip.utility.MediaManager;
import com.kinstalk.voip.sdk.common.Compatibility;
import com.kinstalk.voip.sdk.common.Log;

public class EarphoneEventReceiver extends BroadcastReceiver
{
	public static final String TAG = "EarphoneEventReceiver";
	@Override
	public void onReceive(Context context, Intent intent)
	{
		int state = intent.getIntExtra("state", -1);
		 Log.d(TAG, "Earphone Events : state = " + state);
		if (state == 1)
		{
			try {
				MediaManager.getInstance().setEarphoneConnected(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (state == 0)
		{
			try {
				MediaManager.getInstance().setEarphoneConnected(false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.detectBluetoothState(context, intent);
	}

	private void detectBluetoothState(Context context, Intent intent) {
		 Log.d(TAG, "detectBluetoothState");
		String action = intent.getAction();
		if (action.equals(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)) {
			int state = -1;
			if (Compatibility.getAndroidSDKVersion() < 11) {
				state = intent.getIntExtra(
						"android.bluetooth.profile.extra.STATE", -1);
			} else {
				state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, -1);
			}
			switch (state) {
				case BluetoothA2dp.STATE_CONNECTED :
					 Log.d(TAG, "BluetoothA2dp.STATE_CONNECTED");
					try {
						MediaManager.getInstance().setBluetoothConnected(true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case BluetoothA2dp.STATE_DISCONNECTED :
					 Log.d(TAG, "BluetoothA2dp.STATE_DISCONNECTED");
					try {
						MediaManager.getInstance().setBluetoothConnected(false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default :
					 Log.d(TAG, "BluetoothA2dp, default state:");
					break;
			}
		}
	}
}
