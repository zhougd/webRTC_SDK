package com.kinstalk.voip.sdk.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

public class StringUtility
{
	private static final String TAG = "StringUtility";

	public static final String BOUNDARY = "--------httpmagicshow";
	public static final String TWO_HYPHENS = "--";
	public static final String HTTP_END = "\r\n";

	public static final String lastSubString(String source, int targetLen)
	{
		if (source == null)
		{
			 Log.d(TAG, "Source String can not be null!");
			return null;
		}
		else if (source.length() < targetLen)
		{
			 Log.d(TAG, "Target length excesses actual length!");
			return null;
		}
		else
		{
			 Log.d(TAG, "===" + source.length() + "|" + targetLen);
			return source.substring(source.length() - targetLen, source.length());
		}
	}

	public static String getInstanceId(Context ctx)
	{
		TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return "<" + imei + "_" + imei + ">";
	}

	public static String getHttpUserAgent(Context context, String userId)
	{
		String VALUE_SPLIT = "%5";
		String software = "Weaver";
		String version = getAppVersionName(context);
		String oemTag = getOemTag(context);
		String platform = "PHONE";
		String deviceVendor = android.os.Build.BRAND;
		String deviceModel = android.os.Build.MODEL;
		// PC,PHONE,PAD,TV
		String onlineDevice = "PHONE";
		StringBuilder ua = new StringBuilder();
		ua.append(software).append(VALUE_SPLIT).append(version == null ? "" : version).append(VALUE_SPLIT).append(oemTag).append(VALUE_SPLIT).append(platform).append(VALUE_SPLIT)
				.append(deviceVendor == null ? "" : deviceVendor).append(VALUE_SPLIT).append(deviceModel == null ? "" : deviceModel.replace(" ", "_")).append(VALUE_SPLIT).append(onlineDevice)
				.append(VALUE_SPLIT).append(userId);
		 Log.d(TAG, "getHttpUserAgent=" + ua);
		return ua.toString();
	}

	public static String getAppVersionName(Context context)
	{
		String versionName = null;
		try
		{
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
		}
		catch (NameNotFoundException e)
		{
			 Log.d(TAG, "Have't version name!");
		}
		catch (RuntimeException e)
		{
			 Log.d(TAG, "exception when get version name.");
		}
		 Log.d(TAG, "version name:" + versionName);
		return versionName == null ? "" : versionName;
	}

	public static String getOemTag(Context context)
	{
		String oem = "";
		try
		{
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			oem = appInfo.metaData.getString("lenovo:channel");
		}
		catch (NameNotFoundException e)
		{
			 Log.e(TAG, "Get OEM-tag failed!", e);
		}
		catch (RuntimeException e)
		{
			 Log.e(TAG, "Get OEM-tag failed!", e);
		}
		return oem;
	}

	public static final URI generateUri(String scheme, String host, String path)
	{
		URI result = null;
		try
		{
			result = new URI(scheme, host, path, null);
		}
		catch (URISyntaxException e)
		{
			 Log.d(TAG, "Generate URI failed!");
		}
		return result;
	}
	
