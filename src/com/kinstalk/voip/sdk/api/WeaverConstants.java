package com.kinstalk.voip.sdk.api;

import com.kinstalk.voip.sdk.EngineSdkServerType;

/**
 * 
 * 全局常量文件，存放代码中使用的全局常量，除非业务修改，不然此部分常量应该非常稳定。如果有需要根据运行时修改的属性，则应该放入{@link core.lib.CoreConfig}。
 * 
 * @author luolong1
 * 
 */
public class WeaverConstants
{
	public static final String WEAVER_TAG = "WeaverSDK";
	
	public static final String WEAVER_SCHEME = "weaver";

	public class RequestReturnCode
	{
		public static final int OK = 200;
		public static final int SERVER_RETURNED_ERROR = 202;
		public static final int FATAL_ERROR = -999;

		public static final int NETWORK_FAILURE = 700;
		public static final int TOKEN_INVALID = 800;
	}
	
	public static enum WeaverServerType {
		  ES_OFFICIAL_ONLINE_SERVER(".kinstalk", EngineSdkServerType.ES_OFFICIAL_ONLINE_SERVER),
		  ES_TESTER_TESTING_SERVER(".test.kinstalk", EngineSdkServerType.ES_TESTER_TESTING_SERVER),
		  ES_ENGINEER_DEVELOPMENT_SERVER(".dev.kinstalk", EngineSdkServerType.ES_ENGINEER_DEVELOPMENT_SERVER),
		  ES_PAPA_ONLINE_SERVER(".kinstalk", EngineSdkServerType.ES_PAPA_ONLINE_SERVER),
		  ES_QINJIAN_ONLINE_SERVER(".kinstalk", EngineSdkServerType.ES_QINYOUYUE_ONLINE_SERVER),
		  ES_QINJIAN_TEST_SERVER(".test.kinstalk", EngineSdkServerType.ES_QINYOUYUE_TEST_SERVER),
		  ES_QINJIAN_DEV_SERVER(".dev.kinstalk", EngineSdkServerType.ES_QINYOUYUE_DEV_SERVER);
		  String mServerTypeKeyWord;
		  EngineSdkServerType mEsServerType;
		  WeaverServerType(String keyword, EngineSdkServerType esServerType) {
			  mServerTypeKeyWord = keyword;
			  mEsServerType = esServerType;
		  }
		  
		  public String getServerTypeKeyWord() {
			  return mServerTypeKeyWord;
		  }
		  
		  public EngineSdkServerType getEsServerType() {
			  return mEsServerType;
		  }
	}

}