package com.kinstalk.voip.sdk.logic.file;

import java.net.URI;

import android.content.Context;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.api.WeaverService;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.logic.WeaverAbstractLogic;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;

public class FileLogic extends WeaverAbstractLogic
{
	private URI mFileLogicUri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, FileConstants.LOGIC_HOST, null);

	public FileLogic(Context context)
	{
		super(context);
		 Log.d(getClass(), "FileLogic init~");

		WeaverService.getInstance().registerLogicHandler(mFileLogicUri, this);
	}

	@Override
	public void doHandleRequest(WeaverRequest req)
	{
		 Log.d(getClass(), "UserLogic handle request:" + req.toString());
		String path = req.getURI().getPath();
		if (FileConstants.LogicPath.UPLOAD.equals(path))
		{
			upload(req);
		}
		else
		{
			 Log.d(getClass(), "UserLogic request abondoned:" + req.toString());
		}
	}

	private final void upload(WeaverRequest req)
	{

	}
}
