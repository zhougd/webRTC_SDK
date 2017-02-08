package com.kinstalk.voip.sdk.logic.contact.json;

import com.j256.ormlite.field.DatabaseField;
import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class MyFriendJsonObject extends AbstractJsonObject
{
	@DatabaseField
	private String constellation;
	private String birthMonth;
	@DatabaseField
	private String maritalStatus;
	@DatabaseField
	private String city;
	@DatabaseField
	private boolean inHisBlackList;
	@DatabaseField(id = true, unique = true)
	private String userId;
	@DatabaseField
	private String age;
	@DatabaseField
	private String province;
	@DatabaseField
	private String phoneValid;
	@DatabaseField
	private String pinyin;
	@DatabaseField
	private String birthYear;
	@DatabaseField
	private String picUrl;
	@DatabaseField
	private String areaCode;
	@DatabaseField
	private boolean inMyBlackList;
	@DatabaseField
	private String aliasPinyin;
	@DatabaseField
	private String relation;
	@DatabaseField
	private String job;
	@DatabaseField
	private String birthDay;
	@DatabaseField
	private String commonRelation;
	@DatabaseField
	private String mobileno;
	@DatabaseField
	private String country;
	@DatabaseField
	private String sign;
	@DatabaseField
	private String aliasName;
	@DatabaseField
	private boolean settingForStranger;
	@DatabaseField
	private String school;
	@DatabaseField
	private String email;
	@DatabaseField
	private String company;
	@DatabaseField
	private String realName;
	@DatabaseField
	private String shield;
	@DatabaseField(columnName = "MyFriendJsonObject" + "mobileNo" )
	private String mobileNo;
	@DatabaseField
	private long updateAt;
	@DatabaseField
	private int gender;
	@DatabaseField
	private int status;

	public MyFriendJsonObject() {
		super.setmTobePersisted();
	}
	public String getConstellation()
	{
		return constellation;
	}

	public void setConstellation(String constellation)
	{
		this.constellation = constellation;
	}

	public String getBirthMonth()
	{
		return birthMonth;
	}

	public void setBirthMonth(String birthMonth)
	{
		this.birthMonth = birthMonth;
	}

	public String getMaritalStatus()
	{
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus)
	{
		this.maritalStatus = maritalStatus;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public boolean isInHisBlackList()
	{
		return inHisBlackList;
	}

	public void setInHisBlackList(boolean inHisBlackList)
	{
		this.inHisBlackList = inHisBlackList;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getAge()
	{
		return age;
	}

	public void setAge(String age)
	{
		this.age = age;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getPhoneValid()
	{
		return phoneValid;
	}

	public void setPhoneValid(String phoneValid)
	{
		this.phoneValid = phoneValid;
	}

	public String getPinyin()
	{
		return pinyin;
	}

	public void setPinyin(String pinyin)
	{
		this.pinyin = pinyin;
	}

	public String getBirthYear()
	{
		return birthYear;
	}

	public void setBirthYear(String birthYear)
	{
		this.birthYear = birthYear;
	}

	public String getPicUrl()
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}

	public String getAreaCode()
	{
		return areaCode;
	}

	public void setAreaCode(String areaCode)
	{
		this.areaCode = areaCode;
	}

	public boolean isInMyBlackList()
	{
		return inMyBlackList;
	}

	public void setInMyBlackList(boolean inMyBlackList)
	{
		this.inMyBlackList = inMyBlackList;
	}

	public String getAliasPinyin()
	{
		return aliasPinyin;
	}

	public void setAliasPinyin(String aliasPinyin)
	{
		this.aliasPinyin = aliasPinyin;
	}

	public String getRelation()
	{
		return relation;
	}

	public void setRelation(String relation)
	{
		this.relation = relation;
	}

	public String getJob()
	{
		return job;
	}

	public void setJob(String job)
	{
		this.job = job;
	}

	public String getBirthDay()
	{
		return birthDay;
	}

	public void setBirthDay(String birthDay)
	{
		this.birthDay = birthDay;
	}

	public String getCommonRelation()
	{
		return commonRelation;
	}

	public void setCommonRelation(String commonRelation)
	{
		this.commonRelation = commonRelation;
	}

	public String getMobileno()
	{
		return mobileno;
	}

	public void setMobileno(String mobileno)
	{
		this.mobileno = mobileno;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getSign()
	{
		return sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}

	public String getAliasName()
	{
		return aliasName;
	}

	public void setAliasName(String aliasName)
	{
		this.aliasName = aliasName;
	}

	public boolean isSettingForStranger()
	{
		return settingForStranger;
	}

	public void setSettingForStranger(boolean settingForStranger)
	{
		this.settingForStranger = settingForStranger;
	}

	public String getSchool()
	{
		return school;
	}

	public void setSchool(String school)
	{
		this.school = school;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getCompany()
	{
		return company;
	}

	public void setCompany(String company)
	{
		this.company = company;
	}

	public String getRealName()
	{
		return realName;
	}

	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public String getShield()
	{
		return shield;
	}

	public void setShield(String shield)
	{
		this.shield = shield;
	}

	public String getMobileNo()
	{
		return mobileNo;
	}

	public void setMobileNo(String mobileNo)
	{
		this.mobileNo = mobileNo;
	}

	public long getUpdateAt()
	{
		return updateAt;
	}

	public void setUpdateAt(long updateAt)
	{
		this.updateAt = updateAt;
	}

	public int getGender()
	{
		return gender;
	}

	public void setGender(int gender)
	{
		this.gender = gender;
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
