package com.kinstalk.voip.sdk.logic.message;

public class MessageConstants
{
	public static final String LOGIC_HOST = "message";

	public static final class LogicPath
	{
		public static final String SEND_PIC_MSG = "/send_msg";
		public static final String SEND_AUDIO_MSG = "/send_audio";
		public static final String SEND_VIDEO_MSG = "/send_video";
		public static final String SEND_PICTURE_MSG = "/send_picture";
	}

	public static final class LogicParam
	{
		public static final String TO_ID = "to_id";
		public static final String TOKEN = "token";
		public static final String PIC = "pic";
		public static final String PIC_SIZE = "pic_size";
		public static final String RATIO = "ratio";
		public static final String CONTENT = "content";
		public static final String LONGITUDE = "longitude";
		public static final String LATITUDE = "latitude";
		public static final String MAP_DESC = "map_desc";
	}
}
