package com.kinstalk.voip.sdk.logic.user;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.api.WeaverService;
import com.kinstalk.voip.sdk.interfaces.WeaverRequestListener;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.logic.WeaverAbstractLogic;
import com.kinstalk.voip.sdk.logic.sip.SipConstants;
import com.kinstalk.voip.sdk.logic.sip.SipLogicApi;
import com.kinstalk.voip.sdk.logic.user.json.GetPinJsonObject;
import com.kinstalk.voip.sdk.logic.user.json.Register4PhoneJsonObject;
import com.kinstalk.voip.sdk.logic.user.json.UserDetailJsonObject;
import com.kinstalk.voip.sdk.logic.user.json.UserInformationJsonObject;
import com.kinstalk.voip.sdk.logic.user.json.UserLoginBy3rdPartyJsonObject;
import com.kinstalk.voip.sdk.logic.user.json.UserLoginJsonObject;
import com.kinstalk.voip.sdk.logic.user.json.UserLogoutJsonObject;
import com.kinstalk.voip.sdk.logic.user.json.UserModifyDetailJsonObject;
import com.kinstalk.voip.sdk.logic.user.json.UserResetPwdJsonObject;
import com.kinstalk.voip.sdk.logic.user.json.UserLoginJsonObject.Infos;
import com.kinstalk.voip.sdk.http.WeaverBaseAPI;
import com.kinstalk.voip.sdk.http.WeaverHttpConnection;
import com.kinstalk.voip.sdk.http.WeaverHttpRequest;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;
import com.kinstalk.voip.sdk.common.UserPreferences;
import com.kinstalk.voip.sdk.common.Utility;

