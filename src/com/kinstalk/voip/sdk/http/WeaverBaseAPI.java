package com.kinstalk.voip.sdk.http;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;
import com.kinstalk.voip.sdk.api.WeaverConstants.WeaverServerType;
import com.kinstalk.voip.sdk.common.Log;

public class WeaverBaseAPI
{
	private static WeaverServerType ENV = WeaverServerType.ES_QINJIAN_ONLINE_SERVER;

//	private static final ObjectMapper mapper = new ObjectMapper();
//	static
//	{
//		mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//	}

	public synchronized static void setENV(WeaverServerType eNV)
	{
		ENV = eNV;
		 Log.d("WeaverBaseAPI", "setENV: " + ENV.getEsServerType().toString() + ":" + ENV.getServerTypeKeyWord());
	}

	public static <T> T bytesToJsonObject(byte[] src, Class<T> classOfT)
	{
		try
		{
			//T result = mapper.readValue(src, classOfT);
		    T result = new Gson().fromJson(new String(src, "UTF-8"), classOfT);
			return result;
		}
		catch (Exception e)
		{
			 Log.e("WeaverBaseAPI", "", e);
			return null;
		}
	}

	public synchronized static WeaverServerType getENV()
	{
		return ENV;
	}

	/**
	 * 除非传入的Model类没有空构造函数，否则本方法也不会返回null
	 * 
	 * @param returnCode
	 * @param returnData
	 * @param targetClass
	 * @return
	 */
	public static final <T extends AbstractJsonObject> T dealResponse(int returnCode, byte[] returnData, Class<T> targetClass)
	{
		if (returnCode == 200)
		{
			T o = bytesToJsonObject(returnData, targetClass);
			if (o != null) {
				o.putToDB();
			}
			return o;
		}
		else
		{
			T result = null;
			try
			{
				result = targetClass.newInstance();
				result.setError_code(String.valueOf(returnCode));
			}
			catch (InstantiationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}
	}
	
	public static final <T extends AbstractJsonObject> T dealResponse2(T objectForPostProcess, int returnCode, byte[] returnData, Class<T> targetClass)
	{
		if (returnCode == 200)
		{
			T o = bytesToJsonObject(returnData, targetClass);
			if (o != null && o.getError_code() == null) {
				o.postProcess(objectForPostProcess);
				o.putToDB();
			}
			return o;
		}
		else
		{
			T result = null;
			try
			{
				result = targetClass.newInstance();
				result.setError_code(String.valueOf(returnCode));
			}
			catch (InstantiationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}
	}

}
