package com.kinstalk.voip.sdk.logic.sip.aidl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kinstalk.voip.sdk.EngineSdkAudioChannelState;
import com.kinstalk.voip.sdk.EngineSdkCallInformation;
import com.kinstalk.voip.sdk.EngineSdkCallState;
import com.kinstalk.voip.sdk.EngineSdkVideoChannelState;

public class CallInfo implements Parcelable
{
	private CallConfiguration mCallConfiguration = new CallConfiguration();
	private EngineSdkCallState callState;
	private int endReason;
	private String declineReason = "";
	private int callStartTime;
	private int callEndTime;
	private String sessionId;
	public EngineSdkAudioChannelState audioReceivingState;
	public EngineSdkAudioChannelState audioTransmitingState;
	public EngineSdkVideoChannelState mainVideoReceivingState;
	public EngineSdkVideoChannelState mainVideoTransmitingState;

	public CallInfo()
	{

	}

	public CallInfo(EngineSdkCallInformation esCallInfo)
	{
		mCallConfiguration = new CallConfiguration(esCallInfo.getCallConfiguration());
		callState = esCallInfo.getCallState();
		endReason = esCallInfo.getCallEndReason().swigValue();
		declineReason = esCallInfo.getCallDeclinedReason();
		callStartTime = esCallInfo.getCallStartTime();
		callEndTime = esCallInfo.getCallEndTime();
		sessionId = esCallInfo.getCallSessionId();
		audioReceivingState = esCallInfo.getAudioReceivingChannelState();
		audioTransmitingState = esCallInfo.getAudioTransmitingChannelState();
		mainVideoReceivingState = esCallInfo.getMainVideoReceivingChannelState();
		mainVideoTransmitingState = esCallInfo.getMainVideoTransmitingChannelState();
	}

	public CallConfiguration getCallConfiguration()
	{
		return mCallConfiguration;
	}

	public void setCallConfiguration(CallConfiguration mCallConfiguration)
	{
		this.mCallConfiguration = mCallConfiguration;
	}

	public EngineSdkCallState getCallState()
	{
		return callState;
	}

	public void setCallState(EngineSdkCallState callState)
	{
		this.callState = callState;
	}

	public int getEndReason()
	{
		return endReason;
	}

	public void setEndReason(int endReason)
	{
		this.endReason = endReason;
	}

	public String getDeclineReason()
	{
		return declineReason;
	}

	public void setDeclineReason(String declineReason)
	{
		this.declineReason = declineReason;
	}

	public int getCallStartTime()
	{
		return callStartTime;
	}

	public void setCallStartTime(int callStartTime)
	{
		this.callStartTime = callStartTime;
	}

	public int getCallEndTime()
	{
		return callEndTime;
	}

	public void setCallEndTime(int callEndTime)
	{
		this.callEndTime = callEndTime;
	}

	public CallInfo(Parcel source)
	{
		mCallConfiguration = source.readParcelable(CallConfiguration.class.getClassLoader());
		callState = EngineSdkCallState.swigToEnum(source.readInt());
		endReason = source.readInt();
		declineReason = source.readString();
		callStartTime = source.readInt();
		callEndTime = source.readInt();
		sessionId = source.readString();

		audioReceivingState = EngineSdkAudioChannelState.swigToEnum(source.readInt());
		audioTransmitingState = EngineSdkAudioChannelState.swigToEnum(source.readInt());
		mainVideoReceivingState = EngineSdkVideoChannelState.swigToEnum(source.readInt());
		mainVideoTransmitingState = EngineSdkVideoChannelState.swigToEnum(source.readInt());
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeParcelable(mCallConfiguration, 0);
		dest.writeInt(callState.swigValue());
		dest.writeInt(endReason);
		dest.writeString(declineReason);
		dest.writeInt(callStartTime);
		dest.writeInt(callEndTime);
		dest.writeString(sessionId);

		dest.writeInt(audioReceivingState.swigValue());
		dest.writeInt(audioTransmitingState.swigValue());
		dest.writeInt(mainVideoReceivingState.swigValue());
		dest.writeInt(mainVideoTransmitingState.swigValue());
	}

	public static final Parcelable.Creator<CallInfo> CREATOR = new Parcelable.Creator<CallInfo>()
	{
		@Override
		public CallInfo createFromParcel(Parcel source)
		{// 从Parcel中读取数据，返回person对象
			return new CallInfo(source);
		}

		@Override
		public CallInfo[] newArray(int size)
		{
			return new CallInfo[size];
		}
	};

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}
}
