package com.kinstalk.voip.sdk.logic.allrelaypath;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.api.WeaverConstants.WeaverServerType;
import com.kinstalk.voip.sdk.api.WeaverService;
import com.kinstalk.voip.sdk.interfaces.WeaverRequestListener;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.logic.WeaverAbstractLogic;
import com.kinstalk.voip.sdk.logic.allrelaypath.json.AllRelayPathJsonObject;
import com.kinstalk.voip.sdk.logic.config.json.ConfigJsonObject;
import com.kinstalk.voip.sdk.logic.contact.ContactLogic;
import com.kinstalk.voip.sdk.logic.contact.ContactLogic.GetContactList;
import com.kinstalk.voip.sdk.logic.contact.json.ContactListJsonObject;
import com.kinstalk.voip.sdk.logic.user.UserConstants;
import com.kinstalk.voip.sdk.http.WeaverBaseAPI;
import com.kinstalk.voip.sdk.http.WeaverHttpConnection;
import com.kinstalk.voip.sdk.http.WeaverHttpRequest;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;

public class AllRelayPathLogic extends WeaverAbstractLogic
{
	private URI mAllRelayPathLogicUri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, AllRelayPathConstants.LOGIC_HOST, null);
	private static List<String> mAllformalForamtRelayPathList = null;
	private static int mAccessedPathPositionIndex = 0;
	private static String TAG = "AllRelayPathLogic";

	public AllRelayPathLogic(Context context)
	{
		super(context);
		 Log.d(getClass(), "AllRelayPathLogicUri init~");

		WeaverService.getInstance().registerLogicHandler(mAllRelayPathLogicUri, this);
	}

	@Override
	public void doHandleRequest(WeaverRequest req)
	{
		 Log.d(getClass(), "AllRelayPathLogicUri handle request:" + req.toString());
		 req.run();
//		String path = req.getURI().getPath();
//		if (AllRelayPathConstants.LogicPath.GET_ALLRELAYPATH.equals(path))
//		{
//			conductGetAllRelayPath(req);
//		}

	}
	
	
	public static void produceFormalFormatRelayPathList(List<List<String>> allRelayPaths)
	{
		int relayPathCnt = allRelayPaths.size();
		mAllformalForamtRelayPathList = new ArrayList<String>();
		for (int i = 0; i < relayPathCnt; i++) {
			List<String> relayPathHopList = allRelayPaths.get(i);
			int relayPathHopCnt = relayPathHopList.size();
			String relayPath = "1;1" + ";";
			for (int j = 0; j < relayPathHopCnt; j++) {
				relayPath += relayPathHopList.get(j);
				if (j < (relayPathHopCnt - 1)) {
					relayPath += ",";
				} else {
					relayPath += ";";
				}
			}
			mAllformalForamtRelayPathList.add(relayPath);
			Log.i(TAG, "RelayPath[" + i + "]=" + relayPath);
		}
		mAccessedPathPositionIndex = 0;
	}
	
	public static String getNextRelayPath()
	{
		String path = null;
		if (mAccessedPathPositionIndex < mAllformalForamtRelayPathList.size())
		{
			path = mAllformalForamtRelayPathList.get(mAccessedPathPositionIndex);
			mAccessedPathPositionIndex++;
		}
		return path;
	}

	
	public static class GetAllRelayPath extends WeaverRequest {
		private static final String mMethodName = "/" + GetAllRelayPath.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<AllRelayPathJsonObject> mRetDataType = AllRelayPathJsonObject.class;
		AllRelayPathJsonObject mObject;
		
		public GetAllRelayPath(WeaverRequestListener listener)
		{
			super.setParam(mTargetURI, listener);
		}
		public static AllRelayPathJsonObject run(WeaverRequestListener listener) {
			WeaverService.getInstance().dispatchRequest(new AllRelayPathLogic.GetAllRelayPath(listener));
			return null;
		}
		@Override
		public void run() {
			AllRelayPathJsonObject response = null;
			try {
				response = getAllRelayPath();
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}
	}
	
	/**
	 * 获取配置信息
	 * 
	 * @param 要获取的配置项的键值
	 *            ，可以传入多个键值，用逗号(,)分割
	 * @return
	 */
	private static final AllRelayPathJsonObject getAllRelayPath() throws NetworkErrorException
	{
		WeaverHttpRequest req = new WeaverHttpRequest();
		String port = (WeaverBaseAPI.getENV() ==WeaverServerType.ES_QINJIAN_ONLINE_SERVER)?":8080":"80";
		req.setUrl("http://gslb" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com" + port + "/getRelayPath");
		//req.setUrl("http://gslb.dev.qinyouyue.com/getRelayPath");
		//req.setUrl("http://gslb.qinyouyue.com:8080/getRelayPath");
		req.setMethod("GET");

		WeaverHttpConnection.sendHttp(req);

		return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), AllRelayPathJsonObject.class);
	}
}
