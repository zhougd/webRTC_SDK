package com.kinstalk.voip.sdk.logic.contact;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.api.WeaverService;
import com.kinstalk.voip.sdk.interfaces.WeaverRequestListener;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.data.DataService;
import com.kinstalk.voip.sdk.data.model.ContactDataItem;
import com.kinstalk.voip.sdk.logic.WeaverAbstractLogic;
import com.kinstalk.voip.sdk.logic.contact.json.ContactJsonObject;
import com.kinstalk.voip.sdk.logic.contact.json.ContactListJsonObject;
import com.kinstalk.voip.sdk.logic.contact.json.FriendAddJsonObject;
import com.kinstalk.voip.sdk.logic.contact.json.MyFriendJsonObject;
import com.kinstalk.voip.sdk.logic.contact.json.OnlineContactListJsonObject;
import com.kinstalk.voip.sdk.logic.contact.json.OnlineContactListJsonObject.OnlineContactJsonObject;
import com.kinstalk.voip.sdk.logic.user.UserConstants;
import com.kinstalk.voip.sdk.http.WeaverBaseAPI;
import com.kinstalk.voip.sdk.http.WeaverHttpConnection;
import com.kinstalk.voip.sdk.http.WeaverHttpRequest;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;
import com.kinstalk.voip.sdk.common.Utility;

