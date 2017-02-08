package com.kinstalk.voip.sdk.logic.user.json;

import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class UserLogoutJsonObject extends AbstractJsonObject
{
	private String success_info;

	public String getSuccess_info()
	{
		return success_info;
	}

	public void setSuccess_info(String success_info)
	{
		this.success_info = success_info;
	}

}
