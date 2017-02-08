package com.kinstalk.voip.sdk.logic.sip.aidl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kinstalk.voip.sdk.EngineSdkCallConfiguration;
import com.kinstalk.voip.sdk.EngineSdkSessionDirection;
import com.kinstalk.voip.sdk.common.Log;

public class CallConfiguration implements Parcelable
{
	private String localAccountId = "";
	private String remoteAccountId = "";
	private String localGID = "";
	private String remoteGID = "";
	private boolean isAudioEnabled;
	private boolean isMainVideoEnabled;
	private boolean is2ndVideoEnabled;
	private boolean isDataEnabled;
	private long callToken;
	private EngineSdkSessionDirection callDirection;

	public EngineSdkSessionDirection getCallDirection()
	{
		return callDirection;
	}

	public void setCallDirection(EngineSdkSessionDirection callDirection)
	{
		this.callDirection = callDirection;
	}

	public CallConfiguration()
	{
	}

	public CallConfiguration(EngineSdkCallConfiguration esCallConfiguration)
	{
		localAccountId = esCallConfiguration.getLocalAccountId();
		remoteAccountId = esCallConfiguration.getRemoteAccountId();
		
		localGID = esCallConfiguration.getLocalDisplayName();
		remoteGID = esCallConfiguration.getRemoteDisplayName();
		Log.e("CallConfiguration", "JNI Local GID："+localGID);
		Log.e("CallConfiguration", "JNI Remote GID："+remoteGID);
		isAudioEnabled = esCallConfiguration.getAudioChannelEnabled();
		isMainVideoEnabled = esCallConfiguration.getMainVideoChannelEnabled();
		is2ndVideoEnabled = esCallConfiguration.getSecondVideoChannelEnabled();
		isDataEnabled = esCallConfiguration.getDataVideoChannelEnabled();
		callToken = esCallConfiguration.getCallLocalToken();
		callDirection = esCallConfiguration.getCallDirection();

	}

	public CallConfiguration(Parcel source)
	{
		localAccountId = source.readString();
		remoteAccountId = source.readString();
		localGID = source.readString();
		remoteGID = source.readString();
		isAudioEnabled = source.readByte() == 1 ? true : false;
		isMainVideoEnabled = source.readByte() == 1 ? true : false;
		is2ndVideoEnabled = source.readByte() == 1 ? true : false;
		isDataEnabled = source.readByte() == 1 ? true : false;
		callToken = source.readLong();
		callDirection = EngineSdkSessionDirection.swigToEnum(source.readInt());
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(localAccountId);
		dest.writeString(remoteAccountId);
		dest.writeString(localGID);
		dest.writeString(remoteGID);
		dest.writeByte(isAudioEnabled ? (byte) 1 : (byte) 0);
		dest.writeByte(isMainVideoEnabled ? (byte) 1 : (byte) 0);
		dest.writeByte(is2ndVideoEnabled ? (byte) 1 : (byte) 0);
		dest.writeByte(isDataEnabled ? (byte) 1 : (byte) 0);
		dest.writeLong(callToken);
		dest.writeInt(callDirection.swigValue());
	}

	public static final Parcelable.Creator<CallConfiguration> CREATOR = new Parcelable.Creator<CallConfiguration>()
	{
		@Override
		public CallConfiguration createFromParcel(Parcel source)
		{// 从Parcel中读取数据，返回person对象
			return new CallConfiguration(source);
		}

		@Override
		public CallConfiguration[] newArray(int size)
		{
			return new CallConfiguration[size];
		}
	};

	public boolean isAudioEnabled()
	{
		return isAudioEnabled;
	}

	public void setAudioEnabled(boolean isAudioEnabled)
	{
		this.isAudioEnabled = isAudioEnabled;
	}

	public boolean isMainVideoEnabled()
	{
		return isMainVideoEnabled;
	}

	public void setMainVideoEnabled(boolean isMainVideoEnabled)
	{
		this.isMainVideoEnabled = isMainVideoEnabled;
	}

	public boolean isIs2ndVideoEnabled()
	{
		return is2ndVideoEnabled;
	}

	public void setIs2ndVideoEnabled(boolean is2ndVideoEnabled)
	{
		this.is2ndVideoEnabled = is2ndVideoEnabled;
	}

	public boolean isDataEnabled()
	{
		return isDataEnabled;
	}

	public void setDataEnabled(boolean isDataEnabled)
	{
		this.isDataEnabled = isDataEnabled;
	}

	public String getLocalGID()
	{
		return localGID;
	}

	public void setLocalGID(String localgid)
	{
		this.localGID = localgid;
	}

	public String getRemoteGID()
	{
		return remoteGID;
	}

	public void setRemoteGID(String remoteGID)
	{
		this.remoteGID = remoteGID;
	}

	public String getLocalAccountId()
	{
		return localAccountId;
	}

	public void setLocalAccountId(String localAccountId)
	{
		this.localAccountId = localAccountId;
	}

	public String getRemoteAccountId()
	{
		return remoteAccountId;
	}

	public void setRemoteAccountId_EX(String remoteAccountId)
	{
		this.remoteAccountId = remoteAccountId;
	}
	
	public long getCallToken()
	{
		return callToken;
	}

	public void setCallToken(long mCallToken)
	{
		this.callToken = mCallToken;
	}
}
