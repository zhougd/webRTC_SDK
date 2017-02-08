package com.kinstalk.voip.sdk.logic.sip.delegate;


import com.kinstalk.voip.sdk.EngineSdkBiException;
import com.kinstalk.voip.sdk.EngineSdkBiExceptionCallback;
import com.kinstalk.voip.sdk.EngineSdkCallCallback;
import com.kinstalk.voip.sdk.EngineSdkCallInformation;
import com.kinstalk.voip.sdk.EngineSdkInSessionNetworkQualityIndication;
import com.kinstalk.voip.sdk.EngineSdkMediaAudioCallback;
import com.kinstalk.voip.sdk.EngineSdkMediaDataCallback;
import com.kinstalk.voip.sdk.EngineSdkMediaMainVideoCallback;
import com.kinstalk.voip.sdk.EngineSdkMediaSecondVideoCallback;
import com.kinstalk.voip.sdk.EngineSdkVideoPixelFormat;
import com.kinstalk.voip.sdk.common.Log;

public class CallDataReceiver
{
	public static final String TAG = "CallDataReceiver";
	private EngineSdkMediaMainVideoCallback mMainVideo;
	private EngineSdkMediaAudioCallback mAudio;
	private EngineSdkMediaDataCallback mData;
	private EngineSdkMediaSecondVideoCallback mSecondVideo;
	private EngineSdkBiExceptionCallback mBiException;
	private EngineSdkCallCallback mCall;

	private CallDataListener mListener;

