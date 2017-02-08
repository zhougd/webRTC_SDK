package com.kinstalk.voip.sdk.logic.camera.json;

import java.util.List;

import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class CameraListJsonObject extends AbstractJsonObject
{
	private List<CameraJsonObject> result;

	public List<CameraJsonObject> getResult()
	{
		return result;
	}

	public void setResult(List<CameraJsonObject> result)
	{
		this.result = result;
	}
}
