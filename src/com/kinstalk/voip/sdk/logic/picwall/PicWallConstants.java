package com.kinstalk.voip.sdk.logic.picwall;

public class PicWallConstants
{
	public static final String LOGIC_HOST = "picwall";

	public static final class LogicPath
	{
		public static final String LIST = "/list";
	}

	public static final class LogicParam
	{
		public static final String TOKEN = "token";// server ticket 或devicesn
		public static final String UPDATE_AT = "update_At";// 时间戳，上次获取的时间戳，第一次为0
		public static final String FIRST_NUMBER = "first_number";// 从0开始计数，取历史记录的开始位置
		public static final String COUNT_NUMBER = "count_number";// 需要获取的总数
	}

}