public class UserLogic extends WeaverAbstractLogic
{
	private URI mUserLogicUri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, null);
	//private final Context mContext;
	private UserLoginJsonObject mCurrentUserLoginJsonObject = null;
	private String mCurrentUserId = "";

	// private String mUserToken;
	// private String mUserId;
	// private String mUserDomain;

	public synchronized UserLoginJsonObject getmCurrentUserLoginJsonObject() {
		if (mCurrentUserLoginJsonObject == null) {
			mCurrentUserLoginJsonObject = new UserLoginJsonObject();
		}
		mCurrentUserLoginJsonObject = mCurrentUserLoginJsonObject.updateFromDB();
		return mCurrentUserLoginJsonObject;
	}
	
	public String getUserToken() {
		String token = "";
		UserLoginJsonObject userLoginJsonObject = WeaverService.getInstance().getUserLogic().getmCurrentUserLoginJsonObject();
		if (userLoginJsonObject != null) {
			token = userLoginJsonObject.getToken();
		}
		return token;
	}
	
	public String getUserId() {
		String userId = "";
		UserLoginJsonObject userLoginJsonObject = WeaverService.getInstance().getUserLogic().getmCurrentUserLoginJsonObject();
		if (userLoginJsonObject != null) {
			userId = userLoginJsonObject.getUserId();
		}
		return userId;
	}

	private synchronized void setmCurrentUserLoginJsonObject(UserLoginJsonObject currentUserLoginJsonObject) {
		if (currentUserLoginJsonObject != null) {
			mCurrentUserLoginJsonObject = currentUserLoginJsonObject;
			mCurrentUserId = mCurrentUserLoginJsonObject.getUserId();
			mCurrentUserLoginJsonObject.putToDB();
		} else {
			mCurrentUserLoginJsonObject.removeFromDB();
			mCurrentUserLoginJsonObject = currentUserLoginJsonObject;
		}
	}
	
	private synchronized void setmCurrentUserId(String userId) {
		mCurrentUserId = userId;
	}
	
	private synchronized String getmCurrentUserId() {
		return mCurrentUserId;
	}

	public UserLogic(Context context)
	{
		super(context);
		 Log.d(getClass(), "UserLogic init~");
		//mContext = context;

		WeaverService.getInstance().registerLogicHandler(mUserLogicUri, this);
	}
	
	@Override
	public void doHandleRequest(WeaverRequest req)
	{
		 Log.d(getClass(), "UserLogic handle request:" + req.toString());
		req.run();
	}
	
	public static class UserLogin extends WeaverRequest {
		private static final String mMethodName = "/" + UserLogin.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<UserLoginJsonObject> mRetDataType = UserLoginJsonObject.class;
		String mUserId;
		String mUserPassword;
		String mAppKey;
		String mAppSecret;
		String mAppSource;

		/**
		 * 用户登录，获取token和用户好友列表信息
		 * 
		 * @param passport
		 *            用户账号
		 * @param password
		 *            用户密码
		 * @param appKey
		 *            有约发放第三方的appkey
		 * @param appSecret
		 *            有约发放第三方的appSecret
		 * 
		 * @return
		 */
		public UserLogin(String userId, String userPassword, String appKey, String appSecret, String appSource, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mUserId = userId;
			mUserPassword = userPassword;
			mAppKey = appKey;
			mAppSecret = appSecret;
			mAppSource = appSource;
			if (mAppSource == null || "".equals(mAppSource)) {
				mAppSource = "Weaver";
			}
		}

		@Override
		public void run() {
			UserPreferences.setBool(UserPreferences.Key.IS_AUTO_REGISTER, true);
			login(mUserId, mUserPassword, mAppKey, mAppSecret);
		}

		private void login(String userId, String userPassword, String appKey, String appSecret) {
			/*UserLoginJsonObject userLoginJsonObject = null;
			try {
				userLoginJsonObject = userLoginHttp(userId, userPassword, appKey, appSecret);
				if (userLoginJsonObject != null) {
					String userDomain = "sip" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com";
					userLoginJsonObject.setUserDomain(userDomain);
					userLoginJsonObject.setUserId(userId);
					userLoginJsonObject.setPassword(userPassword);
					userLoginJsonObject.setAppKey(appKey);
					userLoginJsonObject.setAppSecret(appSecret);
					userLoginJsonObject.setAppSource(mAppSource); 
					WeaverService.getInstance().getUserLogic().setmCurrentUserLoginJsonObject(userLoginJsonObject);// update again
					Log.i(getClass(), "Token: = " + userLoginJsonObject.getToken() + 
					"PhoneAbility = " + userLoginJsonObject.getPhoneAbility() + 
					"UpdateAt = " + userLoginJsonObject.getUpdateAt() + "[" + Utility.getDateTime(userLoginJsonObject.getUpdateAt()) + "]" + 
					"Uri = " + userLoginJsonObject.getUri() +
					"ExpireIn = " + userLoginJsonObject.getExpireIn());
				}
				handleResponse(userLoginJsonObject);
			} catch (NetworkErrorException e) {
				handleResponse(userLoginJsonObject);
			}*/

			if (WeaverService.getInstance().getSipLogic() != null) {
				//if (this.getResponseCode() == WeaverConstants.RequestReturnCode.OK) {
				//	UserLoginJsonObject response = (UserLoginJsonObject) this.getResponse();
					// 设置音频适配参数
				//	WeaverRequest req1 = SipLogicApi.sipSetAudioMode(Integer.parseInt(response.getPhoneAbility()), null);
				//	WeaverService.getInstance().dispatchRequest(req1);

					// sip登录
					String userDomain = "sip" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com";
					Log.e("userDomain", userDomain);
					WeaverRequest req3 = SipLogicApi.sipSetAccount(userId, userPassword, userDomain, null);
					WeaverService.getInstance().dispatchRequest(req3);
				//}
			}
		}

		private UserLoginJsonObject userLoginHttp(String passport, String password, String appKey, String appSecret) throws NetworkErrorException {
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/UserLogin.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("passport", passport);
			params.put("password", password);
			params.put("appKey", appKey);
			params.put("appSecret", appSecret);
			params.put("serverVersion", "2");
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), UserLoginJsonObject.class);
		}
	}
	
	public static class UserLogout extends WeaverRequest{
		private static final String mMethodName = "/" + UserLogout.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<UserLogoutJsonObject> mRetDataType = UserLogoutJsonObject.class;
		
		String mToken;
		String mSource;
		String mLocale;
		
		/**
		 * 用户登出
		 * 
		 * @param password
		 *            用户的密码
		 * @param checkCode
		 *            用户填写的验证码
		 * @return
		 * @throws NetworkErrorException
		 */
		public UserLogout(WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			UserLoginJsonObject userLoginJsonObject = WeaverService.getInstance().getUserLogic().getmCurrentUserLoginJsonObject();
			if (userLoginJsonObject != null) {
			    mToken = userLoginJsonObject.getToken();
				mSource = userLoginJsonObject.getAppSource();
				mLocale = "zh-CN";//local;
			}
		}
		
		@Override
		public void run() {
			UserPreferences.setBool(UserPreferences.Key.IS_AUTO_REGISTER, false);
			WeaverRequest req = this;
			UserLogoutJsonObject response = null;
			removeUserLoginJsonObjectFromDB();
			try {
				response = userLogoutHttp(mToken, mSource, mLocale);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
			
			if (WeaverService.getInstance().getSipLogic() != null)
			{
				//if (req.getResponseCode() == WeaverConstants.RequestReturnCode.OK)
				{
					 Log.d(getClass(), "[UserLogic] sipDeleteAccount");

					URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, SipConstants.LOGIC_HOST, SipConstants.LogicPath.DELETE_USER_ACCOUNT);
					WeaverRequest reqForSipDelete = new WeaverRequest(uri, req.getRequestCallback());
					WeaverService.getInstance().dispatchRequest(reqForSipDelete);
					//WeaverService.getInstance().dispatchRequest(WeaverAPI.sipDeleteAccount(req.getRequestCallback()));
				}
			}
		}
		
		private void removeUserLoginJsonObjectFromDB() {
			//WeaverService.getInstance().getUserLogic().setmCurrentUserLoginJsonObject(null);
		}
		
		private UserLogoutJsonObject userLogoutHttp(String token, String source, String locale) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/UserLogout.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", token);
			params.put("source", source);
			params.put("locale", locale);
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), UserLogoutJsonObject.class);
		}
	}

	public static class UserDeviceLogin extends WeaverRequest{
		private static final String mMethodName = "/" + UserDeviceLogin.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<UserInformationJsonObject> mRetDataType = UserInformationJsonObject.class;
		String mDeviceSN;
		int mSource;
		
		/**
		 * 设备号登录，获取token
		 * 
		 * @param deviceSN
		 *            用户设备号/[Any series number, notes: now only work at online environment.]
		 * 
		 * @return
		 */
		public UserDeviceLogin(String deviceSN, int source, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mDeviceSN = deviceSN;
			mSource = source;
		}
		
		@Override
		public void run() {
			UserInformationJsonObject response = null;
			try {
				response = userDeviceLogin(mDeviceSN, mSource);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
			
			if (response != null) {
				WeaverService.getInstance().getUserLogic().setmCurrentUserId(response.getUserId());
			}

			if (WeaverService.getInstance().getSipLogic() != null) {
				//if (this.getResponseCode() == WeaverConstants.RequestReturnCode.OK) {
					// 设置音频适配参数
					//WeaverRequest req1 = SipLogicApi.sipSetAudioMode(Integer.parseInt(response.getPhoneAbility()), null);
					//WeaverService.getInstance().dispatchRequest(req1);

					// sip登录
					String userDomain = "sip" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com";
					Log.e("userDomain", userDomain);
					WeaverRequest req3 = SipLogicApi.sipSetAccount(response.getUserId(), "", userDomain, null);
					WeaverService.getInstance().dispatchRequest(req3);
				//}
			}
		}
		

		/**
		 * 生成设备登录请求。使用设备号（无密码）登录
		 * 
		 * @param deviceSN
		 *            用户设备号码
		 * @param source
		 *            用户平台类型，android=8
		 * @return 生成的请求对象
		 */
		private UserInformationJsonObject userDeviceLogin(String deviceSN, int source) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/TempUserInformation.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("deviceSN", deviceSN);
			params.put("source", String.valueOf(source));
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), UserInformationJsonObject.class);
		}
	}
	
	public static class UserRegister extends WeaverRequest{
		private static final String mMethodName = "/" + UserRegister.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<Register4PhoneJsonObject> mRetDataType = Register4PhoneJsonObject.class;
		String mPassport;
		
		/**
		 * 通过手机号注册友约用户
		 * 
		 * @param passport
		 *            要注册的手机号
		 * @return
		 */
		public UserRegister(String passport, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mPassport = passport;
		}
		
		@Override
		public void run() {
			Register4PhoneJsonObject response = null;
			try {
				response = userRegister(mPassport);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}
		
		private Register4PhoneJsonObject userRegister(String passport) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/Register4Phone.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("passport", passport);
			params.put("serverVersion", "2");
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), Register4PhoneJsonObject.class);
		}
	}
	
	public static class UserGetActivationInfo extends WeaverRequest{
		private static final String mMethodName = "/" + UserGetActivationInfo.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<Register4PhoneJsonObject> mRetDataType = Register4PhoneJsonObject.class;
		String mLpsUtgt;
		String mMobileNo;
		
		/**
		 * 通过手机号注册友约用户
		 * 
		 * @param passport
		 *            要注册的手机号
		 * @return
		 */
		public UserGetActivationInfo(String lpsUtgt, String mobileNo, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mLpsUtgt = lpsUtgt;
			mMobileNo = mobileNo;
		}
		
		@Override
		public void run() {
			Register4PhoneJsonObject response = null;
			try {
				response = userGetActivationInfo(mLpsUtgt, mMobileNo);
				if (response != null)
				{
					response.setLpsUtgt(mLpsUtgt);
					response.setMobileNo(mMobileNo);
				}
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}
		
		private Register4PhoneJsonObject userGetActivationInfo(String lpsUtgt, String mobileNo) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/GetActivationInfo.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("lpsutgt", lpsUtgt);
			params.put("mobileNo", mobileNo);
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), Register4PhoneJsonObject.class);
		}
	}

	public static class UserVerifyCheckCode4Phone extends WeaverRequest{
		private static final String mMethodName = "/" + UserVerifyCheckCode4Phone.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<Register4PhoneJsonObject> mRetDataType = Register4PhoneJsonObject.class;
		String mLpsUtgt;
		String mMobileNo;
		String mCheckCode;
		
		/**
		 * 通过手机号注册友约用户
		 * 
		 * @param passport
		 *            要注册的手机号
		 * @return
		 */
		public UserVerifyCheckCode4Phone(String lpsUtgt, String mobileNo, String checkCode, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mLpsUtgt = lpsUtgt;
			mMobileNo = mobileNo;
			mCheckCode = checkCode;
		}
		
		@Override
		public void run() {
			Register4PhoneJsonObject response = null;
			try {
				response = userVerifyCheckCode4Phone(mLpsUtgt, mMobileNo, mCheckCode);
				if (response != null)
				{
					response.setLpsUtgt(mLpsUtgt);
					response.setMobileNo(mMobileNo);
				}
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}

		private Register4PhoneJsonObject userVerifyCheckCode4Phone(String lpsUtgt, String mobileNo, String checkCode) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/VerifyCheckCode4Phone.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("lpsutgt", lpsUtgt);
			params.put("mobileNo", mobileNo);
			params.put("checkCode", checkCode);
			params.put("serverVersion", "2");
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), Register4PhoneJsonObject.class);
		}
		
	}
	
	public static class UserGetUserDetail extends WeaverRequest{
		private static final String mMethodName = "/" + UserGetUserDetail.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<UserDetailJsonObject> mRetDataType = UserDetailJsonObject.class;
		String mToken;
		int mType;
		String mQueryParam;
		String mUserId;
		
		public static UserDetailJsonObject run(WeaverRequestListener listener) {
			UserDetailJsonObject o = new UserDetailJsonObject();
			o = (UserDetailJsonObject)WeaverAbstractLogic.getMemCache().get(o.getUri());
			WeaverService.getInstance().dispatchRequest(new UserLogic.UserGetUserDetail(0, null, listener));
			return o;
		}
		
		/**
		 * 获取用户的详细资料
		 * 
		 * @param token
		 *            用户验证的token，从UserLogin中获取
		 * @param type
		 *            0 查自己 1根据userId查用户 2 根据电话号码查用户
		 * @param queryParam
		 *            根据type来传递，查自己 为空，查别人 type=1传递userid，type=2 传递电话号码
		 * @return
		 */
		public UserGetUserDetail(int type, String queryParam, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			UserLoginJsonObject userLoginJsonObject = WeaverService.getInstance().getUserLogic().getmCurrentUserLoginJsonObject();
			if (userLoginJsonObject != null) {
				mToken = userLoginJsonObject.getToken();
				mUserId = userLoginJsonObject.getUserId();
			}
			if ( mUserId == null || !(mUserId.equals(WeaverService.getInstance().getUserLogic().getmCurrentUserId()))){
				UserInformationJsonObject object = new UserInformationJsonObject();
				object = object.updateFromDB();
				if (object != null) {
					mToken = object.getPassport();
					mUserId = object.getUserId();
				}
			}

			mType = type;
			mQueryParam = queryParam;
		}
		
		@Override
		public void run() {
			UserDetailJsonObject response = null;
			Log.i("UserGetUserDetail", "mToken=" + mToken);
			if (mToken != null) {
				response = userGetUserDetailFromDB(mUserId, mToken);
				if (response != null) {
					preHandleResponse(response);
					Log.i("UserGetUserDetail", "handleResponse1=" + response);
				}
				try {
					response = userGetUserDetail(mToken, mType, mQueryParam);
				} catch (NetworkErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handleResponse(response);
			} else {
				setResponse(WeaverConstants.RequestReturnCode.TOKEN_INVALID, response);
			}
			
			Log.i("UserGetUserDetail", "handleResponse2=" + response);
		}
		
		private UserDetailJsonObject userGetUserDetailFromDB(String userWeaverId, String token) {
			UserDetailJsonObject userDetailJsonObject = null;
			if (token != null && userWeaverId != null && !"".equals(userWeaverId)) {
				userDetailJsonObject = new UserDetailJsonObject();
				userDetailJsonObject.setUserId(userWeaverId);
				userDetailJsonObject = userDetailJsonObject.updateFromDB();
				if (userDetailJsonObject == null) {
					List<UserDetailJsonObject> userDetailJsonObjectList = UserDetailJsonObject.queryForEq(UserDetailJsonObject.class, "mobileNo",
							userWeaverId);
					if (userDetailJsonObjectList != null && userDetailJsonObjectList.size() > 0) {
						for (int i = 0; i < userDetailJsonObjectList.size(); i++) {
						    if (token.equals(userDetailJsonObjectList.get(i).getmToken())) {
						    	userDetailJsonObject = userDetailJsonObjectList.get(i);
						    }
						     
						}
					}
				}
			}
			return userDetailJsonObject;
		}

		private UserDetailJsonObject userGetUserDetail(String token, int type, String queryParam) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/GetUserDetail.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", token);
			params.put("type", Integer.toString(type));
			params.put("queryParam", queryParam);
			params.put("serverVersion", "2");
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			UserDetailJsonObject object = new UserDetailJsonObject();
			object.setmToken(token);
			return WeaverBaseAPI.dealResponse2(object, req.getResponseCode(), req.getResponseData(), UserDetailJsonObject.class);
		}
		
	}
	
	public static class UserGetWeaverTokenByThirdToken extends WeaverRequest{
		private static final String mMethodName = "/" + UserGetWeaverTokenByThirdToken.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<UserLoginBy3rdPartyJsonObject> mRetDataType = UserLoginBy3rdPartyJsonObject.class;
		String mToken;
		int mTokenOrigin;
		String mAppKey; 
		String mAppSecret;
		
		public UserGetWeaverTokenByThirdToken(String token, int tokenOrigin, String appKey, String appSecret, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mToken = token;
			mTokenOrigin = tokenOrigin;
			mAppKey = appKey; 
			mAppSecret = appSecret;
		}
		
		@Override
		public void run() {
			UserLoginBy3rdPartyJsonObject response = null;
			try {
				response = userGetWeaverTokenByThirdToken(mToken, mTokenOrigin, mAppKey, mAppSecret);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}

		/**
		 * 通过第三方登录（包含自动注册）
		 * 
		 * @param token
		 *            第三方用户token
		 * @param tokenOrigin
		 *            第三方用户token来源 譬如 sina微博来源是3 qq来源是4
		 * @return
		 * @throws NetworkErrorException
		 */
		private UserLoginBy3rdPartyJsonObject userGetWeaverTokenByThirdToken(String token, int tokenOrigin, String appKey, String appSecret) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/GetWeaverTokenByThirdToken.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", token);
			params.put("tokenOrigin", String.valueOf(tokenOrigin));
			params.put("appKey", appKey);
			params.put("appSecret", appSecret);
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), UserLoginBy3rdPartyJsonObject.class);
		}
		
	}
	
	public static class UserGetPin extends WeaverRequest{
		private static final String mMethodName = "/" + UserGetPin.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<GetPinJsonObject> mRetDataType = GetPinJsonObject.class;
		
		public UserGetPin(WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
		}
		
		@Override
		public void run() {
			GetPinJsonObject response = null;
			try {
				response = userGetPin();
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}

		/**
		 * 获取图形验证码相关信息
		 * 
		 * @return
		 * @throws NetworkErrorException
		 */
		private GetPinJsonObject userGetPin() throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/GetPIN.json");
			req.setMethod("POST");
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), GetPinJsonObject.class);
		}
		
	}
	
	public static class UserGetCaptchaImage extends WeaverRequest{
		private static final String mMethodName = "/" + UserGetCaptchaImage.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<GetPinJsonObject> mRetDataType = GetPinJsonObject.class;//???????????
		String mRandomCode;
		
		public UserGetCaptchaImage(String randomCode, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mRandomCode = randomCode;
		}
		
		@Override
		public void run() {
			byte[] response = null;
			try {
				response = userGetCaptchaImage(mRandomCode);
				setResponse(WeaverConstants.RequestReturnCode.OK, response);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setResponse(WeaverConstants.RequestReturnCode.NETWORK_FAILURE, null);
			}
			//handleResponse(response);
		}

		/**
		 * 获取图形验证码相关信息
		 * 
		 * @return
		 * @throws NetworkErrorException
		 */
		private byte[] userGetCaptchaImage(String randomCode) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/GetCaptchaImage.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("randomCode", randomCode);
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			if (req.getResponseCode() == 200)
			{
				return req.getResponseData();
			}
			else
			{
				return null;
			}
		}
		
	}
	
	public static class UserVerifyCaptcha4Phone extends WeaverRequest{
		private static final String mMethodName = "/" + UserVerifyCaptcha4Phone.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<GetPinJsonObject> mRetDataType = GetPinJsonObject.class;
		String mRandomCode;
		String mMobileNo;
		String mCaptcha;
		
		public UserVerifyCaptcha4Phone(String randomCode, String mobileNo, String captcha, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mRandomCode = randomCode;
			mMobileNo = mobileNo;
			mCaptcha = captcha;
		}
		
		@Override
		public void run() {
			GetPinJsonObject response = null;
			try {
				response = userVerifyCaptcha4Phone(mRandomCode, mMobileNo, mCaptcha);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}

		/**
		 * 验证图形验证码
		 * 
		 * @param randomCode
		 * 
		 * @param mobileNo
		 *            用户手机号
		 * @param captcha
		 *            用户填写的验证码
		 * @return
		 * @throws NetworkErrorException
		 */
		public static final GetPinJsonObject userVerifyCaptcha4Phone(String randomCode, String mobileNo, String captcha) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/VerifyCaptcha4Phone.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("randomCode", randomCode);
			params.put("mobileNo", randomCode);
			params.put("captcha", randomCode);
			params.put("serverVersion", "2");
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), GetPinJsonObject.class);
		}
		
		
	}

	public static class UserSavePhoneUser extends WeaverRequest{
		private static final String mMethodName = "/" + UserSavePhoneUser.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<GetPinJsonObject> mRetDataType = GetPinJsonObject.class;
		String mLpsUtgt;
		String mRandomCode;
		String mMobileNo;
		String mPassword;
		String mRealName;
		
		public UserSavePhoneUser(String lpsUtgt, String randomCode, String mobileNo, String password, String realName, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mLpsUtgt = lpsUtgt;
			mRandomCode = randomCode;
			mMobileNo = mobileNo;
			mPassword = password;
			mRealName = realName;
		}
		
		@Override
		public void run() {
			GetPinJsonObject response = null;
			try {
				response = userSavePhoneUser(mLpsUtgt, mRandomCode, mMobileNo, mPassword, mRealName);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}

		/**
		 * 验证成功后保存用户
		 * 
		 * @param randomCode
		 * 
		 * @param mobileNo
		 *            用户的手机号
		 * @param password
		 *            用户的密码
		 * @param realName
		 *            用户的昵称
		 * 
		 * @return
		 * @throws NetworkErrorException
		 */
		private GetPinJsonObject userSavePhoneUser(String lpsUtgt, String randomCode, String mobileNo, String password, String realName) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/SavePhoneUser.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("lpsutgt", lpsUtgt);
			params.put("randomCode", randomCode);
			params.put("mobileNo", mobileNo);
			params.put("password", password);
			params.put("realName", realName);
			params.put("serverVersion", "2");
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), GetPinJsonObject.class);
		}
		
		
	}
	

	public static class UserModifyUserDetail extends WeaverRequest{
		private static final String mMethodName = "/" + UserModifyUserDetail.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<UserModifyDetailJsonObject> mRetDataType = UserModifyDetailJsonObject.class;
		String mToken;
		String mRealName;
		String mPicUrl;
		Integer mGender;
		Integer mAreaCode;
		String mEmail;
		String mProfession;
		String mAge;
		String mCompany;
		String mSchool;
		String mMarriage;
		String mConstellation;
		String mCountry;
		String mProvince;
		String mCity;
		String mBirthYear;
		String mBirthMonth;
		String mBirthDay;
		String mSign;

		public UserModifyUserDetail(String token, String realName, String picUrl, Integer gender, Integer areaCode, String email, String profession, String age,
				String company, String school, String marriage, String constellation, String country, String province, String city, String birthYear, String birthMonth, String birthDay, String sign, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mToken = token;
			mRealName = realName;
			mPicUrl = picUrl;
			mGender = gender;
			mAreaCode = areaCode;
			mEmail = email;
			mProfession = profession;
			mAge = age;
			mCompany = company;
			mSchool = school;
			mMarriage = marriage;
			mConstellation = constellation;
			mCountry = country;
			mProvince = province;
			mCity = city;
			mBirthYear = birthYear;
			mBirthMonth = birthMonth;
			mBirthDay = birthDay;
			mSign = sign;

		}
		
		@Override
		public void run() {
			UserModifyDetailJsonObject response = null;
			try {
				response = userModifyUserDetail(mToken, mRealName, mPicUrl, mGender, mAreaCode, mEmail, mProfession, mAge, mCompany, mSchool,
						mMarriage, mConstellation, mCountry, mProvince, mCity, mBirthYear, mBirthMonth, mBirthDay, mSign);
						

			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}

		/**
		 * 验证成功后保存用户
		 * 
		 * @param randomCode
		 * 
		 * @param mobileNo
		 *            用户的手机号
		 * @param password
		 *            用户的密码
		 * @param realName
		 *            用户的昵称
		 * 
		 * @return
		 * @throws NetworkErrorException
		 */
		public static final UserModifyDetailJsonObject userModifyUserDetail(String token, String realName, String picUrl, Integer gender, Integer areaCode, String email, String profession, String age,
				String company, String school, String marriage, String constellation, String country, String province, String city, String birthYear, String birthMonth, String birthDay, String sign)
				throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/ModifyUserDetail.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", token);
			params.put("realName", realName);
			params.put("picUrl", picUrl);
			params.put("serverVersion", "2");
			if (gender != null)
			{
				params.put("gender", String.valueOf(gender));
			}
			if (areaCode != null)
			{
				params.put("areaCode", String.valueOf(areaCode));
			}
			params.put("email", email);
			params.put("country", country);
			params.put("province", province);
			params.put("city", city);
			params.put("birthYear", birthYear);
			params.put("birthMonth", birthMonth);
			params.put("birthDay", birthDay);
			params.put("sign", sign);
			params.put("constellation", constellation);
			params.put("company", company);
			params.put("school", school);
			params.put("maritalStatus", marriage);
			params.put("job", profession);
			params.put("age", age);

			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), UserModifyDetailJsonObject.class);
		}
		
		
	}
	
	public static class UserResetPwd extends WeaverRequest{
		private static final String mMethodName = "/" + UserResetPwd.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<UserResetPwdJsonObject> mRetDataType = UserResetPwdJsonObject.class;
		String mRandomCode;
		String mPassport;
		String mPassword;
		String mCountryCode;
		String mSmsType;
		
		public UserResetPwd(String randomCode, String passport, String password, String countryCode, String smsType, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mRandomCode = randomCode;
			mPassport = passport;
			mPassword = password;
			mCountryCode = countryCode;
			mSmsType = smsType;
		}
		
		@Override
		public void run() {
			UserResetPwdJsonObject response = null;
			try {
				response = userResetPwd(mRandomCode, mPassport, mPassword, mCountryCode, mSmsType);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}

		/**
		 * 验证成功后保存用户
		 * 
		 * @param randomCode
		 * 
		 * @param mobileNo
		 *            用户的手机号
		 * @param password
		 *            用户的密码
		 * @param realName
		 *            用户的昵称
		 * 
		 * @return
		 * @throws NetworkErrorException
		 */
		private UserResetPwdJsonObject userResetPwd(String randomCode, String passport, String password, String countryCode, String smsType) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/ResetPwd.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("randomCode", randomCode);
			params.put("passport", passport);
			params.put("password", password);
			params.put("countryCode", countryCode);
			params.put("smsType", smsType);
			params.put("serverVersion", "2");
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), UserResetPwdJsonObject.class);
		}
		
		
	}

	public static class UserGetSms4ResetPwd extends WeaverRequest{
		private static final String mMethodName = "/" + UserGetSms4ResetPwd.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<UserResetPwdJsonObject> mRetDataType = UserResetPwdJsonObject.class;
		String mPassport;
		
		public UserGetSms4ResetPwd(String passport, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mPassport = passport;
		}
		
		@Override
		public void run() {
			UserResetPwdJsonObject response = null;
			try {
				response = userGetSms4ResetPwd(mPassport);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}

		/**
		 * 获取重置密码的短信验证码
		 * 
		 * @param password
		 *            用户的帐号，手机号或者userId
		 * 
		 * @return
		 * @throws NetworkErrorException
		 */
		private UserResetPwdJsonObject userGetSms4ResetPwd(String passport) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/GetSmsg4ResetPwd.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("passport", passport);
			params.put("serverVersion", "2");
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), UserResetPwdJsonObject.class);
		}
		
		
	}
	
	public static class UserCheckSmsCode4ResetPwd extends WeaverRequest{
		private static final String mMethodName = "/" + UserCheckSmsCode4ResetPwd.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<UserResetPwdJsonObject> mRetDataType = UserResetPwdJsonObject.class;
		String mPassport;
		String mCheckCode;
		
		public UserCheckSmsCode4ResetPwd(String passport, String checkCode, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mPassport = passport;
			mCheckCode = checkCode;
		}
		
		@Override
		public void run() {
			UserResetPwdJsonObject response = null;
			try {
				response = userCheckSmsCode4ResetPwd(mPassport, mCheckCode);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}

		/**
		 * 验证重置密码的短信验证码
		 * 
		 * @param password
		 *            用户的密码
		 * @param checkCode
		 *            用户填写的验证码
		 * @return
		 * @throws NetworkErrorException
		 */
		public static final UserResetPwdJsonObject userCheckSmsCode4ResetPwd(String passport, String checkCode) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/checkSmsCode4ResetPwd.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("passport", passport);
			params.put("checkCode", checkCode);
			params.put("serverVersion", "2");
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), UserResetPwdJsonObject.class);
		}
	}

	private static final UserInformationJsonObject userShowUserInformation(String token, String oemTag) throws NetworkErrorException
	{
		WeaverHttpRequest req = new WeaverHttpRequest();
		req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/users/ShowUserInfomation.json");
		req.setMethod("POST");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("oemTag", oemTag);
		params.put("serverVersion", "2");
		req.setBody(StringUtility.hashmapToParamStringByte(params));
		WeaverHttpConnection.sendHttp(req);

		return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), UserInformationJsonObject.class);
	}
}
