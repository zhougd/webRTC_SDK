package com.kinstalk.voip.sdk.logic.sip.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.kinstalk.voip.sdk.EngineSdkCallInformation;
import com.kinstalk.voip.sdk.EngineSdkNetworkType;
import com.kinstalk.voip.sdk.api.WeaverConstants.WeaverServerType;
import com.kinstalk.voip.sdk.http.WeaverBaseAPI;
import com.kinstalk.voip.sdk.http.WeaverHttpConnection;
import com.kinstalk.voip.sdk.http.WeaverHttpRequest;
import com.kinstalk.voip.sdk.common.Log;

public class BiReporter {
	private static List<String> msBiReportList = new ArrayList<String>();
	private static Thread msSendReportThread = null;

	public static synchronized void putCallBi(EngineSdkCallInformation callInfo) {
		String ret = createRelayFailureReport(callInfo);
		if (ret != null) {
			msBiReportList.add(ret);
		}
		if (msBiReportList.size() > 0 && msSendReportThread == null) {
			msSendReportThread = new Thread() {
				public void run() {
					sendReportProcess();
				}
			};
			msSendReportThread.start();
		}
	}

	private static synchronized int getListSize() {
		return msBiReportList.size();
	}

	private static synchronized String getListLocation(int location) {
		return msBiReportList.get(location);
	}

	private static synchronized void removeListLocation(int location) {
		msBiReportList.remove(location);
	}

	private static synchronized void setNullofSendReportThread() {
		msSendReportThread = null;
	}

	private static void sendReportProcess() {
		while (true) {
			if (getListSize() > 0) {
				String url = getListLocation(0);
				if (httpRequestSendByUrl(url) == true) {
					removeListLocation(0);
				}
			} else {
				break;
			}
		}
		setNullofSendReportThread();
		putCallBi(null); // In case a new even inserted in same time of running "break;" clause.
	}

