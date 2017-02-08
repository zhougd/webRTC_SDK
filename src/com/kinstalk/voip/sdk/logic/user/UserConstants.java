package com.kinstalk.voip.sdk.logic.user;

public class UserConstants
{
	public static final String LOGIC_HOST = "user";

	public static final class LogicPath
	{
		public static final String LOGIN = "/login";
		public static final String LOGIN_WITH_SIP = "/login_with_sip";
		public static final String DEVICE_LOGIN = "/device_login";
		public static final String LOGOUT = "/logout";
		public static final String GET_USER_DETAILS = "/show_user_details";
		public static final String THIRD_PARTY_LOGIN = "/3rd_party_login";
		public static final String REGISTER = "/register4phone";
		public static final String ACTIVATION = "/getactivationinfo";
		public static final String VERIFY_CHECKCODE = "/verifycheckcode4phone";
		public static final String GET_CAPTCHA_IMAGE = "/getcaptchaimage";
		public static final String VERIFY_CAPTCHA = "/verifycaptcha4phone";
		public static final String SAVE_PHONE_USER = "/savephoneuser";
		public static final String GET_CAPTCHA_INFO = "/getpiccaptcha";
		public static final String MODIFY_USER_DETAIL = "/modify_user_detail";
		public static final String RESET_PASSWORD = "/reset_password";
		public static final String GET_SMS_FOR_RESET_PASSWORD = "/getsms4resetpwd";
		public static final String CHECK_SMS_CODE_FOR_RESET_PASSWORD = "/checksmscode4resetpwd";
	}

	public static final class LogicParam
	{
		public static final String USER_ID = "user_id";
		public static final String USER_PASSWORD = "user_password";
		public static final String USER_DOMAIN = "user_domain";
		public static final String USER_NAME = "user_name";
		public static final String USER_GENDER = "user_gender";
		public static final String PIC_URL = "pic_url";
		public static final String AREA_CODE = "area_code";
		public static final String EMAIL = "email";
		public static final String COUNTRY = "country";
		public static final String PROVINCE = "province";
		public static final String CITY = "city";
		public static final String BIRTH_YEAR = "birthYear";
		public static final String BIRTH_MONTH = "birthMonth";
		public static final String BIRTH_DAY = "birthDay";
		public static final String SIGN = "sign";
		public static final String PROFESSION = "profession";
		public static final String AGE = "age";
		public static final String COMPANY = "company";
		public static final String SCHOOL = "school";
		public static final String MARITAL_STATUS = "marriage";
		public static final String CONSTELLATION = "constellation";
		public static final String TOKEN = "token";
		public static final String APP_KEY = "app_key";
		public static final String APP_SECRET = "app_secret";
		public static final String USER_TOKEN = "user_token";
		public static final String DEVICE_SN = "device_sn";
		public static final String SOURCE = "source";
		public static final String LOCALE = "locale";
		public static final String COUNTRY_CODE = "country_code";
		public static final String PASSPORT = "passport";
		public static final String LPS_UTGT = "lpsutgt";
		public static final String MOBILE_NO = "mobile_no";
		public static final String CHECK_CODE = "check_code";
		public static final String USER_TYPE = "user_type";
		public static final String TOKEN_ORIGIN = "token_origin";
		public static final String REGISTER_ORIGIN = "register_origin";
		public static final String RANDOM_CODE = "random_code";
		public static final String CAPTCHA = "captcha";
		public static final String SMS_TYPE = "sms_type";

		public static final String INCALL_ACTIVITY_NAME = "incall_activity";
		public static final String INCALL_NOTIFICATION = "incall_notification";
	}

	public static final class UserStatus
	{
		public static final int OFFLINE = 0;
		public static final int ONLINE = 1;
	}
}
