package com.kinstalk.voip.sdk.logic.user.json;

import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class Register4PhoneJsonObject extends AbstractJsonObject
{
	private String lpsUtgt;
	private String success_info;
	private String randomCode;
	private String mobileNo;

	public String getLpsUtgt()
	{
		return lpsUtgt;
	}

	public void setLpsUtgt(String lpsUtgt)
	{
		this.lpsUtgt = lpsUtgt;
	}

	public String getSuccess_info()
	{
		return success_info;
	}

	public void setSuccess_info(String success_info)
	{
		this.success_info = success_info;
	}

	public String getRandomCode()
	{
		return randomCode;
	}

	public void setRandomCode(String randomCode)
	{
		this.randomCode = randomCode;
	}

	public String getMobileNo()
	{
		return mobileNo;
	}

	public void setMobileNo(String mobileNo)
	{
		this.mobileNo = mobileNo;
	}

}
