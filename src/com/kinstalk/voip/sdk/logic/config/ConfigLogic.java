package com.kinstalk.voip.sdk.logic.config;

import java.net.URI;
import java.util.HashMap;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.api.WeaverService;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.logic.WeaverAbstractLogic;
import com.kinstalk.voip.sdk.logic.config.json.ConfigJsonObject;
import com.kinstalk.voip.sdk.http.WeaverBaseAPI;
import com.kinstalk.voip.sdk.http.WeaverHttpConnection;
import com.kinstalk.voip.sdk.http.WeaverHttpRequest;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;

public class ConfigLogic extends WeaverAbstractLogic
{
	private URI mConfigLogicUri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, ConfigConstants.LOGIC_HOST, null);

	public ConfigLogic(Context context)
	{
		super(context);
		 Log.d(getClass(), "ConfigLogicUri init~");

		WeaverService.getInstance().registerLogicHandler(mConfigLogicUri, this);
	}

	@Override
	public void doHandleRequest(WeaverRequest req)
	{
		 Log.d(getClass(), "ConfigLogic handle request:" + req.toString());
		String path = req.getURI().getPath();
		if (ConfigConstants.LogicPath.GET_CONFIG.equals(path))
		{
			getConfig(req);
		}

	}

	private final void getConfig(WeaverRequest req)
	{
		String keys = (String) req.getParameter(ConfigConstants.LogicParam.KEYS);
		try
		{
			handleResponse(req, configGetConfig(keys));
		}
		catch (NetworkErrorException e)
		{
			req.setResponse(WeaverConstants.RequestReturnCode.NETWORK_FAILURE, null);
		}

	}
	
	/**
	 * 获取配置信息
	 * 
	 * @param 要获取的配置项的键值
	 *            ，可以传入多个键值，用逗号(,)分割
	 * @return
	 */
	private static final ConfigJsonObject configGetConfig(String keys) throws NetworkErrorException
	{
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("key", keys);

		WeaverHttpRequest req = new WeaverHttpRequest();
		req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/config/getConfig.json?key=" + keys);
		req.setMethod("POST");

		WeaverHttpConnection.sendHttp(req);

		return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), ConfigJsonObject.class);
	}
}
