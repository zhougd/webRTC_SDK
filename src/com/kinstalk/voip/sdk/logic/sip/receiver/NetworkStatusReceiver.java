package com.kinstalk.voip.sdk.logic.sip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.kinstalk.voip.sdk.EngineSdkNetworkType;
import com.kinstalk.voip.sdk.logic.sip.service.SipService;
import com.kinstalk.voip.sdk.common.Log;

public class NetworkStatusReceiver extends BroadcastReceiver
{
	public static final String TAG = "NetworkStatusReceiver";
	private final SipService mService;

	public NetworkStatusReceiver(SipService service)
	{
		mService = service;
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
		{
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
			if (info != null && info.isConnected())
			{
				switch (info.getType())
				{
					case ConnectivityManager.TYPE_ETHERNET:
						mService.getLoader().setNetworkType(EngineSdkNetworkType.ES_NETWORK_TYPE_ADSL);
						break;
					case ConnectivityManager.TYPE_MOBILE:
						switch (info.getSubtype())
						{
							case TelephonyManager.NETWORK_TYPE_1xRTT:// 2G ~ 50-100 kbps
							case TelephonyManager.NETWORK_TYPE_CDMA:// 2G ~ 14-64 kbps
							case TelephonyManager.NETWORK_TYPE_EDGE:// 2G ~ 50-100 kbps
							case TelephonyManager.NETWORK_TYPE_GPRS:// 2G ~ 100 kbps
								mService.getLoader().setNetworkType(EngineSdkNetworkType.ES_NETWORK_TYPE_GPRS);
								break;
							case TelephonyManager.NETWORK_TYPE_EVDO_0:// 3G ~ 400-1000 kbps
							case TelephonyManager.NETWORK_TYPE_EVDO_A:// 3G ~ 600-1400 kbps
							case TelephonyManager.NETWORK_TYPE_EVDO_B:// 3G ~ 600-1400 kbps
								mService.getLoader().setNetworkType(EngineSdkNetworkType.ES_NETWORK_TYPE_CHINA_TELECOM_3G);
								break;
							case TelephonyManager.NETWORK_TYPE_HSDPA:// 3G ~ 2-14 Mbps
								mService.getLoader().setNetworkType(EngineSdkNetworkType.ES_NETWORK_TYPE_CHINA_MOBILE_3G);
								break;
							case TelephonyManager.NETWORK_TYPE_LTE:// 4G ~ 2-14 Mbps
								mService.getLoader().setNetworkType(EngineSdkNetworkType.ES_NETWORK_TYPE_CHINA_MOBILE_4G);
								break;
							case TelephonyManager.NETWORK_TYPE_HSPA:// 3G ~ 700-1700 H kbps
							case TelephonyManager.NETWORK_TYPE_HSUPA:// 3G ~ 1-23 Mbps
							case TelephonyManager.NETWORK_TYPE_UMTS:
							case TelephonyManager.NETWORK_TYPE_HSPAP: // H+ 3G ~ 400-7000 kbps
								mService.getLoader().setNetworkType(EngineSdkNetworkType.ES_NETWORK_TYPE_CHINA_UNICOM_3G);
								break;
							default:
								mService.getLoader().setNetworkType(EngineSdkNetworkType.ES_NETWORK_TYPE_GPRS);
						}
						break;
					case ConnectivityManager.TYPE_WIFI:
					case ConnectivityManager.TYPE_WIMAX:
						mService.getLoader().setNetworkType(EngineSdkNetworkType.ES_NETWORK_TYPE_WIFI);
						break;
					default:
						mService.getLoader().setNetworkType(EngineSdkNetworkType.ES_NETWORK_TYPE_UNKNOWN);
				}
				 Log.d(TAG, "Network Changed to : Type="  + info.getType() + " TypeName = "+ info.getTypeName() + "; Subtype="  + info.getSubtype() + "; SubtypeName=" + info.getSubtypeName() + "; ExtraInfo=" + info.getExtraInfo() + "; Reason=" + info.getReason());
			}
			else
			{
				 Log.d(TAG, "Network Changed: Currently Unavailable! ");
			}
		}
	}
}
