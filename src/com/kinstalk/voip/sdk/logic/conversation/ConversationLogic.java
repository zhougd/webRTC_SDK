package com.kinstalk.voip.sdk.logic.conversation;

import java.net.URI;
import java.util.HashMap;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.api.WeaverService;
import com.kinstalk.voip.sdk.interfaces.WeaverRequestListener;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.logic.WeaverAbstractLogic;
import com.kinstalk.voip.sdk.logic.conversation.json.ConversationHistoryListJsonObject;
import com.kinstalk.voip.sdk.http.WeaverBaseAPI;
import com.kinstalk.voip.sdk.http.WeaverHttpConnection;
import com.kinstalk.voip.sdk.http.WeaverHttpRequest;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;

public class ConversationLogic extends WeaverAbstractLogic{
	private URI mConversationLogicUri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, ConversationConstants.LOGIC_HOST, null);
	
	public ConversationLogic(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		WeaverService.getInstance().registerLogicHandler(mConversationLogicUri, this);
	}

	@Override
	public void doHandleRequest(WeaverRequest req) {
		// TODO Auto-generated method stub
		 Log.d(getClass(), "ConversationLogic handle request:" + req.toString());
		req.run();
	}
	
	public static class GetConversationHistoryList extends WeaverRequest {
		private static final String mMethodName = "/" + GetConversationHistoryList.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, ConversationConstants.LOGIC_HOST, mMethodName);
		public static final Class<ConversationHistoryListJsonObject> mRetDataType = ConversationHistoryListJsonObject.class;
		String mToken;
		int mFirstNumber;
		int mCountNumber;


		/**
		 * Get conversation history list
		 * 
		 * @param token
		 *            用户验证的token，从UserLogin中获取[server ticket 或devicesn]
		 * @param firstNumber
		 *            history list beginning position（Initialized from zero）[从0开始计数，取历史记录的开始位置]
		 * @param countNumber
		 *            要拉取的历史记录个数[需要获取的总数]
		 * @return
		 */
		public GetConversationHistoryList(int firstNumber, int countNumber, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			mToken = WeaverService.getInstance().getUserLogic().getUserToken();
			mFirstNumber = firstNumber;
			mCountNumber = countNumber;
		}

		@Override
		public void run() {
			ConversationHistoryListJsonObject response = null;
			try {
				response = conversationHistoryList(mToken, mFirstNumber, mCountNumber);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleResponse(response);
		}

		private ConversationHistoryListJsonObject conversationHistoryList(String token, int firstNumber, int countNumber) throws NetworkErrorException
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setUrl("http://api" + WeaverBaseAPI.getENV().getServerTypeKeyWord() + ".com/1.0/conhistory/list.json");
			req.setMethod("POST");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", token);
			params.put("firstNumber", Integer.toString(firstNumber));
			params.put("countNumber", Integer.toString(countNumber));
			req.setBody(StringUtility.hashmapToParamStringByte(params));
			WeaverHttpConnection.sendHttp(req);

			return WeaverBaseAPI.dealResponse(req.getResponseCode(), req.getResponseData(), ConversationHistoryListJsonObject.class);

		}
	}
}
