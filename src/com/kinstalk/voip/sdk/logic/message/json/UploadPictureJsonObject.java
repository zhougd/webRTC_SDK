package com.kinstalk.voip.sdk.logic.message.json;

import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class UploadPictureJsonObject extends AbstractJsonObject
{
	private String picUrl;
	private String accountId;
	private int status;
	private String tid;
	private String createAt;

	public String getPicUrl()
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getTid()
	{
		return tid;
	}

	public void setTid(String tid)
	{
		this.tid = tid;
	}

	public String getCreateAt()
	{
		return createAt;
	}

	public void setCreateAt(String createAt)
	{
		this.createAt = createAt;
	}

}
