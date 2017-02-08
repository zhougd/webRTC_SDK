package com.kinstalk.voip.sdk.logic.sip;

public class SipConstants
{
	public static final String LOGIC_HOST = "sip";

	public static final class LogicPath
	{
		public static final String INIT_SDK = "/initsdk";
		public static final String DESTROY_ENGINE = "/destroyegine";
		public static final String SET_USER_ACCOUNT = "/setuseraccount";
		public static final String DELETE_USER_ACCOUNT = "/deluseraccount";
		public static final String SEND_MESSAGE = "/sendmessage";
		public static final String MAKE_CALL = "/makecall";
		public static final String ANSWER_CALL = "/answercall";
		public static final String END_CALL = "/endcall";
		public static final String SET_DOWN_TO_AUDIO = "/set_down_to_audio";
		public static final String SET_TRANSMITING_VIDEO_PAUSED = "/set_transmiting_video_paused";
		public static final String REQUEST_REMOTE_PAUSE_TRANSMITING_VIDEO = "/request_remote_pause_transmiting_video";
		public static final String SET_AUDIO_MODE = "/set_audio_mode";
		public static final String PAUSE_INCALL_OPERATE = "/pause_incall_operate";
		public static final String RESUME_INCALL_OPERATE = "/resume_incall_operate";
		public static final String SET_NOTIFICATIONVIEW = "/set_notificationview";
		public static final String PLAY_RINGTONE = "/play_ringtone";
		public static final String STOP_PLAYING = "/stop_playing";
		public static final String SET_INCALLING_MODE = "/set_incalling_mode";
		public static final String SET_HANDFREE = "/set_handfree";
		public static final String SET_EARPHONECONNECTED = "/set_earphoneconnected";
		public static final String SET_BLUETOOTHCONNECTED = "/set_bluetoothconnected";
		public static final String OPERATE_SYSTEMAUDIO = "/operate_systemaudio";
		public static final String ENTRY_INCALLSTATE = "/entry_incallstate";
		public static final String LEAVE_INCALLSTATE = "/leave_incallstate";
		public static final String SET_FORCED_RELAY_PATH = "/setforcedrelaypath";
	}

	public static final class LogicParam
	{
		public static final String CONTEXT = "context";
		public static final String USER_AGENT = "user_agent";

		// public static final String USER_ID = "user_id";
		// public static final String USER_ID = "user_id";

		public static final String USER_ID = "user_id";
		public static final String USER_PASSOWRD = "user_password";
		public static final String USER_DOMAIN = "user_domain";
		public static final String CUSTOMIZED_NOTIFICATION = "notification";
		public static final String INCALL_ACTIVITY_NAME = "incall_activity_name";
		public static final String RANDOMCALL_MODE = "randomcall_mode";
		public static final String SERVER_TYPE = "server_type";
		public static final String OUTGOINGCALL_RINGTONE_ID = "outgoingcall_ringtone_id";
		public static final String INCALLING_MODE = "incalling_mode";
		public static final String IS_HANDFREE_ON = "is_handfree_on";
		public static final String IS_EARPHONE_CONNECTEd = "is_earphone_connected";
		public static final String IS_BLUETOOTH_CONNECTED = "is_bluetooth_connected";

		public static final String TO = "to";
		public static final String GID = "gid";
		public static final String MESSAGE = "msg";
		public static final String MIME_TYPE = "mime_type";
		public static final String SENDER_MODULE = "sender_module";

		public static final String IS_AUDIO_ONLY = "is_audio_only";
		public static final String CALL_ID = "call_id";
		public static final String AUDIO_MODE = "audio_mode";

		public static final String GSLB_SERVER = "gslb_server";
		public static final String RELAY_SERVER = "relay_server";
		public static final String SIP_PROXY_SERVER = "sip_proxy_server";
		
		public static final String RINGTONE_TYPE = "ring_tone_type";
		public static final String FORCED_RELAY_PATH = "forced_relay_path";
		
		public static final String VIDEO_PAUSED = "video_paused";
		public static final String ALLOW_RESET_BY_PEER = "allow_reset_by_peer";
		public static final String DOWN_TO_AUDIO = "down_to_audio";
	}

	public static final class IntentAction
	{
		public static final String ON_MESSAGE = "com.kinstalk.voip.sdk.message";
		public static final String ON_MESSAGE_SENT_RESULT = "com.kinstalk.voip.sdk.message_sent_result";
	}
}
