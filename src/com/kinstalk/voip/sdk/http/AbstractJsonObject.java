package com.kinstalk.voip.sdk.http;

import com.kinstalk.voip.sdk.data.model.AbstractDataItem;

public abstract class AbstractJsonObject extends AbstractDataItem
{
	private String error_code;
	private String error_info;

	public String getError_code()
	{
		return error_code;
	}

	public void setError_code(String error_code)
	{
		this.error_code = error_code;
	}

	public String getError_info()
	{
		return error_info;
	}

	public void setError_info(String error_info)
	{
		this.error_info = error_info;
	}
}
