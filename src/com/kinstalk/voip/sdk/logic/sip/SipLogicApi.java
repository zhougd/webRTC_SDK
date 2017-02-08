package com.kinstalk.voip.sdk.logic.sip;

import java.net.URI;

import android.app.Notification;
import android.content.Context;

import com.kinstalk.voip.sdk.EngineSdkMsgSender;
import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.api.WeaverService;
import com.kinstalk.voip.sdk.interfaces.WeaverRequestListener;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.logic.config.ConfigConstants;
import com.kinstalk.voip.sdk.logic.sip.delegate.CallDataListener;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;

public class SipLogicApi {
	private static final String TAG = "SipLogic API";
	/**
	 * <b>此请求在userLogout请求执行后会被自动调用，客户端可以不使用</b>
	 * 
	 * 删除当前的SIP帐户（SIP反注册/离线）
	 * 
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	@Deprecated
	public static WeaverRequest sipDeleteAccount(WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] sipDeleteAccount");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.DELETE_USER_ACCOUNT);
		WeaverRequest req = new WeaverRequest(uri, listener);

		return req;
	}
	
	public static WeaverRequest playRingtone(WeaverRequestListener listener, int ringtoneType)
	{
		 Log.d(TAG, "[WeaverAPI] playRingtone");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.PLAY_RINGTONE);
		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.RINGTONE_TYPE, ringtoneType);
		WeaverService.getInstance().dispatchRequest(req);
		return req;
	}
	
	public static WeaverRequest stopPlaying(WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] stopPlaying");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.STOP_PLAYING);
		WeaverRequest req = new WeaverRequest(uri, listener);
		WeaverService.getInstance().dispatchRequest(req);
		return req;
	}
	
	public static WeaverRequest setInCallingMode(WeaverRequestListener listener, int mode)
	{
		 Log.d(TAG, "[WeaverAPI] setInCallingMode");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.SET_INCALLING_MODE);
		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.INCALLING_MODE, mode);
		WeaverService.getInstance().dispatchRequest(req);
		return req;
	}
	
	public static WeaverRequest setHandfree(WeaverRequestListener listener, boolean isHandfreeOn)
	{
		 Log.d(TAG, "[WeaverAPI] setHandfree");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.SET_HANDFREE);
		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.IS_HANDFREE_ON, isHandfreeOn);
		WeaverService.getInstance().dispatchRequest(req);
		return req;
	}
	
	public static WeaverRequest setEarphoneConnected(WeaverRequestListener listener, boolean isEarphoneConnected)
	{
		 Log.d(TAG, "[WeaverAPI] setEarphoneConnected");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.SET_EARPHONECONNECTED);
		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.IS_EARPHONE_CONNECTEd, isEarphoneConnected);
		WeaverService.getInstance().dispatchRequest(req);
		return req;
	}
	
	public static WeaverRequest setBluetoothConnected(WeaverRequestListener listener, boolean isBluetoothConnected)
	{
		 Log.d(TAG, "[WeaverAPI] setBluetoothConnected");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.SET_BLUETOOTHCONNECTED);
		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.IS_BLUETOOTH_CONNECTED, isBluetoothConnected);
		WeaverService.getInstance().dispatchRequest(req);
		return req;
	}
	
	public static WeaverRequest operateSystemAudio(WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] operateSystemAudio");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.OPERATE_SYSTEMAUDIO);
		WeaverRequest req = new WeaverRequest(uri, listener);
		WeaverService.getInstance().dispatchRequest(req);
		return req;
	}


	/**
	 * 结束音视频通话
	 * 
	 * @param callId
	 *            通话id
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest sipEndCall(int callId, WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] sipEndCall");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.END_CALL);

		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.CALL_ID, callId);

		return req;
	}

	/**
	 * <b>此请求在userLogin请求执行后会被自动调用，客户端可以不使用</b>
	 * 
	 * 生成SIP模块初始化的请求。
	 * 
	 * @param context
	 *            应用程序的Context对象
	 * @param userAgent
	 *            用户UA字符串
	 * @param inCallActivityName
	 *            通话使用的InCallActivity的完整类名，由客户端实现这个类
	 * @param customNotification
	 *            通话中使用的Notification对象，由客户端实现
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest sipInit(Context context, String userAgent, String inCallActivityName, Notification customNotification, int outGoingCallRingtoneId, WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] sipInit");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.INIT_SDK);
		WeaverRequest req = new WeaverRequest(uri, listener);

		if (userAgent == null)
		{
		    userAgent = com.kinstalk.voip.sdk.common.StringUtility.getHttpUserAgent(context, "");
		}
		req.addParameter(SipConstants.LogicParam.CONTEXT, context);
		req.addParameter(SipConstants.LogicParam.USER_AGENT, userAgent);
		req.addParameter(SipConstants.LogicParam.INCALL_ACTIVITY_NAME, inCallActivityName);
		req.addParameter(SipConstants.LogicParam.CUSTOMIZED_NOTIFICATION, customNotification);
		req.addParameter(SipConstants.LogicParam.OUTGOINGCALL_RINGTONE_ID,outGoingCallRingtoneId);

		return req;
	}
	
	/**
	 * 
	 * 生成SIP模块释放的请求。
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest sipDestroy( WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] sipDestroy");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.DESTROY_ENGINE);
		WeaverRequest req = new WeaverRequest(uri, listener);
		return req;
	}
	
	/**
	 * <b>此请求用于单独设置notification</b>
	 * 
	 * @param customNotification
	 *            通话中使用的Notification对象，由客户端实现
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest sipSetNotificationView(Notification customNotification, WeaverRequestListener listener) 
	{
		 Log.d(TAG, "[WeaverAPI] sipSetNotificationView");
		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.SET_NOTIFICATIONVIEW);
		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.CUSTOMIZED_NOTIFICATION, customNotification);
		
		return req;
	}

	/**
	 * 发起音视频通话呼叫, 支持跨进程启动音视频通话
	 * 
	 * @param to
	 *            接收方id
	 * @param isAudioOnly
	 *            是否是仅音频通话,true=音频通话，false=音视频通话
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest sipMakeCall(String to, String gid, boolean isAudioOnly, WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] sipMakeCall");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.MAKE_CALL);

		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.TO, to);
		req.addParameter(SipConstants.LogicParam.GID, gid);
		req.addParameter(SipConstants.LogicParam.IS_AUDIO_ONLY, isAudioOnly);

		return req;
	}
	
	public static WeaverRequest sipAnswerCall(boolean isAudioOnly, WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] sipMakeCall");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.ANSWER_CALL);

		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.IS_AUDIO_ONLY, isAudioOnly);

		return req;
	}

	/**
	 * 发送文本消息
	 * 
	 * @param to
	 *            接收方id
	 * @param message
	 *            消息内容
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest sipSendMessage(String to, String message, WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] sipSendMessage");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.SEND_MESSAGE);
     
		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.TO, to);
		req.addParameter(SipConstants.LogicParam.MESSAGE, message);
		req.addParameter(SipConstants.LogicParam.MIME_TYPE, "text/html-fragment-1.0");
		req.addParameter(SipConstants.LogicParam.SENDER_MODULE, EngineSdkMsgSender.ES_MSG_SENDER_UI_DIALOG);

		return req;
	}
	
	/**
	 * 发送唤醒消息
	 * 
	 * @param to
	 *            接收方id
	 * @param message
	 *            消息内容
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest sipSendNotifiaction(String to, String message, WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] sipSendMessage");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.SEND_MESSAGE);
     
		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.TO, to);
		req.addParameter(SipConstants.LogicParam.MESSAGE, message);
		req.addParameter(SipConstants.LogicParam.MIME_TYPE, "application/vctl_notify+json");
		req.addParameter(SipConstants.LogicParam.SENDER_MODULE, EngineSdkMsgSender.ES_MSG_SENDER_UI_DIALOG);

		return req;
	}

	/**
	 * <b>此请求在userLogin请求执行后会被自动调用，客户端可以不使用</b>
	 * 
	 * 生成SIP帐户添加/注册的请求。
	 * 
	 * @param userId
	 *            用户的id
	 * @param userPassword
	 *            用户的密码
	 * @param userDomain
	 *            用户的域
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest sipSetAccount(String userId, String userPassword, String userDomain, WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] sipSetAccount");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.SET_USER_ACCOUNT);
		WeaverRequest req = new WeaverRequest(uri, listener);

		req.addParameter(SipConstants.LogicParam.USER_ID, userId);
		req.addParameter(SipConstants.LogicParam.USER_PASSOWRD, userPassword);
		req.addParameter(SipConstants.LogicParam.USER_DOMAIN, userDomain);

		return req;
	}


	
	/**
	 * 视频通话转音频通话
	 * 
	 * @param pause
	 *            true 转音频
	 *            fals 转视频
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest setDown2Audio(boolean pause, WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] setDown2Audio");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.SET_DOWN_TO_AUDIO);

		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.DOWN_TO_AUDIO, pause);

		return req;
	}
	
	/**
	 * 关闭开启本地视频传输
	 * 
	 * @param pause
	 *            true 关闭本地视频
	 *            fals 开启本地视频
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest setTransmitingVideoPaused(boolean pause, WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] setTransmitingVideoPaused");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.SET_TRANSMITING_VIDEO_PAUSED);

		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.VIDEO_PAUSED, pause);

		return req;
	}
	
	/**
	 * 请求远方关闭开启视频传输
	 * 
	 * @param pause
	 *            true 关闭本地视频
	 *            false 开启本地视频
	 * @param allowResetByPeer
	 *            true 允许对反操作
	 *            false 不允许对方操作
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest requestRemotePauseTransmitingVideo(boolean pause, boolean allowResetByPeer, WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] requestRemotePauseTransmitingVideo");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.REQUEST_REMOTE_PAUSE_TRANSMITING_VIDEO);

		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.VIDEO_PAUSED, pause);
		req.addParameter(SipConstants.LogicParam.ALLOW_RESET_BY_PEER, allowResetByPeer);
		return req;
	}
	
	/**
	 * 根据服务器的配置，设置音频默认模式
	 * 
	 * @param mode
	 *            音频模式
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest sipSetAudioMode(int audioMode, WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] sipSetAudioMode: mode=" + audioMode);

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.SET_AUDIO_MODE);
		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(SipConstants.LogicParam.AUDIO_MODE, audioMode);

		return req;
	}

	/**
	 * 获取配置信息。
	 * 
	 * @param key
	 *            要获取的配置项的键值，可以传入多个键值，用逗号(,)分割
	 * @param listener
	 *            如果需要监听此请求的执行结果，请注册监听器
	 * @return 生成的请求对象
	 */
	public static WeaverRequest configGetConfig(String keys, WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] configGetConfig");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, ConfigConstants.LOGIC_HOST, ConfigConstants.LogicPath.GET_CONFIG);
		WeaverRequest req = new WeaverRequest(uri, listener);
		req.addParameter(ConfigConstants.LogicParam.KEYS, keys);

		return req;
	}

	public static WeaverRequest entryInCallState(WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] entryInCallState");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.ENTRY_INCALLSTATE);
		WeaverRequest req = new WeaverRequest(uri, listener);
		WeaverService.getInstance().dispatchRequest(req);
		return req;
	}
	
	public static WeaverRequest leaveInCallState(WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] leaveInCallState");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.LEAVE_INCALLSTATE);
		WeaverRequest req = new WeaverRequest(uri, listener);
		WeaverService.getInstance().dispatchRequest(req);
		return req;
	}
	
	public static WeaverRequest sipPauseInCallOperate(WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] sipPauseInCallOperate");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.PAUSE_INCALL_OPERATE);
		WeaverRequest req = new WeaverRequest(uri, listener);

		return req;
	}

	public static WeaverRequest sipResumeInCallOperate(WeaverRequestListener listener)
	{
		 Log.d(TAG, "[WeaverAPI] sipResumeInCallOperate");

		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.RESUME_INCALL_OPERATE);
		WeaverRequest req = new WeaverRequest(uri, listener);

		return req;
	}
}
