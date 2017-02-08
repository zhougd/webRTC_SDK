package com.kinstalk.voip.sdk.logic.picwall.json;

import java.util.List;

import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class PicWallListJsonObject extends AbstractJsonObject
{
	private long updateAt;
	private long serverTime;
	private UserPicWall userPicWall;
	private int status;
	private List<UserPicWallItem> walls;

	public static class UserPicWall
	{
		private int id;
		private String userId;
		private String status;
		private String onWall;
		private String createAt;

		public int getId()
		{
			return id;
		}

		public void setId(int id)
		{
			this.id = id;
		}

		public String getUserId()
		{
			return userId;
		}

		public void setUserId(String userId)
		{
			this.userId = userId;
		}

		public String getStatus()
		{
			return status;
		}

		public void setStatus(String status)
		{
			this.status = status;
		}

		public String getOnWall()
		{
			return onWall;
		}

		public void setOnWall(String onWall)
		{
			this.onWall = onWall;
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

	public static class UserPicWallItem
	{
		private String userId;
		private String mobileNo;
		private String countryCode;
		private String passport;
		private String password;
		private String origin;
		private long createAt;
		private String ip;
		private long updateAt;
		private boolean confirm;
		private int status;
		private long lasId;
		private String isBinded;
		private String errorCode;
		private String picUrl;
		private String shield;
		private String realName;
		private boolean isTempAccount;
		private int firstLogin;
		private String isFirstLogin;
		private String pinyin;
		private int gender;
		private String areaCode;
		private String email;
		private String country;
		private String province;
		private String city;
		private int birthYear;
		private int birthMonth;
		private int birthDay;
		private String sign;
		private int constellation;
		private int maritalStatus;
		private String job;
		private String school;
		private String company;
		private int age;

		public String getUserId()
		{
			return userId;
		}

		public void setUserId(String userId)
		{
			this.userId = userId;
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

		public String getPassport()
		{
			return passport;
		}

		public void setPassport(String passport)
		{
			this.passport = passport;
		}

		public String getPassword()
		{
			return password;
		}

		public void setPassword(String password)
		{
			this.password = password;
		}

		public String getOrigin()
		{
			return origin;
		}

		public void setOrigin(String origin)
		{
			this.origin = origin;
		}

		public long getCreateAt()
		{
			return createAt;
		}

		public void setCreateAt(long createAt)
		{
			this.createAt = createAt;
		}

		public String getIp()
		{
			return ip;
		}

		public void setIp(String ip)
		{
			this.ip = ip;
		}

		public long getUpdateAt()
		{
			return updateAt;
		}

		public void setUpdateAt(long updateAt)
		{
			this.updateAt = updateAt;
		}

		public boolean isConfirm()
		{
			return confirm;
		}

		public void setConfirm(boolean confirm)
		{
			this.confirm = confirm;
		}

		public int getStatus()
		{
			return status;
		}

		public void setStatus(int status)
		{
			this.status = status;
		}

		public long getLasId()
		{
			return lasId;
		}

		public void setLasId(long lasId)
		{
			this.lasId = lasId;
		}

		public String getIsBinded()
		{
			return isBinded;
		}

		public void setIsBinded(String isBinded)
		{
			this.isBinded = isBinded;
		}

		public String getErrorCode()
		{
			return errorCode;
		}

		public void setErrorCode(String errorCode)
		{
			this.errorCode = errorCode;
		}

		public String getPicUrl()
		{
			return picUrl;
		}

		public void setPicUrl(String picUrl)
		{
			this.picUrl = picUrl;
		}

		public String getShield()
		{
			return shield;
		}

		public void setShield(String shield)
		{
			this.shield = shield;
		}

		public String getRealName()
		{
			return realName;
		}

		public void setRealName(String realName)
		{
			this.realName = realName;
		}

		public boolean isTempAccount()
		{
			return isTempAccount;
		}

		public void setTempAccount(boolean isTempAccount)
		{
			this.isTempAccount = isTempAccount;
		}

		public int getFirstLogin()
		{
			return firstLogin;
		}

		public void setFirstLogin(int firstLogin)
		{
			this.firstLogin = firstLogin;
		}

		public String getIsFirstLogin()
		{
			return isFirstLogin;
		}

		public void setIsFirstLogin(String isFirstLogin)
		{
			this.isFirstLogin = isFirstLogin;
		}

		public String getPinyin()
		{
			return pinyin;
		}

		public void setPinyin(String pinyin)
		{
			this.pinyin = pinyin;
		}

		public int getGender()
		{
			return gender;
		}

		public void setGender(int gender)
		{
			this.gender = gender;
		}

		public String getAreaCode()
		{
			return areaCode;
		}

		public void setAreaCode(String areaCode)
		{
			this.areaCode = areaCode;
		}

		public String getEmail()
		{
			return email;
		}

		public void setEmail(String email)
		{
			this.email = email;
		}

		public String getCountry()
		{
			return country;
		}

		public void setCountry(String country)
		{
			this.country = country;
		}

		public String getProvince()
		{
			return province;
		}

		public void setProvince(String province)
		{
			this.province = province;
		}

		public String getCity()
		{
			return city;
		}

		public void setCity(String city)
		{
			this.city = city;
		}

		public int getBirthYear()
		{
			return birthYear;
		}

		public void setBirthYear(int birthYear)
		{
			this.birthYear = birthYear;
		}

		public int getBirthMonth()
		{
			return birthMonth;
		}

		public void setBirthMonth(int birthMonth)
		{
			this.birthMonth = birthMonth;
		}

		public int getBirthDay()
		{
			return birthDay;
		}

		public void setBirthDay(int birthDay)
		{
			this.birthDay = birthDay;
		}

		public String getSign()
		{
			return sign;
		}

		public void setSign(String sign)
		{
			this.sign = sign;
		}

		public int getConstellation()
		{
			return constellation;
		}

		public void setConstellation(int constellation)
		{
			this.constellation = constellation;
		}

		public int getMaritalStatus()
		{
			return maritalStatus;
		}

		public void setMaritalStatus(int maritalStatus)
		{
			this.maritalStatus = maritalStatus;
		}

		public String getJob()
		{
			return job;
		}

		public void setJob(String job)
		{
			this.job = job;
		}

		public String getSchool()
		{
			return school;
		}

		public void setSchool(String school)
		{
			this.school = school;
		}

		public String getCompany()
		{
			return company;
		}

		public void setCompany(String company)
		{
			this.company = company;
		}

		public int getAge()
		{
			return age;
		}

		public void setAge(int age)
		{
			this.age = age;
		}
	}

	public long getUpdateAt()
	{
		return updateAt;
	}

	public void setUpdateAt(long updateAt)
	{
		this.updateAt = updateAt;
	}

	public long getServerTime()
	{
		return serverTime;
	}

	public void setServerTime(long serverTime)
	{
		this.serverTime = serverTime;
	}

	public UserPicWall getUserPicWall()
	{
		return userPicWall;
	}

	public void setUserPicWall(UserPicWall userPicWall)
	{
		this.userPicWall = userPicWall;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public List<UserPicWallItem> getWalls()
	{
		return walls;
	}

	public void setWalls(List<UserPicWallItem> walls)
	{
		this.walls = walls;
	}

}
