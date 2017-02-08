package com.kinstalk.voip.sdk.logic.picwall;

import java.net.URI;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.interfaces.WeaverRequestListener;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;

public class PicWallLogicApi {
	private static final String TAG = "PicWallLogicApi";

	/**
	 * 获取随机视频请用户列表。
	 * 
	 * @param userToken
	 *            用户身份token，从登录获取
	 * @param start
	 *            列表开始的位置
	 * @param count
	 *            列表返回的用户数
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest picwallList(String token, long updateAt, int start, int count, WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] picwallList");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, PicWallConstants.LOGIC_HOST, PicWallConstants.LogicPath.LIST);

		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(PicWallConstants.LogicParam.UPDATE_AT, updateAt);
		req.addParameter(PicWallConstants.LogicParam.TOKEN, token);
		req.addParameter(PicWallConstants.LogicParam.FIRST_NUMBER, start);
		req.addParameter(PicWallConstants.LogicParam.COUNT_NUMBER, count);

		return req;
	}

}
