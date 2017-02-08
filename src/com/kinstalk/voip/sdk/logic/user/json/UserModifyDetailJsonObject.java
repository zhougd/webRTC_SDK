package com.kinstalk.voip.sdk.logic.user.json;

import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class UserModifyDetailJsonObject extends AbstractJsonObject
{
	private String userId;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}
}
