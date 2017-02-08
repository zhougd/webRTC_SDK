package com.kinstalk.voip.sdk.logic.picwall;

import java.net.URI;
import java.util.HashMap;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.api.WeaverService;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.logic.WeaverAbstractLogic;
import com.kinstalk.voip.sdk.logic.picwall.json.PicWallListJsonObject;
import com.kinstalk.voip.sdk.http.WeaverBaseAPI;
import com.kinstalk.voip.sdk.http.WeaverHttpConnection;
import com.kinstalk.voip.sdk.http.WeaverHttpRequest;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;

public class PicWallLogic extends WeaverAbstractLogic
{
	private URI mPicWallLogicUri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, PicWallConstants.LOGIC_HOST, null);

	public PicWallLogic(Context context)
	{
		super(context);
		 Log.d(getClass(), "PicWallLogic init~");

		WeaverService.getInstance().registerLogicHandler(mPicWallLogicUri, this);
	}

	@Override
	public void doHandleRequest(WeaverRequest req)
	{
		 Log.d(getClass(), "PicWallLogic handle request:" + req.toString());
		String path = req.getURI().getPath();
		if (PicWallConstants.LogicPath.LIST.equals(path))
		{
			getList(req);
		}
		else
		{
			 Log.d(getClass(), "PicWallLogic request abondoned:" + req.toString());
		}
	}

	private void getList(WeaverRequest req)
	{
		String token = (String) req.getParameter(PicWallConstants.LogicParam.TOKEN);
		long updateAt = (Long) req.getParameter(PicWallConstants.LogicParam.UPDATE_AT);
		int firstNumber = (Integer) req.getParameter(PicWallConstants.LogicParam.FIRST_NUMBER);
		int countNumber = (Integer) req.getParameter(PicWallConstants.LogicParam.COUNT_NUMBER);

		try
		{
			handleResponse(req, picwallList(token, updateAt, firstNumber, countNumber));
		}
		catch (NetworkErrorException e)
		{
			req.setResponse(WeaverConstants.RequestReturnCode.NETWORK_FAILURE, null);
		}
	}
	
	private static final PicWallListJsonObject picwallList(String token, long updateAt, int start, int count) throws NetworkErrorException
	{
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("updateAt", String.valueOf(updateAt));
		params.put("firstNumber", String.valueOf(start));
		params.put("countNumber", String.valueOf(count));

		WeaverHttpRequest req = new WeaverHttpRequest();
		req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/userpicwall/list.json");
		req.setMethod("POST");
		req.setBody(StringUtility.hashmapToParamStringByte(params));
		WeaverHttpConnection.sendHttp(req);

		return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), PicWallListJsonObject.class);
	}
}
