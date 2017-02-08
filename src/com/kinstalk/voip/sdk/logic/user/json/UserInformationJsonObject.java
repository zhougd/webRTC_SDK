package com.kinstalk.voip.sdk.logic.user.json;

import java.util.List;

import android.net.Uri;

import com.j256.ormlite.field.DatabaseField;
import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class UserInformationJsonObject extends AbstractJsonObject
{
	@DatabaseField(id=true, unique=true)
	private String mClassURI;
	@DatabaseField
	private String userId;
	@DatabaseField
	private String passport;
	private String userName;
	private String mobileNo;
	private String countryCode;
	private int shield;
	private int isBinded;
	private int isTempAccount;
	private int confirm;
	private String picUrl;
	private int isFirstLogin;
	private List<Sips> sips;
	private long currentTime;
	private int gender;
	private String domain;
	private int hasPwd;
	private int age;
	private String phoneAbility;
	
	public UserInformationJsonObject() {
		Uri uri = this.getUri();
		mClassURI = uri.toString();
		super.setmTobePersisted();
	}

	public static class Sips
	{
		public String url;

		public String getUrl()
		{
			return url;
		}

		public void setUrl(String url)
		{
			this.url = url;
		}
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getPassport()
	{
		return passport;
	}

	public void setPassport(String passport)
	{
		this.passport = passport;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getMobileNo()
	{
		return mobileNo;
	}

	public void setMobileNo(String mobileNo)
	{
		this.mobileNo = mobileNo;
	}

	public String getCountryCode()
	{
		return countryCode;
	}

	public void setCountryCode(String countryCode)
	{
		this.countryCode = countryCode;
	}

	public int getShield()
	{
		return shield;
	}

	public void setShield(int shield)
	{
		this.shield = shield;
	}

	public int getIsBinded()
	{
		return isBinded;
	}

	public void setIsBinded(int isBinded)
	{
		this.isBinded = isBinded;
	}

	public int getIsTempAccount()
	{
		return isTempAccount;
	}

	public void setIsTempAccount(int isTempAccount)
	{
		this.isTempAccount = isTempAccount;
	}

	public int getConfirm()
	{
		return confirm;
	}

	public void setConfirm(int confirm)
	{
		this.confirm = confirm;
	}

	public String getPicUrl()
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}

	public int getIsFirstLogin()
	{
		return isFirstLogin;
	}

	public void setIsFirstLogin(int isFirstLogin)
	{
		this.isFirstLogin = isFirstLogin;
	}

	public List<Sips> getSips()
	{
		return sips;
	}

	public void setSips(List<Sips> sips)
	{
		this.sips = sips;
	}

	public long getCurrentTime()
	{
		return currentTime;
	}

	public void setCurrentTime(long currentTime)
	{
		this.currentTime = currentTime;
	}

	public int getGender()
	{
		return gender;
	}

	public void setGender(int gender)
	{
		this.gender = gender;
	}

	public String getDomain()
	{
		return domain;
	}

	public void setDomain(String domain)
	{
		this.domain = domain;
	}

	public int getHasPwd()
	{
		return hasPwd;
	}

	public void setHasPwd(int hasPwd)
	{
		this.hasPwd = hasPwd;
	}

	public int getAge()
	{
		return age;
	}

	public void setAge(int age)
	{
		this.age = age;
	}

	public String getPhoneAbility()
	{
		return phoneAbility;
	}

	public void setPhoneAbility(String phoneAbility)
	{
		this.phoneAbility = phoneAbility;
	}
}
