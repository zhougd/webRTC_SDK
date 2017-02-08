package com.kinstalk.voip.sdk.logic.user.json;

import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.kinstalk.voip.sdk.http.AbstractJsonObject;
import com.kinstalk.voip.sdk.common.UserPreferences;

public class UserLoginJsonObject extends AbstractJsonObject
{
	@DatabaseField(id=true, unique=true)
	private String mClassURI;
	@DatabaseField
	private String token;
	private String expireIn;
	private long updateAt;
	private String phoneAbility;
	private List<Infos> infos;
	
	// Comment[001] beginning: The following member value are added for identifying the object from DB
	@DatabaseField
	String passport;
	@DatabaseField
	String password;
	@DatabaseField
	String appSource;//source	否	String	1.0	用来标识发起请求的应用。字符串，最大200个字符
	@DatabaseField
	String appKey;
	@DatabaseField
	String appSecret;
	@DatabaseField
	String userDomain;
	// Comment[001] ended:

	public UserLoginJsonObject() {
		mClassURI = this.getUri().toString();
		super.setmTobePersisted();
	}

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
		if (UserPreferences.getBool(UserPreferences.Key.IS_AUTO_REGISTER, true)) {
			return token;
		} else {
			return null;
		}
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

	public long getUpdateAt()
	{
		return updateAt;
	}

	public void setUpdateAt(long updateAt)
	{
		this.updateAt = updateAt;
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
	
	public String getUserId() {
		return passport;
	}

	public void setUserId(String passport) {
		this.passport = passport;
	}

	public String getPassword() {
		return password;
	}

	public String getAppKey() {
		return appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}
	public String getmClassURI() {
		return mClassURI;
	}

	public void setmClassURI(String mClassURI) {
		this.mClassURI = mClassURI;
	}
	public String getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(String userDomain) {
		this.userDomain = userDomain;
	}
	
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppSource() {
		return appSource;
	}

	public void setAppSource(String appSource) {
		this.appSource = appSource;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
}
