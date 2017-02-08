package com.kinstalk.voip.sdk.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;

public class WeaverHttpConnection
{
	private static final Class<?> TAG = WeaverBaseAPI.class.getClass();
	private static String gUserAgent = "";

	public static void sendHttp(WeaverHttpRequest req)
	{
		sendHttp(req, false);
	}

	public static void sendMultipartFormdata(WeaverHttpRequest req)
	{
		sendHttp(req, true);
	}

	private static void sendHttp(WeaverHttpRequest req, boolean isMultipartFormdata)
	{
		// 将返回值默认置为错误，如果接下来的逻辑导致没有新的ReturnCode被set，那么则按照此错误码返回
		req.setResponseCode(WeaverConstants.RequestReturnCode.NETWORK_FAILURE);

		InputStream is = null;
		OutputStream os = null;
		ByteArrayOutputStream baos = null;
		HttpURLConnection connection = null;
		try
		{
			 Log.d(TAG, "--------------------------Sending Server Message--------------------------");
			 Log.d(TAG, "[ HTTP " + req.getMethod() + " ] " + req.getUrl());

			// 创建Http连接
			URL url = new URL(req.getUrl());
			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod(req.getMethod());
			if (isMultipartFormdata)
			{
				connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + StringUtility.BOUNDARY);
			}
			else if ("POST".endsWith(req.getMethod()))
			{
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			}

			// 处理通用的HTTP头
			connection.setRequestProperty("Accept-Encoding", "UTF-8");
			connection.setRequestProperty("Content-Length", String.valueOf(req.getBody() != null ? req.getBody().length : 0));
			connection.setRequestProperty("User-Agent", gUserAgent);
			 Log.d(TAG, "[ UA ] " + gUserAgent);
			if (!isMultipartFormdata)
			{
				connection.setConnectTimeout(20000);
				connection.setReadTimeout(30000);
			}

			// 用户自定义的HTTP头
			Enumeration<String> e = req.getHeaders().keys();
			if (e != null)
			{
				String propertyKey = null;
				String value = null;
				while (e.hasMoreElements())
				{
					propertyKey = e.nextElement();
					value = req.getHeaders().get(propertyKey);
					connection.addRequestProperty(propertyKey, value);
					 Log.d(TAG, "[ HEAD ] " + propertyKey + " = " + value);
				}
			}

			// 发送BODY
			if (req.getBody() != null && req.getBody().length > 0)
			{
				try
				{
					 Log.d(TAG, "[ BODY ] " + new String(req.getBody(), "UTF-8"));
				}
				catch (Exception e1)
				{
					 Log.d(TAG, "[ BODY (binary) ] ");
				}

				os = connection.getOutputStream();
				os.write(req.getBody());
			}
			else

			{
				 Log.d(TAG, "[ BODY NULL ] ");
			}

			// 取得返回码
			req.setResponseCode(connection.getResponseCode());

			// 取回返回数据
			byte[] buffer;
			int len;
			buffer = new byte[128];
			len = 0;
			baos = new ByteArrayOutputStream();

			try
			{
				is = connection.getInputStream();
				while ((len = is.read(buffer)) > 0)
				{
					baos.write(buffer, 0, len);
				}
				req.setResponseData(baos.toByteArray());
			}
			catch (Exception e1)
			{
				// no response
			}

			// cr.setResponseLength(mHttpAgent.getLength());
			req.setContentType(connection.getHeaderField("Content-Type"));

			try
			{
				 Log.d(TAG, "[ REPONSE CODE ] " + req.getResponseCode());

				if (req.getResponseData() == null || req.getResponseData().length == 0)
				{
					 Log.d(TAG, "[ NULL REPONSE DATA ] ");
				}
				else if (req.getContentType() != null && req.getContentType().toLowerCase(Locale.ENGLISH).startsWith("text"))
				{
					String newlog = new String(req.getResponseData(), "UTF-8");
					 Log.d(TAG, "[ REPONSE DATA ] " + newlog);
				}
				else
				{
					 Log.d(TAG, "[ REPONSE DATA TYPE = " + req.getContentType() + "] ");
					 Log.d(TAG, "[ REPONSE DATA (binary) ] ");
//					String newlog = new String(req.getResponseData(), "UTF-8");
//					 Log.d(TAG, "[ REPONSE DATA ] " + newlog);
				}
			}
			catch (Exception ex)
			{
				 Log.e(TAG, "", ex);
			}

			 Log.d(TAG, "--------------------------------------------------------------------------");
		}
		catch (Exception ex)
		{
			 Log.e(TAG, "", ex);
		}
		finally
		{
			// 关闭连接并显式地置空，以解决某些VM的bug
			try
			{
				if (baos != null)
				{
					baos.close();
				}

				if (connection != null)
				{
					connection.disconnect();
				}
			}
			catch (Exception e)
			{
				 Log.e(TAG, "", e);
			}

			is = null;
			os = null;
			baos = null;
		}
	}

	public static void setUserAgent(String userAgent)
	{
		gUserAgent = userAgent;
	}
}
