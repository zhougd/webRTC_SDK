package com.kinstalk.voip.sdk.logic.contact.json;

import com.j256.ormlite.field.DatabaseField;
import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class ContactJsonObject extends AbstractJsonObject
{
	@DatabaseField
	private long userUpdateAt;
	@DatabaseField
	private long id;
	@DatabaseField
	private long userId;
	@DatabaseField
	private String userMobile;
	@DatabaseField(id = true, unique = true)
	private long friendId;
	
	@DatabaseField(foreign=true, foreignAutoCreate=false, foreignAutoRefresh=false)
	private ContactListJsonObject mContactListJsonObject;
	
	@DatabaseField
	private String friendMobile;
	@DatabaseField
	private String aliasName;
	@DatabaseField
	private String aliasPinyin;
	@DatabaseField
	private String aliasPinyinAbbr;
	@DatabaseField
	private int deviceType;
	@DatabaseField
	private long createAt;
	@DatabaseField
	private long updateAt;
	@DatabaseField
	private String name;
	@DatabaseField
	private String pinyin;
	@DatabaseField
	private String pinyinAbbr;
	@DatabaseField
	private String picture;
	@DatabaseField
	private int commonRelation;
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


	ContactJsonObject() {
		super.setmTobePersisted();
	}
	public void SetContactListJsonObject(ContactListJsonObject contactListJsonObject) {
		mContactListJsonObject = contactListJsonObject;
	}

	public long getUserUpdateAt()
	{
		return userUpdateAt;
	}

	public void setUserUpdateAt(long userUpdateAt)
	{
		this.userUpdateAt = userUpdateAt;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public String getUserMobile()
	{
		return userMobile;
	}

	public void setUserMobile(String userMobile)
	{
		this.userMobile = userMobile;
	}

	public long getFriendId()
	{
		return friendId;
	}

	public void setFriendId(long friendId)
	{
		this.friendId = friendId;
	}

	public String getFriendMobile()
	{
		return friendMobile;
	}

	public void setFriendMobile(String friendMobile)
	{
		this.friendMobile = friendMobile;
	}

	public String getAliasName()
	{
		return aliasName;
	}

	public void setAliasName(String aliasName)
	{
		this.aliasName = aliasName;
	}

	public String getAliasPinyin()
	{
		return aliasPinyin;
	}

	public void setAliasPinyin(String aliasPinyin)
	{
		this.aliasPinyin = aliasPinyin;
	}

	public String getAliasPinyinAbbr()
	{
		return aliasPinyinAbbr;
	}

	public void setAliasPinyinAbbr(String aliasPinyinAbbr)
	{
		this.aliasPinyinAbbr = aliasPinyinAbbr;
	}

	public int getDeviceType()
	{
		return deviceType;
	}

	public void setDeviceType(int deviceType)
	{
		this.deviceType = deviceType;
	}

	public long getCreateAt()
	{
		return createAt;
	}

	public void setCreateAt(long createAt)
	{
		this.createAt = createAt;
	}

	public long getUpdateAt()
	{
		return updateAt;
	}

	public void setUpdateAt(long updateAt)
	{
		this.updateAt = updateAt;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPinyin()
	{
		return pinyin;
	}

	public void setPinyin(String pinyin)
	{
		this.pinyin = pinyin;
	}

	public String getPinyinAbbr()
	{
		return pinyinAbbr;
	}

	public void setPinyinAbbr(String pinyinAbbr)
	{
		this.pinyinAbbr = pinyinAbbr;
	}

	public String getPicture()
	{
		return picture;
	}

	public void setPicture(String picture)
	{
		this.picture = picture;
	}

	public int getCommonRelation()
	{
		return commonRelation;
	}

	public void setCommonRelation(int commonRelation)
	{
		this.commonRelation = commonRelation;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public String getSign()
	{
		return sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}

	public int getInviteResult()
	{
		return inviteResult;
	}

	public void setInviteResult(int inviteResult)
	{
		this.inviteResult = inviteResult;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getAge()
	{
		return age;
	}

	public void setAge(String age)
	{
		this.age = age;
	}
}
