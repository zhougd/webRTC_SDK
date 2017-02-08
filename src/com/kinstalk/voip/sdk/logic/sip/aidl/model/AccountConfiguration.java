package com.kinstalk.voip.sdk.logic.sip.aidl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kinstalk.voip.sdk.EngineSdkAccountConfiguration;

public class AccountConfiguration implements Parcelable
{
	private String phoneNumber = "";
	private String appDomain = "";
	private int reportTimeout;
	private String accAuthResponseBody = "";
	private boolean isRandomCallMode = false;
	private String realm = "";// es_cred_info 的属性
	private String scheme = "";// es_cred_info 的属性
	private String username = "";// es_cred_info 的属性
	private String password = "";// es_cred_info 的属性
	private String credDigest = "";// es_cred_info 的属性

	public AccountConfiguration()
	{

	}

	public AccountConfiguration(EngineSdkAccountConfiguration conf)
	{
		phoneNumber = conf.getLocalAccountId();
		appDomain = conf.getApplicationDomain();
		reportTimeout = conf.getRegisterReportTimeoutMs();
		accAuthResponseBody = conf.getLocalAccountCredentialResponseBody();
	}

	public AccountConfiguration(Parcel source)
	{
		phoneNumber = source.readString();
		appDomain = source.readString();
		reportTimeout = source.readInt();
		accAuthResponseBody = source.readString();
		realm = source.readString();
		scheme = source.readString();
		username = source.readString();
		password = source.readString();
		credDigest = source.readString();
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public String getAppDomain()
	{
		return appDomain;
	}

	public void setAppDomain(String appDomain)
	{
		this.appDomain = appDomain;
	}

	public int getReportTimeOute()
	{
		return reportTimeout;
	}

	public void setReportTimeOute(int reportTimeOute)
	{
		this.reportTimeout = reportTimeOute;
	}

	public String getAccAuthResponseBody()
	{
		return accAuthResponseBody;
	}

	public void setAccAuthResponseBody(String accAuthResponseBody)
	{
		this.accAuthResponseBody = accAuthResponseBody;
	}

	public String getRealm()
	{
		return realm;
	}

	public void setRealm(String realm)
	{
		this.realm = realm;
	}

	public String getScheme()
	{
		return scheme;
	}

	public void setScheme(String scheme)
	{
		this.scheme = scheme;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getCredDigest()
	{
		return credDigest;
	}

	public void setCredDigest(String credDigest)
	{
		this.credDigest = credDigest;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(phoneNumber);
		dest.writeString(appDomain);
		dest.writeInt(reportTimeout);
		dest.writeString(accAuthResponseBody);
		dest.writeString(realm);
		dest.writeString(scheme);
		dest.writeString(username);
		dest.writeString(password);
		dest.writeString(credDigest);
	}

	public static final Parcelable.Creator<AccountConfiguration> CREATOR = new Parcelable.Creator<AccountConfiguration>()
	{
		@Override
		public AccountConfiguration createFromParcel(Parcel source)
		{// 从Parcel中读取数据，返回person对象
			return new AccountConfiguration(source);
		}

		@Override
		public AccountConfiguration[] newArray(int size)
		{
			return new AccountConfiguration[size];
		}
	};

	@Override
	public int describeContents()
	{
		return 0;
	}

	public boolean isRandomCallMode()
	{
		return isRandomCallMode;
	}

	public void setRandomCallMode(boolean isRandomCallMode)
	{
		this.isRandomCallMode = isRandomCallMode;
	}
}
