package com.kinstalk.voip.sdk.logic.sip.aidl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kinstalk.voip.sdk.EngineSdkAccountInformation;
import com.kinstalk.voip.sdk.EngineSdkAccountState;
import com.kinstalk.voip.sdk.EngineSdkAccountUnregisterReason;

public class AccountInfo implements Parcelable
{
	private AccountConfiguration accountConfiguration = new AccountConfiguration();
	private EngineSdkAccountState accountState;
	private EngineSdkAccountUnregisterReason unRegReason;

	public AccountInfo()
	{

	}

	public AccountInfo(EngineSdkAccountInformation info)
	{
		this();
		accountState = info.getAccountState();
		unRegReason = info.getAccountUnregisterReason();
		accountConfiguration = new AccountConfiguration(info.getAccountConfiguration());
	}

	public AccountConfiguration getAccountConfiguration()
	{
		return accountConfiguration;
	}

	public void setAccountConfiguration(AccountConfiguration accountConfiguration)
	{
		this.accountConfiguration = accountConfiguration;
	}

	public EngineSdkAccountState getAccountState()
	{
		return accountState;
	}

	public void setAccountState(EngineSdkAccountState accountState)
	{
		this.accountState = accountState;
	}

	public EngineSdkAccountUnregisterReason getUnRegReason()
	{
		return unRegReason;
	}

	public void setUnRegReason(EngineSdkAccountUnregisterReason unRegReason)
	{
		this.unRegReason = unRegReason;
	}

	public AccountInfo(Parcel source)
	{
		accountConfiguration = source.readParcelable(AccountConfiguration.class.getClassLoader());
		accountState = EngineSdkAccountState.swigToEnum(source.readInt());
		unRegReason = EngineSdkAccountUnregisterReason.swigToEnum(source.readInt());
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeParcelable(accountConfiguration, 0);
		dest.writeInt(accountState.swigValue());
		dest.writeInt(unRegReason.swigValue());
	}

	public static final Parcelable.Creator<AccountInfo> CREATOR = new Parcelable.Creator<AccountInfo>()
	{
		@Override
		public AccountInfo createFromParcel(Parcel source)
		{// 从Parcel中读取数据，返回person对象
			return new AccountInfo(source);
		}

		@Override
		public AccountInfo[] newArray(int size)
		{
			return new AccountInfo[size];
		}
	};
}
