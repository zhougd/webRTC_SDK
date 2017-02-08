package com.kinstalk.voip.sdk.logic.contact;

public class ContactConstants
{
	public static final String LOGIC_HOST = "contact";
	
	
	public static final class LogicPath
	{
		public static final String UPDATE_LIST_TIME = "/updateListTime";
		public static final String UPDATE_LIST = "/updateList";
		public static final String CONTACT_DETAILS = "/contactDetails";
		public static final String ONLINE_LIST = "/onlineList";
		public static final String GET_CONTACTS_LIST = "/getContactsList";
	}

	public static final class LogicParam
	{
		public static final String TOKEN = "token";
		public static final String LIST_TIMES = "list_times";
		public static final String CONTACT_LIST_NAME = "USER_FRIEND_ID_LIST";
		public static final String CONTACT_ID = "contact_id";
		public static final String CONTACT_MOBILE_NO = "contact_mobile_no";
		
	}
}