public class ContactLogic extends WeaverAbstractLogic
{
	public static final String TAG = "ContactLogic";
	private URI mContactLogicUri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, ContactConstants.LOGIC_HOST, null);
	private ContactListJsonObject mContactList = new ContactListJsonObject();
	//private String mUserToken;

	public ContactLogic(Context context)
	{
		super(context);

		WeaverService.getInstance().registerLogicHandler(mContactLogicUri, this);
	}

	@Override
	public void doHandleRequest(WeaverRequest req)
	{
		 Log.d(getClass(), "ContactLogic handle request:" + req.toString());
		req.run();
	}

	public static class GetContactList extends WeaverRequest {
		private static final String mMethodName = "/" + GetContactList.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<ContactListJsonObject> mRetDataType = ContactListJsonObject.class;
		String mToken;
		String mUserId;
		int mFirstNumber;
		int mCountNumber;
		long mUpdateAt;
		long mUserUpdateAt;
		ContactListJsonObject mObject;
		
		public static ContactListJsonObject run(WeaverRequestListener listener) {
			ContactListJsonObject o = new ContactListJsonObject();
			Log.d(TAG, "get from memory:" + o.getUri());
			String token = WeaverService.getInstance().getUserLogic().getUserToken();
			String userId = WeaverService.getInstance().getUserLogic().getUserId();
			o = (ContactListJsonObject)WeaverAbstractLogic.getMemCache().get(o.getUri());
			if (o != null && o.getmToken() != null && o.getmUserId() != null) {
				if (token == null || !token.equals(o.getmToken()) || !userId.equals(o.getmUserId())) {
					Log.d(TAG, "get from invalid ContactListJsonObject from memory " + "userId = " + o.getmUserId() + "expected userId = " + "userId"
							+ "token = " + o.getmToken() + "expected token = " + token);
				    o = null;
				} else {
					Log.d(TAG, "get from valid ContactListJsonObject from memory " + "userId = " + o.getmUserId() + "token = " + o.getmToken());
				}
			} else {
				if (o == null) {
				    Log.d(TAG, "failed to get ContactListJsonObject from memory null.");
				} else {
					Log.d(TAG, "failed to get ContactListJsonObject from memory " + "userId = " + o.getmUserId() + "token = " + o.getmToken());
					o = null;
				}
			}
			
			if (o != null) {
				Log.d(TAG, "" + " UpdateAt= " + Utility.getDateTime(o.getUpdateAt()) +
						"UserUpdateAt = " + Utility.getDateTime(o.getUserUpdateAt()) + 
						"ListUpdateAt = " + Utility.getDateTime(o.getListUpdateAt()) +
						"ServerTime = " + Utility.getDateTime(o.getServerTime())
						);
			}
			
			WeaverService.getInstance().dispatchRequest(new ContactLogic.GetContactList(0, 500, 0, 0, listener, o));
			return o;
		}

		/**
		 * 获取用户的好友列表
		 * 
		 * @param token
		 *            用户验证的token，从UserLogin中获取
		 * @param firstNumber
		 *            好友列表的起始位置（从0开始）
		 * @param countNumber
		 *            要拉取的好友个数（最大500，如果实际数量小于要求数量，则返回实际数量）
		 * @param updateAt
		 *            上次获取数据的版本号
		 * @param userUpdateAt
		 *            上次获取数据的用户版本号
		 * @return
		 */
		public GetContactList(int firstNumber, int countNumber, long updateAt, long userUpdateAt, WeaverRequestListener listener, ContactListJsonObject defaultObject) {
			super.setParam(mTargetURI, listener);
			mToken = WeaverService.getInstance().getUserLogic().getUserToken();
			mUserId = WeaverService.getInstance().getUserLogic().getUserId();
			mFirstNumber = firstNumber;
			mCountNumber = countNumber;
			mUpdateAt = updateAt;
			mUserUpdateAt = userUpdateAt;
			mObject = defaultObject;
		}

		@Override
		public void run() {
			ContactListJsonObject response = null;
			if (mToken != null) {
				if (mObject == null) {
					ContactListJsonObject object = new ContactListJsonObject();
					object.setmUserId(mUserId);
					object = object.updateFromDB();
					if (object != null && object.getmToken() != null && !"".equals(object.getmToken()) && object.getmToken().equals(mToken)) {
						Log.d(TAG, "get ContactListJsonObject from db " + "userId = " + object.getmUserId() + "token = " + object.getmToken());
						preHandleResponse(object);
						mObject = object;
					}
				}

				/**
				 * For saving flow, here we should try to avoid server response
				 * information which client had received. It is good option to
				 * let server identifying by parameter "UpdateAt" or
				 * "UserUpdateAt", but now server does not check these items;
				 * Client also could get new contacts by parameter
				 * "firstNumber", but this is not convenient and maybe is not
				 * precise if user's contacts have been changed. There also are
				 * some workaround solutions from client side, such as client
				 * first try to request new contacts after it had hold, then if
				 * server response null, but "UpdateAt" or "UserUpdateAt"
				 * changed, then client redo request for a full contacts list.
				 * However workaround solution absolutely is not a best option
				 * eventually, we expect the total solution involved server side
				 * technical is a saving flow solution.
				 */
				if (mObject != null) {
					mUpdateAt = mObject.getUpdateAt();
					mUserUpdateAt = mObject.getUserUpdateAt();
				}
				try {
					response = getContactList(mToken, mUserId, mFirstNumber, mCountNumber, mUpdateAt, mUserUpdateAt);
				} catch (NetworkErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handleResponse(response);
			} else {
				setResponse(WeaverConstants.RequestReturnCode.TOKEN_INVALID, response);
			}
		}

		private ContactListJsonObject getContactList(String token, String userId, int firstNumber, int countNumber, long updateAt, long userUpdateAt)
				throws NetworkErrorException {
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/friend/list.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", token);
			params.put("firstNumber", Integer.toString(firstNumber));
			params.put("countNumber", Integer.toString(countNumber));
			params.put("updateAt", Long.toString(updateAt));
			params.put("userUpdateAt", Long.toString(userUpdateAt));
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			if (mObject == null) {
				mObject = new ContactListJsonObject();
			}
			mObject.setmToken(token);
			mObject.setmUserId(userId);
			
			return WeaverBaseAPI.dealResponse2(mObject, req.getResponseCode(), req.getResponseData(), ContactListJsonObject.class);

		}
	}
	
	public static class GetContactDetail extends WeaverRequest {
		private static final String mMethodName = "/" + GetContactDetail.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<MyFriendJsonObject> mRetDataType = MyFriendJsonObject.class;
		String mToken;
		String mContactId;
		String mContactMobileNo;

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
		public GetContactDetail(String contactId, String contactMobileNo, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mToken = WeaverService.getInstance().getUserLogic().getUserToken();
			mContactId = contactId;
			mContactMobileNo = contactMobileNo;
		}

		@Override
		public void run() {
			MyFriendJsonObject response = null;
			try {
				response = getContactDetail(mToken, mContactId, mContactMobileNo);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}

		private MyFriendJsonObject getContactDetail(String token, String contactId, String contactMobileNo) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/friend/myfriend.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", token);
			if (contactMobileNo != null) {
			    params.put("friendMobile", contactMobileNo);
			}
			if (contactId != null) {
			    params.put("friendId", contactId);
			}
			req.setBody(StringUtility.hashmapToParamStringByte(params));

			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), MyFriendJsonObject.class);
		}
	}
	
	public static class GetOnlineContactList extends WeaverRequest {
		private static final String mMethodName = "/" + GetOnlineContactList.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, UserConstants.LOGIC_HOST, mMethodName);
		public static final Class<OnlineContactListJsonObject> mRetDataType = OnlineContactListJsonObject.class;
		String mToken;
		int mShowDevices;

		/**
		 * 获取用户的好友列表
		 * 
		 * @param token
		 *            用户验证的token，从UserLogin中获取
		 * @param firstNumber
		 *            好友列表的起始位置（从0开始）
		 * @param countNumber
		 *            要拉取的好友个数（最大500，如果实际数量小于要求数量，则返回实际数量）
		 * @param updateAt
		 *            上次获取数据的版本号
		 * @param userUpdateAt
		 *            上次获取数据的用户版本号
		 * @return
		 */
		public GetOnlineContactList(int showDevices, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mToken = WeaverService.getInstance().getUserLogic().getUserToken();
			mShowDevices = showDevices;
		}

		@Override
		public void run() {
			OnlineContactListJsonObject response = null;
			try {
				response = getOnlineContactList(mToken, mShowDevices);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}

		private OnlineContactListJsonObject getOnlineContactList(String token, int showDevices) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/friend/status.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", token);
			params.put("showDevices", String.valueOf(showDevices));
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), OnlineContactListJsonObject.class);
		}
	}
	
	private void getOnlineList(String token)
	{
		OnlineContactListJsonObject newList = null;
		try {
			newList = onlineList(token, 1);
		} catch (NetworkErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (newList != null)
		{
			List<OnlineContactJsonObject> contactList = newList.getOnLineUsers();
			LinkedList<ContactDataItem> datas = new LinkedList<ContactDataItem>();


			for (OnlineContactJsonObject c1 : contactList)
			{
				for (ContactJsonObject c2 : mContactList.getContacts())
				{
					if (c1.getUserId() == c2.getFriendId())
					{
						datas.add(ContactDataItem.fromJsonObject(c2));
					}
				}
			}

			 Log.d(TAG, "Total found:" + datas.size());

			DataService.getInstance().putDataItems(datas);
		}
	}
	
	private static final OnlineContactListJsonObject onlineList(String token, int showDevices) throws NetworkErrorException
	{
		WeaverHttpRequest req = new WeaverHttpRequest();
		req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/friend/status.json");
		req.setMethod("POST");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("showDevices", String.valueOf(showDevices));
		req.setBody(StringUtility.hashmapToParamStringByte(params));
		WeaverHttpConnection.sendHttp(req);

		return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), OnlineContactListJsonObject.class);
	}

	/**
	 * 通过手机号或者用户id添加好友
	 * 
	 * @param token
	 *            登陆用户的token,用于验证用户的合法性
	 * @param friendMobiles
	 *            待添加为好友的用户的手机号列表
	 * @return
	 */
	private static final FriendAddJsonObject friendAdd(String token, String friendMobiles) throws NetworkErrorException
	{
		WeaverHttpRequest req = new WeaverHttpRequest();
		req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/friend/add.json");
		req.setMethod("POST");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("friendMobiles", friendMobiles);
		params.put("deviceType", "0");
		params.put("autoAddFriend", Boolean.toString(false));
		params.put("forbidPrivacy", Boolean.toString(false));
		params.put("bare", "1");
		req.setBody(StringUtility.hashmapToParamStringByte(params));
		WeaverHttpConnection.sendHttp(req);

		return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), FriendAddJsonObject.class);
	}

}
