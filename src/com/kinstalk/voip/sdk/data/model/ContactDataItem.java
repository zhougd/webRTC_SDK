package com.kinstalk.voip.sdk.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.kinstalk.voip.sdk.logic.contact.json.ContactJsonObject;
import com.kinstalk.voip.sdk.logic.contact.json.MyFriendJsonObject;

public class ContactDataItem extends AbstractDataItem
{
	@DatabaseField(id = true, unique = true)
	private long userId;
	@DatabaseField
	private String userMobile;
	@DatabaseField
	private String aliasName;
	@DatabaseField
	private int deviceType;
	@DatabaseField
	private String name;
	@DatabaseField
	private String picture;
	@DatabaseField
	private String gender;
	@DatabaseField
	private String sign;
	@DatabaseField
	private int inviteResult;
	@DatabaseField
	private int status;
	@DatabaseField
	private String age;
	@DatabaseField
	private String myUserId;

	public static final ContactDataItem fromJsonObject(ContactJsonObject jsonItem)
	{
		if (jsonItem == null)
		{
			return null;
		}

		ContactDataItem result = new ContactDataItem();
		result.setUserId(jsonItem.getFriendId());
		result.setUserMobile(jsonItem.getFriendMobile());
		result.setAliasName(jsonItem.getAliasName());
		result.setDeviceType(jsonItem.getDeviceType());
		result.setName(jsonItem.getName());
		result.setPicture(jsonItem.getPicture());
		result.setGender(jsonItem.getGender());
		result.setSign(jsonItem.getSign());
		result.setInviteResult(jsonItem.getInviteResult());
		result.setStatus(jsonItem.getStatus());
		result.setAge(jsonItem.getAge());
		result.setMyUserId(jsonItem.getUserId() + "");

		return result;
	}

	public static final ContactDataItem fromJsonObject(MyFriendJsonObject jsonItem)
	{
		if (jsonItem == null)
		{
			return null;
		}

		ContactDataItem result = new ContactDataItem();
		result.setUserId(Long.parseLong(jsonItem.getUserId()));
		result.setUserMobile(jsonItem.getMobileno());
		result.setAliasName(jsonItem.getAliasName());
		result.setName(jsonItem.getRealName());
		result.setPicture(jsonItem.getPicUrl());
		result.setGender(String.valueOf(jsonItem.getGender()));
		result.setSign(jsonItem.getSign());
		result.setStatus(jsonItem.getStatus());
		result.setAge(jsonItem.getAge());
		result.setMyUserId(jsonItem.getUserId());

		return result;
	}

	public String getMyUserId() {
		return myUserId;
	}

	private void setMyUserId(String myUserId) {
		this.myUserId = myUserId;
	}
	
	public long getUserId() {
		return userId;
	}

	private void setUserId(long userId) {
		this.userId = userId;
	}
	
	public String getUserMobile()
	{
		return userMobile;
	}

	private void setUserMobile(String userMobile)
	{
		this.userMobile = userMobile;
	}

	public String getAliasName()
	{
		return aliasName;
	}

	private void setAliasName(String aliasName)
	{
		this.aliasName = aliasName;
	}

	public int getDeviceType()
	{
		return deviceType;
	}

	private void setDeviceType(int deviceType)
	{
		this.deviceType = deviceType;
	}

	public String getName()
	{
		return name;
	}

	private void setName(String name)
	{
		this.name = name;
	}

	public String getPicture()
	{
		return picture;
	}

	private void setPicture(String picture)
	{
		this.picture = picture;
	}

	public String getGender()
	{
		return gender;
	}

	private void setGender(String gender)
	{
		this.gender = gender;
	}

	public String getSign()
	{
		return sign;
	}

	private void setSign(String sign)
	{
		this.sign = sign;
	}

	public int getInviteResult()
	{
		return inviteResult;
	}

	private void setInviteResult(int inviteResult)
	{
		this.inviteResult = inviteResult;
	}

	public int getStatus()
	{
		return status;
	}

	private void setStatus(int status)
	{
		this.status = status;
	}

	public String getAge()
	{
		return age;
	}

	private void setAge(String age)
	{
		this.age = age;
	}
}
