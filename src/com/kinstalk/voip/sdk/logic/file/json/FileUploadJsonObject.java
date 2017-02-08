package com.kinstalk.voip.sdk.logic.file.json;

import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class FileUploadJsonObject extends AbstractJsonObject
{
	private String randomCode;
	private String pinUrl;

	public String getRandomCode()
	{
		return randomCode;
	}

	public void setRandomCode(String randomCode)
	{
		this.randomCode = randomCode;
	}

	public String getPinUrl()
	{
		return pinUrl;
	}

	public void setPinUrl(String pinUrl)
	{
		this.pinUrl = pinUrl;
	}

}
