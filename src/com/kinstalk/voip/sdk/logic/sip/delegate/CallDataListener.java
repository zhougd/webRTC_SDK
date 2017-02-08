package com.kinstalk.voip.sdk.logic.sip.delegate;

import com.kinstalk.voip.sdk.EngineSdkAudioChannelState;
import com.kinstalk.voip.sdk.EngineSdkBiException;
import com.kinstalk.voip.sdk.EngineSdkCallInformation;
import com.kinstalk.voip.sdk.EngineSdkDataChannelState;
import com.kinstalk.voip.sdk.EngineSdkInSessionNetworkQualityIndication;
import com.kinstalk.voip.sdk.EngineSdkVideoChannelState;
import com.kinstalk.voip.sdk.EngineSdkVideoPixelFormat;

public interface CallDataListener
{
	/** 暂时只提供指针接口，数据接口暂时屏蔽 */
	// public abstract void onMainVideoData(long callToken, byte[] pcDataBuf, int width, int height,
	// char rotation, EnginePixelFormat format);

	public abstract void onCallNetworkQualityChanged(EngineSdkInSessionNetworkQualityIndication networkQualityInd);
	
	public abstract void onMainVideoData(long callToken, long pointer, int width, int height, char rotation, EngineSdkVideoPixelFormat format);

	public abstract void onMainVideoReceiveStateChange(long callToken, EngineSdkVideoChannelState p_state);

	public abstract void onMainVideoTransmitStateChange(long callToken, EngineSdkVideoChannelState p_state);

	public abstract void on2ndVideoData(long callToken, long pointer, int width, int height, char rotation, EngineSdkVideoPixelFormat format);

	public abstract void on2ndVideoStateChange(long callToken, EngineSdkVideoChannelState p_state);

	public abstract void onRequestPauseMainVideo(long callToken, boolean isPause, boolean canResetByPeer);

	/** 声音暂时还是由SDK处理，不触发该接口 */
	// public abstract void onAudioData(es_audio_channel_configuration p_conf, byte[] pcDataBuf);

	public abstract void onAudioReceiveStateChange(long callToken, EngineSdkAudioChannelState p_state);

	public abstract void onAudioTransmitStateChange(long callToken, EngineSdkAudioChannelState p_state);

	public abstract void onData(long callToken, byte[] pcDataBuf);

	public abstract void onDataStateChange(long callToken, EngineSdkDataChannelState p_state);

	public abstract void onBiException(long callToken, EngineSdkBiException bi_exception, int detail);

	public abstract void onMainVideoFilterSetResult(EngineSdkCallInformation p_call, boolean is_success, long filtertype);

	public abstract void onMainVideoRecvVideoFilter(EngineSdkCallInformation p_call, long filtertype);

	public abstract void onInfo(String infoContent);

	public abstract void onMainVideoChannelWonderfulSaved(EngineSdkCallInformation arg0, String filePath);
}
