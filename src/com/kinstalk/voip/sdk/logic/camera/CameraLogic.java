package com.kinstalk.voip.sdk.logic.camera;

import java.net.URI;
import java.util.HashMap;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.api.WeaverService;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.logic.WeaverAbstractLogic;
import com.kinstalk.voip.sdk.logic.camera.json.CameraListJsonObject;
import com.kinstalk.voip.sdk.http.WeaverBaseAPI;
import com.kinstalk.voip.sdk.http.WeaverHttpConnection;
import com.kinstalk.voip.sdk.http.WeaverHttpRequest;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;

public class CameraLogic extends WeaverAbstractLogic
{
	private URI mCameraLogicUri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, CameraConstants.LOGIC_HOST, null);

	public CameraLogic(Context context)
	{
		super(context);

		WeaverService.getInstance().registerLogicHandler(mCameraLogicUri, this);
	}

	@Override
	public void doHandleRequest(WeaverRequest req)
	{
		 Log.d(getClass(), "CameraLogic handle request:" + req.toString());
		String path = req.getURI().getPath();
		if (CameraConstants.LogicPath.GET_PUBLIC_LIST.equals(path))
		{
			getPublicList(req);
		}
	}
	
	private void getPublicList(WeaverRequest req)
	{
		int startPos = (Integer) req.getParameter(CameraConstants.LogicParam.START_POS);
		int range = (Integer) req.getParameter(CameraConstants.LogicParam.RANGE);
		try
		{
			req.setResponse(WeaverConstants.RequestReturnCode.OK, getPublicCameraList(startPos, range));
		}
		catch(NetworkErrorException e)
		{
			req.setResponse(WeaverConstants.RequestReturnCode.SERVER_RETURNED_ERROR, null);
		}
		
	}
	
	private static final CameraListJsonObject getPublicCameraList(int startPos, int range) throws NetworkErrorException
	{
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("start", String.valueOf(startPos));
		params.put("range", String.valueOf(range));

		WeaverHttpRequest req = new WeaverHttpRequest();
		req.setUrl("http://103.249.129.11/get_public_list/?" + StringUtility.hashmapToParamString(params));
		req.setMethod("GET");

		WeaverHttpConnection.sendHttp(req);

		return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), CameraListJsonObject.class);
	}
}