	private static boolean httpRequestSendByUrl(String url) {
		WeaverHttpRequest req = new WeaverHttpRequest();
		req.setUrl(url);
		req.setMethod("GET");
		Log.d("BiReporter", "Start sending by url " + url);
		WeaverHttpConnection.sendHttp(req);
		Log.d("BiReporter", "End sending by url " + url);

		if (req.getResponseCode() == 200) {
			byte[] byteArray = req.getResponseData();
			if (byteArray != null && byteArray.length > 0) {
				String newlog;
				try {
					newlog = new String(byteArray, "UTF-8");
					Log.d("BiReporter", "[ RESPONSE DATA ] "
							+ newlog);
					return true;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	private static String getNetworkType(EngineSdkCallInformation callInfo)
	{
		String netTypeTime = "";
		
		if(callInfo.getCallResultReport().getBiNetType3gChinaMobileInMs() > 0) {
			netTypeTime += EngineSdkNetworkType.ES_NETWORK_TYPE_CHINA_MOBILE_3G + "," + String.valueOf(callInfo.getCallResultReport().getBiNetType3gChinaMobileInMs());
		}
		if (callInfo.getCallResultReport().getBiNetType4gChinaMobileInMs() > 0) {
			netTypeTime += EngineSdkNetworkType.ES_NETWORK_TYPE_CHINA_MOBILE_4G + "," + String.valueOf(callInfo.getCallResultReport().getBiNetType4gChinaMobileInMs());
		}
		
		if (callInfo.getCallResultReport().getBiNetType3gChinaTelecomInMs() > 0) {
			netTypeTime += EngineSdkNetworkType.ES_NETWORK_TYPE_CHINA_TELECOM_3G + "," + String.valueOf(callInfo.getCallResultReport().getBiNetType3gChinaTelecomInMs());
		}
		if (callInfo.getCallResultReport().getBiNetType3gChinaUnicomInMs() > 0) {
			netTypeTime += EngineSdkNetworkType.ES_NETWORK_TYPE_CHINA_UNICOM_3G + "," + String.valueOf(callInfo.getCallResultReport().getBiNetType3gChinaUnicomInMs());
		}
		if (callInfo.getCallResultReport().getBiNetTypeAdslInMs() > 0) {
			netTypeTime += EngineSdkNetworkType.ES_NETWORK_TYPE_ADSL + "," + String.valueOf(callInfo.getCallResultReport().getBiNetTypeAdslInMs());
		}
		if (callInfo.getCallResultReport().getBiNetTypeFddiInMs() > 0) {
			netTypeTime += EngineSdkNetworkType.ES_NETWORK_TYPE_FDDI + "," + String.valueOf(callInfo.getCallResultReport().getBiNetTypeFddiInMs());
		}
		if (callInfo.getCallResultReport().getBiNetTypeGprsInMs() > 0) {
			netTypeTime += EngineSdkNetworkType.ES_NETWORK_TYPE_GPRS + "," + String.valueOf(callInfo.getCallResultReport().getBiNetTypeGprsInMs());
		}
		if (callInfo.getCallResultReport().getBiNetTypeWifiInMs() > 0) {
			netTypeTime += EngineSdkNetworkType.ES_NETWORK_TYPE_WIFI + "," + String.valueOf(callInfo.getCallResultReport().getBiNetTypeWifiInMs());
		}
		if (callInfo.getCallResultReport().getBiNetTypeUnkownInMs() > 0) {
			netTypeTime += EngineSdkNetworkType.ES_NETWORK_TYPE_UNKNOWN + "," + String.valueOf(callInfo.getCallResultReport().getBiNetTypeUnkownInMs());	
		}
		
		return netTypeTime;
	}
	
	private static String createRelayFailureReport(
			EngineSdkCallInformation callInfo) {
		String relayFailureReport = null;
		if (callInfo != null) {
			boolean audioSetupFailed = (callInfo.getCallResultReport()
					.getBiAudChnEnabled() == true && callInfo
					.getCallResultReport().getBiAudSetup() == false);
			boolean videoSetupFailed = (callInfo.getCallResultReport()
					.getBiVidChnEnabled() == true && callInfo
					.getCallResultReport().getBiVidSetup() == false);
			if (audioSetupFailed || videoSetupFailed) {
				String port = (WeaverBaseAPI.getENV() == WeaverServerType.ES_QINJIAN_ONLINE_SERVER)?":8080":"80";
				String url = "http://gslb" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com" + port + "/admin/bugmonitor/bug_report/?";
				url += "bug_name=media setup fail";
				url += "&bug_type=1";
				url += "&bug_contents="
						+ "[LocalId:" + String.valueOf(callInfo.getCallConfiguration().getLocalAccountId()) + "]"
						+ "[RemoteId:" + String.valueOf(callInfo.getCallConfiguration().getRemoteAccountId()) + "]"
				        + "[SessionID:" + String.valueOf(callInfo.getCallSessionId())  + "]"
				        + "[netTypeTime:" + getNetworkType(callInfo)  + "]"
				        + "[Audio setup failed:" + audioSetupFailed + "]"
				        + "[Video setup failed:" + videoSetupFailed + "]"
				        + "[Local public IP:" + longToIp(callInfo.getCallResultReport().getBiLocalPublicIP()) + "]"
				        + "[relay path=" + String.valueOf(callInfo.getCallResultReport().getBiRelayPath()) + "]";
				// url += "report_date=2015-06-19 10:24";
				url += "&report_date="
						+ Calendar.getInstance().get(Calendar.YEAR) + "-"
						+ (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-"
						+ Calendar.getInstance().get(Calendar.DATE) + " "
						+ Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
						+ ":" + Calendar.getInstance().get(Calendar.MINUTE);

				url = url.replace(";", "|");
				relayFailureReport = url;
			}
		}
		return relayFailureReport;
	}
	
	public static String longToIp(long ip) {
	    StringBuilder sb = new StringBuilder(15);

	    for (int i = 0; i < 4; i++) {
	        sb.insert(0, Long.toString(ip & 0xff));

	        if (i < 3) {
	            sb.insert(0, '.');
	        }

	        ip >>= 8;
	    }

	    return sb.toString();
	  }
}
