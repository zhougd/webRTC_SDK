package com.kinstalk.voip.sdk.logic.user.json;

import java.util.List;

import com.kinstalk.voip.sdk.logic.contact.json.ContactJsonObject;
import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class UserLoginBy3rdPartyJsonObject extends AbstractJsonObject
{
	private String token;
	private String expireIn;
	private String userId;
	private String passport;
	private String userName;
	private String mobileNo;
	private String countryCode;
	private int shield;
	private int isBinded;
	private String picUrl;
	private int isFirstLogin;
	private long currentTime;
	private int gender;
	private String domain;
	private int age;
	private String phoneAbility;
	private List<Infos> infos;
	private List<ContactJsonObject> contacts;

	private int total;
	private long updateAt;
	private long userUpdateAt;
	private long listUpdateAt;
	private long serverTime;

	private int status;

	public static class Infos
	{
		private int id;
		private int userId;
		private String listName;
		private String paramInfo;
		private long updateAt;
		private long userUpdateAt;
		private long listUpdateAt;

		public int getId()
		{
			return id;
		}

		public void setId(int id)
		{
			this.id = id;
		}

		public int getUserId()
		{
			return userId;
		}

		public void setUserId(int userId)
		{
			this.userId = userId;
		}

		public String getListName()
		{
			return listName;
		}

		public void setListName(String listName)
		{
			this.listName = listName;
		}

		public String getParamInfo()
		{
			return paramInfo;
		}

		public void setParamInfo(String paramInfo)
		{
			this.paramInfo = paramInfo;
		}

		public long getUpdateAt()
		{
			return updateAt;
		}

		public void setUpdateAt(long updateAt)
		{
			this.updateAt = updateAt;
		}

		public long getUserUpdateAt()
		{
			return userUpdateAt;
		}

		public void setUserUpdateAt(long userUpdateAt)
		{
			this.userUpdateAt = userUpdateAt;
		}

		public long getListUpdateAt()
		{
			return listUpdateAt;
		}

		public void setListUpdateAt(long listUpdateAt)
		{
			this.listUpdateAt = listUpdateAt;
		}
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getExpireIn()
	{
		return expireIn;
	}

	public void setExpireIn(String expireIn)
	{
		this.expireIn = expireIn;
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

	public List<Infos> getInfos()
	{
		return infos;
	}

	public void setInfos(List<Infos> infos)
	{
		this.infos = infos;
	}

	public List<ContactJsonObject> getContacts()
	{
		return contacts;
	}

	public void setContacts(List<ContactJsonObject> contacts)
	{
		this.contacts = contacts;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public long getUpdateAt()
	{
		return updateAt;
	}

	public void setUpdateAt(long updateAt)
	{
		this.updateAt = updateAt;
	}

	public long getUserUpdateAt()
	{
		return userUpdateAt;
	}

	public void setUserUpdateAt(long userUpdateAt)
	{
		this.userUpdateAt = userUpdateAt;
	}

	public long getListUpdateAt()
	{
		return listUpdateAt;
	}

	public void setListUpdateAt(long listUpdateAt)
	{
		this.listUpdateAt = listUpdateAt;
	}

	public long getServerTime()
	{
		return serverTime;
	}

	public void setServerTime(long serverTime)
	{
		this.serverTime = serverTime;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

}
