package com.kinstalk.voip.sdk.logic.sip.aidl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kinstalk.voip.sdk.EngineSdkEngineState;

public class EngineInfo implements Parcelable
{
	private EngineConfiguration engineConfiguration = new EngineConfiguration();
	private EngineSdkEngineState engineState;

	public EngineInfo()
	{

	}

	public EngineInfo(Parcel source)
	{
		engineConfiguration = source.readParcelable(EngineConfiguration.class.getClassLoader());
		engineState = EngineSdkEngineState.swigToEnum(source.readInt());
	}

	public EngineConfiguration getEngineConfiguration()
	{
		return engineConfiguration;
	}

	public void setEngineConfiguration(EngineConfiguration engineConfiguration)
	{
		this.engineConfiguration = engineConfiguration;
	}

	public EngineSdkEngineState getEngineState()
	{
		return engineState;
	}

	public void setEngineState(EngineSdkEngineState engineState)
	{
		this.engineState = engineState;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeParcelable(engineConfiguration, 0);
		dest.writeInt(engineState.swigValue());
	}

	public static final Parcelable.Creator<EngineInfo> CREATOR = new Parcelable.Creator<EngineInfo>()
	{
		@Override
		public EngineInfo createFromParcel(Parcel source)
		{// 从Parcel中读取数据，返回person对象
			return new EngineInfo(source);
		}

		@Override
		public EngineInfo[] newArray(int size)
		{
			return new EngineInfo[size];
		}
	};

}
