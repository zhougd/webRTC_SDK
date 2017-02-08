package com.kinstalk.voip.sdk.logic.user.json;

import com.j256.ormlite.field.DatabaseField;
import com.kinstalk.voip.sdk.data.model.AbstractDataItem;
import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class UserDetailJsonObject extends AbstractJsonObject
{
	@DatabaseField(id = true, unique = true)
	private String userId;
	@DatabaseField
	private String mobileNo;
	@DatabaseField
	private String counryCode;
	@DatabaseField
	private String picUrl;
	@DatabaseField
	private String realName;
	@DatabaseField
	private int gender;
	@DatabaseField
	private String areaCode;
	@DatabaseField
	private String email;
	@DatabaseField
	private String country;
	@DatabaseField
	private String province;
	@DatabaseField
	private String city;
	@DatabaseField
	private String birthYear;
	@DatabaseField
	private String birthMonth;
	@DatabaseField
	private String birthDay;
	@DatabaseField
	private String age;
	@DatabaseField
	private String updateAt;
	@DatabaseField
	private String sign;
	@DatabaseField
	private String constellation;
	@DatabaseField
	private String maritalStatus;
	@DatabaseField
	private String job;
	@DatabaseField
	private String school;
	@DatabaseField
	private String company;
	@DatabaseField
	private String accomplishmentDegree;
	
	@DatabaseField
	private String mToken;

	public String getmToken() {
		return mToken;
	}

	public void setmToken(String mToken) {
		this.mToken = mToken;
	}

	public UserDetailJsonObject() {
	    super.setmTobePersisted();
	}
	
	@Override
	public <T extends AbstractDataItem> void postProcess(T objectForPostProcess) {
		UserDetailJsonObject o = (UserDetailJsonObject)objectForPostProcess;
		setmToken(o.getmToken());
	}
	
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

	public String getCounryCode()
	{
		return counryCode;
	}

	public void setCounryCode(String counryCode)
	{
		this.counryCode = counryCode;
	}

	public String getPicUrl()
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}

	public String getRealName()
	{
		return realName;
	}

	public void setRealName(String realName)
	{
		this.realName = realName;
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

	public String getBirthYear()
	{
		return birthYear;
	}

	public void setBirthYear(String birthYear)
	{
		this.birthYear = birthYear;
	}

	public String getBirthMonth()
	{
		return birthMonth;
	}

	public void setBirthMonth(String birthMonth)
	{
		this.birthMonth = birthMonth;
	}

	public String getBirthDay()
	{
		return birthDay;
	}

	public void setBirthDay(String birthDay)
	{
		this.birthDay = birthDay;
	}

	public String getAge()
	{
		return age;
	}

	public void setAge(String age)
	{
		this.age = age;
	}

	public String getUpdateAt()
	{
		return updateAt;
	}

	public void setUpdateAt(String updateAt)
	{
		this.updateAt = updateAt;
	}

	public String getSign()
	{
		return sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}

	public String getConstellation()
	{
		return constellation;
	}

	public void setConstellation(String constellation)
	{
		this.constellation = constellation;
	}

	public String getMaritalStatus()
	{
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus)
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

	public String getAccomplishmentDegree()
	{
		return accomplishmentDegree;
	}

	public void setAccomplishmentDegree(String accomplishmentDegree)
	{
		this.accomplishmentDegree = accomplishmentDegree;
	}
}
