package com.kinstalk.voip.sdk.logic.user.json;

import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class UserResetPwdJsonObject extends AbstractJsonObject
{
	private String token;
	private String randomCode;
	private String success_info;

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getRandomCode()
	{
		return randomCode;
	}

	public void setRandomCode(String randomCode)
	{
		this.randomCode = randomCode;
	}

	public String getSuccess_info()
	{
		return success_info;
	}

	public void setSuccess_info(String success_info)
	{
		this.success_info = success_info;
	}

}
