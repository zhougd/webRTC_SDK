package com.kinstalk.voip.sdk.logic.sip.delegate;

import com.kinstalk.voip.sdk.EngineSdkAccountInformation;
import com.kinstalk.voip.sdk.EngineSdkAccountState;
import com.kinstalk.voip.sdk.EngineSdkAudioChannelState;
import com.kinstalk.voip.sdk.EngineSdkBiException;
import com.kinstalk.voip.sdk.EngineSdkCallInformation;
import com.kinstalk.voip.sdk.EngineSdkCallState;
import com.kinstalk.voip.sdk.EngineSdkDataChannelState;
import com.kinstalk.voip.sdk.EngineSdkEngineInformation;
import com.kinstalk.voip.sdk.EngineSdkInSessionNetworkQualityIndication;
import com.kinstalk.voip.sdk.EngineSdkMsgSender;
import com.kinstalk.voip.sdk.EngineSdkVideoChannelState;
import com.kinstalk.voip.sdk.EngineSdkVideoPixelFormat;

/**
 * 空实现所有的接口方法，客户端使用的时候可以继承这个类，减少不必要的方法重写。
 * 
 * @author luolong1
 * 
 */
public class EngineListenerImpl implements EngineListener, CallDataListener
{
	@Override
	public void onMainVideoData(long callToken, long pointer, int width, int height, char rotation, EngineSdkVideoPixelFormat format)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onMainVideoReceiveStateChange(long callToken, EngineSdkVideoChannelState p_state)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onMainVideoTransmitStateChange(long callToken, EngineSdkVideoChannelState p_state)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void on2ndVideoData(long callToken, long pointer, int width, int height, char rotation, EngineSdkVideoPixelFormat format)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void on2ndVideoStateChange(long callToken, EngineSdkVideoChannelState p_state)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestPauseMainVideo(long callToken, boolean isPause, boolean canResetByPeer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onAudioReceiveStateChange(long callToken, EngineSdkAudioChannelState p_state)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onAudioTransmitStateChange(long callToken, EngineSdkAudioChannelState p_state)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onData(long callToken, byte[] pcDataBuf)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onDataStateChange(long callToken, EngineSdkDataChannelState p_state)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onBiException(long callToken, EngineSdkBiException bi_exception, int detail)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onMainVideoFilterSetResult(EngineSdkCallInformation p_call, boolean is_success, long filtertype)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onMainVideoRecvVideoFilter(EngineSdkCallInformation p_call, long filtertype)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onEngineStateChange(EngineSdkEngineInformation p_state)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onCallStateChange(EngineSdkCallInformation callInfo, EngineSdkCallState state)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onAccountStateChange(EngineSdkAccountInformation account, EngineSdkAccountState state)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onWaitWakeupTimeout(String wakeup_caller_num, String wakeup_callee_num, String last_waiting_sip_callID)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessageSentResult(int msgId, String msgGlobalId, String msgSentTime, String remote_number, boolean isSuccess, String msgContent, String im_mime_type, String reason,
			EngineSdkMsgSender senderModule)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(int msgId, String msgGlobalId, String sentTime, String sender, String msgContent, String mimeType, EngineSdkMsgSender senderModule)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInfo(String infoContent)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMainVideoChannelWonderfulSaved(EngineSdkCallInformation arg0, String filePath)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallNetworkQualityChanged(EngineSdkInSessionNetworkQualityIndication networkQualityInd) {
		// TODO Auto-generated method stub
		
	}

}
