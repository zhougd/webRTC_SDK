package com.kinstalk.voip.sdk.common;

import android.content.Context;

/**
 * 用户储存配置文件，通过SharedPreferences实现。
 * 
 * 在跨进程的使用中，不能保存getSharedPreferences获取的对象，否则会导致进程间数据不一致。请在每次读写之前通过getSharedPreferences方法进行临时获取。
 * 
 * @author luolong1
 * 
 */
public class UserPreferences
{
	public static final class Key
	{
		public static final String USER_ID = "user_id";
		public static final String USER_DOMAIN = "user_domain";
		public static final String USER_PASSWORD = "user_password";
		public static final String IS_AUTO_REGISTER = "is_auto_register";
		public static final String AUDIO_CONFIG = "audio_config";
		public static final String USER_TOKEN = "user_token";
		public static final String USER_WEAVER_ID = "user_weaver_id";
		public static final String DATABASEVERSION_SUPPORTEDDATATYPEHASHCODE = "DataBaseVersion_mDatabaseSupportedDataTypeHashCode";
		public static final String DATABASEVERSION_NUMBER = "DataBaseVersion_mDatabaseVersion";
	}

	private static final Class<?> TAG = UserPreferences.class.getClass();

	private static Context gContext;
	private static String gDesiredPreferencesFileName = "weaver-preference-file";

	public static synchronized void init(Context context)
	{
		gContext = context;
		String desiredPreferencesFileName = context.getApplicationInfo().packageName;
		if (desiredPreferencesFileName != null && !"".equals(desiredPreferencesFileName)) {
		    gDesiredPreferencesFileName = desiredPreferencesFileName.replace(".", "-");
		}
		 Log.d(TAG, "[init] gDesiredPreferencesFileName=" + gDesiredPreferencesFileName);
	}

	public static synchronized void setString(String key, String value)
	{
		 Log.d(TAG, "[setString] key=" + key + ", value=" + value);
		if (gContext != null)
		{
			gContext.getSharedPreferences(gDesiredPreferencesFileName, Context.MODE_MULTI_PROCESS).edit().putString(key, value).commit();
		}
		else
		{
			 Log.d(TAG, "[setString] Context is null!");
		}
	}

	public static synchronized String getString(String key)
	{
		if (gContext != null)
		{
			return gContext.getSharedPreferences(gDesiredPreferencesFileName, Context.MODE_MULTI_PROCESS).getString(key, "");
		}
		else
		{
			 Log.d(TAG, "[getString] Context is null!");
			return "";
		}
	}

	public static synchronized void setInt(String key, int value)
	{
		 Log.d(TAG, "[setInt] key=" + key + ", value=" + value);
		if (gContext != null)
		{
			gContext.getSharedPreferences(gDesiredPreferencesFileName, Context.MODE_MULTI_PROCESS).edit().putInt(key, value).commit();
		}
		else
		{
			 Log.d(TAG, "[setInt] Context is null!");
		}
	}

	public static synchronized int getInt(String key, int defaultValue)
	{
		if (gContext != null)
		{
			return gContext.getSharedPreferences(gDesiredPreferencesFileName, Context.MODE_MULTI_PROCESS).getInt(key, defaultValue);
		}
		else
		{
			 Log.d(TAG, "[getInt] Context is null!");
			return defaultValue;
		}
	}

	public static synchronized void setBool(String key, boolean value)
	{
		 Log.d(TAG, "[setBool] key=" + key + ", value=" + value);
		if (gContext != null)
		{
			gContext.getSharedPreferences(gDesiredPreferencesFileName, Context.MODE_MULTI_PROCESS).edit().putBoolean(key, value).commit();
		}
		else
		{
			 Log.d(TAG, "[setBool] Context is null!");
		}
	}

	public static synchronized boolean getBool(String key, boolean defaultValue)
	{
		if (gContext != null)
		{
			return gContext.getSharedPreferences(gDesiredPreferencesFileName, Context.MODE_MULTI_PROCESS).getBoolean(key, defaultValue);
		}
		else
		{
			 Log.d(TAG, "[getBool] Context is null!");
			return defaultValue;
		}
	}
}
