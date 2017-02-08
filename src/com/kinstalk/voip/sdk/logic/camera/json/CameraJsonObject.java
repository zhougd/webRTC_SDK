package com.kinstalk.voip.sdk.logic.camera.json;

import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class CameraJsonObject extends AbstractJsonObject
{
	private String vodUrl;
	private String ipcName;
	private String previewUrl;
	private int isPublic;
	private String ipcId;
	private String ipcDesc;
	private String realtimeUrl;

	public String getVodUrl()
	{
		return vodUrl;
	}

	public void setVodUrl(String vodUrl)
	{
		this.vodUrl = vodUrl;
	}

	public String getIpcName()
	{
		return ipcName;
	}

	public void setIpcName(String ipcName)
	{
		this.ipcName = ipcName;
	}

	public String getPreviewUrl()
	{
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl)
	{
		this.previewUrl = previewUrl;
	}

	public int getIsPublic()
	{
		return isPublic;
	}

	public void setIsPublic(int isPublic)
	{
		this.isPublic = isPublic;
	}

	public String getIpcId()
	{
		return ipcId;
	}

	public void setIpcId(String ipcId)
	{
		this.ipcId = ipcId;
	}

	public String getIpcDesc()
	{
		return ipcDesc;
	}

	public void setIpcDesc(String ipcDesc)
	{
		this.ipcDesc = ipcDesc;
	}

	public String getRealtimeUrl()
	{
		return realtimeUrl;
	}

	public void setRealtimeUrl(String realtimeUrl)
	{
		this.realtimeUrl = realtimeUrl;
	}

}
