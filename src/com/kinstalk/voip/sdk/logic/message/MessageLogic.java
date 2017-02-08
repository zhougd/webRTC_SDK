package com.kinstalk.voip.sdk.logic.message;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.api.WeaverService;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.logic.WeaverAbstractLogic;
import com.kinstalk.voip.sdk.logic.message.json.UploadPictureJsonObject;
import com.kinstalk.voip.sdk.http.WeaverBaseAPI;
import com.kinstalk.voip.sdk.http.WeaverHttpConnection;
import com.kinstalk.voip.sdk.http.WeaverHttpRequest;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;

public class MessageLogic extends WeaverAbstractLogic
{
	private URI mMessageLogicUri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, MessageConstants.LOGIC_HOST, null);
	//private final Context mContext;

	public MessageLogic(Context context)
	{
		super(context);
		 Log.d(getClass(), "UserLogic init~");
		//mContext = context;

		WeaverService.getInstance().registerLogicHandler(mMessageLogicUri, this);
	}

	@Override
	public void doHandleRequest(WeaverRequest req)
	{
		 Log.d(getClass(), "UserLogic handle request:" + req.toString());
		String path = req.getURI().getPath();
		if (MessageConstants.LogicPath.SEND_PIC_MSG.equals(path))
		{
			sendPictureMessage(req);
		}

	}

	private final void sendPictureMessage(WeaverRequest req)
	{
		String to = (String) req.getParameter(MessageConstants.LogicParam.TO_ID);
		String token = (String) req.getParameter(MessageConstants.LogicParam.TOKEN);
		File pic = (File) req.getParameter(MessageConstants.LogicParam.PIC);
		String ratio = (String) req.getParameter(MessageConstants.LogicParam.RATIO);
		String content = (String) req.getParameter(MessageConstants.LogicParam.CONTENT);
		double longitude = (Double) req.getParameter(MessageConstants.LogicParam.LONGITUDE);
		double latitude = (Double) req.getParameter(MessageConstants.LogicParam.LATITUDE);
		String mapDesc = (String) req.getParameter(MessageConstants.LogicParam.MAP_DESC);

		try
		{
			handleResponse(req, uploadPicture(token, "image", pic, ratio, content, to, longitude, latitude, mapDesc));
		}
		catch (NetworkErrorException e)
		{
			req.setResponse(WeaverConstants.RequestReturnCode.NETWORK_FAILURE, null);
		}
	}
	
	/**
	 * @param token
	 *            用户token
	 * @param type
	 *            type=0或者null 表示修改自己的头像， type = 1 表示上传动态图片消息
	 * @param pic
	 *            要上传的图片文件
	 * @param ratio
	 *            图片的长宽比：100_120
	 * @param content
	 *            文字信息
	 * @param longitude
	 *            视频地址, type=1时选填，经度
	 * @param latitude
	 *            视频地址, type=1时选填，纬度
	 * @param mapDesc
	 *            地图说明文字, type=1时选填
	 * @return
	 */
	private static final UploadPictureJsonObject uploadPicture(String token, String type, File pic, String ratio, String content, String toId, double longitude, double latitude, String mapDesc)
			throws NetworkErrorException
	{
		if (pic == null)
		{
			throw new IllegalArgumentException("file to be upload can not be null!");
		}

		WeaverHttpRequest req = new WeaverHttpRequest();
		req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/pic/UploadPicture.json");
		req.setMethod("POST");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("type", "3");
		params.put("ratio", ratio);
		params.put("content", content);
		params.put("friendId", toId);
		params.put("picSize", "0");
		params.put("longitude", String.valueOf(longitude));
		params.put("latitude", String.valueOf(latitude));
		params.put("mapDesc", mapDesc);

		List<File> files = new LinkedList<File>();
		files.add(pic);

		req.setBody(StringUtility.hashmapToMultipartFormDataByte(params, files, "image", "image/png"));
		WeaverHttpConnection.sendMultipartFormdata(req);

		return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), UploadPictureJsonObject.class);
	}
}