	public CallDataReceiver()
	{
		mMainVideo = new EngineSdkMediaMainVideoCallback()
		{
			@Override
			public void onMainVideoChannelDataReceived(EngineSdkCallInformation callInfo, EngineSdkVideoPixelFormat pixelFormat, int frameWidth, int frameHeight, char frameRotation, byte[] dataBuffer)
			{
				super.onMainVideoChannelDataReceived(callInfo, pixelFormat, frameWidth, frameHeight, frameRotation, dataBuffer);
				// mListener.onMainVideoData(callInfo.getCallConfiguration().getCallLocalToken(),
				// pcDataBufPtr, w, h, rotation, p_format);
			}

			@Override
			public void onMainVideoChannelDataReceived(EngineSdkCallInformation callInfo, EngineSdkVideoPixelFormat pixelFormat, int frameWidth, int frameHeight, char frameRotation,
					long dataBufderPtr, int dataLen)
			{
				super.onMainVideoChannelDataReceived(callInfo, pixelFormat, frameWidth, frameHeight, frameRotation, dataBufderPtr, dataLen);
				if (mListener != null)
				{
					mListener.onMainVideoData(callInfo.getCallConfiguration().getCallLocalToken(), dataBufderPtr, frameWidth, frameHeight, frameRotation, pixelFormat);
				}
			}

			@Override
			public void onMainVideoTransmitingChannelStateChanged(EngineSdkCallInformation callInfo)
			{
				super.onMainVideoTransmitingChannelStateChanged(callInfo);
				Log.e("Base Call Data", "onMainVideoTransmitingChannelStateChanged: "+callInfo.getMainVideoTransmitingChannelState());
				if (mListener != null)
				{
					mListener.onMainVideoTransmitStateChange(callInfo.getCallConfiguration().getCallLocalToken(), callInfo.getMainVideoTransmitingChannelState());
				}
				
				
			}

			@Override
			public void onMainVideoReceivingChannelStateChanged(EngineSdkCallInformation callInfo)
			{
				super.onMainVideoReceivingChannelStateChanged(callInfo);
				Log.e("Base Call Data", "onMainVideoReceiveStateChange: "+callInfo.getMainVideoReceivingChannelState());
				if (mListener != null)
				{
					mListener.onMainVideoReceiveStateChange(callInfo.getCallConfiguration().getCallLocalToken(), callInfo.getMainVideoReceivingChannelState());
				}
				
				
			}

			@Override
			public void onMainVideoChannelRemoteRequestReceived(EngineSdkCallInformation callInfo, boolean tranmistingVideoPause, boolean allowResetByPeer)
			{
				super.onMainVideoChannelRemoteRequestReceived(callInfo, tranmistingVideoPause, allowResetByPeer);
				Log.e("Base Call Data", "onRequestPauseMainVideo: "+tranmistingVideoPause);
				if (mListener != null)
				{
					mListener.onRequestPauseMainVideo(callInfo.getCallConfiguration().getCallLocalToken(), tranmistingVideoPause, allowResetByPeer);
				}
				
				
			}

			@Override
			public void onMainVideoChannelSynchronizeVideoFilterResult(EngineSdkCallInformation callInfo, boolean isSuccessful, long filterType)
			{
				super.onMainVideoChannelSynchronizeVideoFilterResult(callInfo, isSuccessful, filterType);
				if (mListener != null)
				{
					callInfo = callInfo.clone();
					callInfo.swigTakeOwnership();
					mListener.onMainVideoFilterSetResult(callInfo, isSuccessful, filterType);
				}
			}

			@Override
			public void onMainVideoChannelVideoFilterIndicationReceivedFromRemote(EngineSdkCallInformation callInfo, long filterType)
			{
				super.onMainVideoChannelVideoFilterIndicationReceivedFromRemote(callInfo, filterType);
				if (mListener != null)
				{
					callInfo = callInfo.clone();
					callInfo.swigTakeOwnership();
					mListener.onMainVideoRecvVideoFilter(callInfo, filterType);
				}
			}
		};

		mAudio = new EngineSdkMediaAudioCallback()
		{
			@Override
			public void onAudioTransmitingChannelStateChange(EngineSdkCallInformation callInfo)
			{
				super.onAudioTransmitingChannelStateChange(callInfo);
				if (mListener != null)
				{
					mListener.onAudioTransmitStateChange(callInfo.getCallConfiguration().getCallLocalToken(), callInfo.getAudioTransmitingChannelState());
				}
			}

			@Override
			public void onAudioRecevingChannelStateChange(EngineSdkCallInformation callInfo)
			{
				super.onAudioRecevingChannelStateChange(callInfo);
				if (mListener != null)
				{
					mListener.onAudioReceiveStateChange(callInfo.getCallConfiguration().getCallLocalToken(), callInfo.getAudioReceivingChannelState());
				}
			}

		};

		mData = new EngineSdkMediaDataCallback()
		{
			@Override
			public void onSecondDataChannelDataReceived(EngineSdkCallInformation callInfo, byte[] dataBuffer)
			{
				super.onSecondDataChannelDataReceived(callInfo, dataBuffer);
				if (mListener != null)
				{
					mListener.onData(callInfo.getCallConfiguration().getCallLocalToken(), dataBuffer);
				}
			}

			@Override
			public void onDataChannelStateChanged(EngineSdkCallInformation callInfo)
			{
				super.onDataChannelStateChanged(callInfo);
				if (mListener != null)
				{
					mListener.onDataStateChange(callInfo.getCallConfiguration().getCallLocalToken(), callInfo.getDataChannelState());
				}
			}
		};

		mSecondVideo = new EngineSdkMediaSecondVideoCallback()
		{
			@Override
			public void onSecondVideoChannelDataReceived(EngineSdkCallInformation p_call, EngineSdkVideoPixelFormat p_format, int w, int h, char rotation, String pcDataBuf, int iDataLen)
			{
			}

			@Override
			public void onSecondVideoChannelStateChanged(EngineSdkCallInformation p_call)
			{
				super.onSecondVideoChannelStateChanged(p_call);
				if (mListener != null)
				{
					mListener.on2ndVideoStateChange(p_call.getCallConfiguration().getCallLocalToken(), p_call.getSecondVideoChannelState());
				}
			}
		};

		mBiException = new EngineSdkBiExceptionCallback()
		{
			@Override
			public void onThrowBiException(EngineSdkCallInformation callInfo, EngineSdkBiException biException, int detail)
			{
				super.onThrowBiException(callInfo, biException, detail);
				if (mListener != null)
				{
					mListener.onBiException(callInfo.getCallConfiguration().getCallLocalToken(), biException, detail);
				}
			}
		};

		mCall = new EngineSdkCallCallback()
		{
			public void onInfoReceived(String controlInfo)
			{
				super.onInfoReceived(controlInfo);
				if (mListener != null)
				{
					mListener.onInfo(controlInfo);
				}
			}

			public void onCallNetworkQualityChanged(EngineSdkInSessionNetworkQualityIndication networkQualityInd)
			{
				super.onCallNetworkQualityChanged(networkQualityInd);
				if (mListener != null) {
					mListener.onCallNetworkQualityChanged(networkQualityInd);
				}
			}

			public void onCallWaitForRemoteWakeupTimeout(String wakeupCallerNum, String wakeupCalleeNum, String lastWaitingSipCallId)
			{
				// TODO 增加客户端回调
			}

			@Override
			public void onMainVideoChannelWonderfulSaved(EngineSdkCallInformation callInfo, String filePath)
			{
				super.onMainVideoChannelWonderfulSaved(callInfo, filePath);
				if (mListener != null)
				{
					callInfo = callInfo.clone();
					callInfo.swigTakeOwnership();
					mListener.onMainVideoChannelWonderfulSaved(callInfo, filePath);
				}
			}
		};
	}

	public EngineSdkMediaMainVideoCallback getMainVideo()
	{
		return mMainVideo;
	}

	public EngineSdkMediaAudioCallback getAudio()
	{
		return mAudio;
	}

	public EngineSdkMediaDataCallback getData()
	{
		return mData;
	}

	public EngineSdkMediaSecondVideoCallback getSecondVideo()
	{
		return mSecondVideo;
	}

	public EngineSdkBiExceptionCallback getBiException()
	{
		return mBiException;
	}

	public EngineSdkCallCallback getCall()
	{
		return mCall;
	}

	public void setListener(CallDataListener listener)
	{
		 Log.d(TAG, "------------listener=" + listener.toString());
		mListener = listener;
	}
	
}