	public static byte[] hashmapListToParamStringByte(HashMap<String, Object> params)
	{
		String result = hashmapListToParamString(params);

		if (result != null)
		{
			try
			{
				return result.getBytes("UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				 Log.e(TAG, "", e);
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	public static byte[] hashmapToParamStringByte(HashMap<String, String> params)
	{
		String result = hashmapToParamString(params);

		if (result != null)
		{
			try
			{
				return result.getBytes("UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				 Log.e(TAG, "", e);
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	/*
	 * 支持HashMap value为List<String> ,String[]
	 * */
	public static String hashmapListToParamString(HashMap<String, Object> params) {
		if (params == null || params.size() == 0) {
			return null;
		}

		boolean isFirstElement = true;
		StringBuilder sb = new StringBuilder();
		Iterator<String> i = params.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
			if (params.get(key) == null) {
				continue;
			}

			if (!isFirstElement) {
				sb.append("&");
			} else {
				isFirstElement = false;
			}

			Object value = params.get(key);

			if (value instanceof String) {
				sb.append(key);
				sb.append("=");
				try {
					sb.append(URLEncoder.encode((String) value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					 Log.e(TAG, "", e);
				}
			}else if (value instanceof List<?>) {
				try {
					sb.append(appendListParameters(key, (List<?>)value));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					 Log.e(TAG, "list Attributs UnsupportedEncoding", e);
				}
			}else if (value instanceof String[]) {
				try {
					sb.append(appendArrayParameters(key, (String[]) value));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					 Log.e(TAG, "array Attributs UnsupportedEncoding", e);
				}
			}else{
				 Log.d(TAG, "Attributs UnsupportedEncoding");
			}
		}

		return sb.toString();
	}

	/*
	 * 仅支持支持HashMap value为String 类型，如需支持Value为List<String> ,String[]使用hashmapListToParamString方法
	 * */
	public static String hashmapToParamString(HashMap<String, String> params) {
		if (params == null || params.size() == 0) {
			return null;
		}

		boolean isFirstElement = true;
		StringBuilder sb = new StringBuilder();
		Iterator<String> i = params.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
			if (params.get(key) == null) {
				continue;
			}

			if (!isFirstElement) {
				sb.append("&");
			} else {
				isFirstElement = false;
			}

			Object value = params.get(key);

			if (value instanceof String) {
				sb.append(key);
				sb.append("=");
				try {
					sb.append(URLEncoder.encode((String) value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					 Log.e(TAG, "", e);
				}
			}else{
				 Log.d(TAG, "Attributs UnsupportedEncoding");
			}
		}

		return sb.toString();
	}
	
	/**
	 * 
	 * @param key
	 * @param parameterArray
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String appendArrayParameters(String key, String[] parameterArray) throws UnsupportedEncodingException {
		StringBuilder paramsArray = new StringBuilder();
		boolean isFirstElement = true;
		if (parameterArray != null && parameterArray.length > 0) {
			for (String param : parameterArray) {
				if (!isFirstElement) {
					paramsArray.append("&");
				} else {
					isFirstElement = false;
				}
				
				paramsArray.append(key).append("=").append(URLEncoder.encode(param, "UTF-8"));
			}
			return paramsArray.toString();
		}
		return null;
	}

	
	/**
	 * @param key
	 * @param parameterList
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String appendListParameters(String key, List<?> parameterList)
			throws UnsupportedEncodingException {
		boolean isFirstElement = true;
		StringBuilder paramsList = new StringBuilder();
		if (parameterList != null && parameterList.size() > 0) {
			for (Object param : parameterList) {
				if (param instanceof String) {

					if (!isFirstElement) {
						paramsList.append("&");
					} else {
						isFirstElement = false;
					}

					paramsList.append(key).append("=")
							.append(URLEncoder.encode((String) param, "UTF-8"));
				}
			}
			return paramsList.toString();
		}
		return null;
	}

	public static byte[] hashmapToMultipartFormDataByte(HashMap<String, String> params, List<File> files, String fileType, String fileMimeType)
	{
		if (files == null || files.size() == 0)
		{
			return null;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			if (params != null && params.size() > 0)
			{
				Set<Entry<String, String>> keySet = params.entrySet();
				for (Map.Entry<String, String> entry : keySet)
				{
					String name = entry.getKey();
					String value = entry.getValue();
					dos.writeBytes(TWO_HYPHENS + BOUNDARY + HTTP_END);
					dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
					dos.writeBytes(HTTP_END);
					dos.writeBytes(URLEncoder.encode(value, "UTF-8") + HTTP_END);
				}
			}

			for (File file : files)
			{
				dos.writeBytes(TWO_HYPHENS + BOUNDARY + HTTP_END);
				dos.writeBytes("Content-Disposition: form-data; name=\"" + fileType + "\"; filename=\"" + URLEncoder.encode(fileType, "UTF-8") + "\"\r\n");
				dos.writeBytes("Content-Type: " + fileMimeType + HTTP_END);
				dos.writeBytes(HTTP_END);

				FileInputStream fileStream = new FileInputStream(file);
				byte[] buffer = new byte[1024 * 5];
				int length = 0;
				while ((length = fileStream.read(buffer)) != -1)
				{
					dos.write(buffer, 0, length);
				}

				dos.writeBytes(HTTP_END);
				
				fileStream.close();
			}

			dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + HTTP_END + HTTP_END);
			
			return baos.toByteArray();
		}
		catch (Exception e)
		{
			 Log.e(TAG, "", e);
		}
		finally
		{
			try
			{
				if (dos != null)
				{
					dos.close();
				}
			}
			catch (Exception e)
			{
				 Log.e(TAG, "", e);
			}

			try
			{
				if (baos != null)
				{
					baos.close();
				}
			}
			catch (Exception e)
			{
				 Log.e(TAG, "", e);
			}
		}

		return null;
	}
}
