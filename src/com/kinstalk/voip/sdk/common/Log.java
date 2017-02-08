package com.kinstalk.voip.sdk.common;

public final class Log
{
	private static String ROOT_TAG = "JPA";

	public static void setLogTag(String tag)
	{
		ROOT_TAG = tag;
	}

	public static final int e(String tag, String msg)
	{
		return android.util. Log.d(ROOT_TAG, "==" + tag + "==\t\t" + msg);
	}

	public static final int w(String tag, String msg)
	{
		return android.util.Log.w(ROOT_TAG, "==" + tag + "==\t\t" + msg);
	}

	public static final int i(String tag, String msg)
	{
		return android.util.Log.i(ROOT_TAG, "==" + tag + "==\t\t" + msg);
	}

	public static final int d(String tag, String msg)
	{
		return android.util.Log.d(ROOT_TAG, "==" + tag + "==\t\t" + msg);
	}

	public static final int v(String tag, String msg)
	{
		return android.util.Log.v(ROOT_TAG, "==" + tag + "==\t\t" + msg);
	}

	public static final int e(String tag, String msg, Throwable tr)
	{
		return android.util.Log.w(ROOT_TAG, "==" + tag + "==\t\t" + msg, tr);
	}

	public static final int i(Class<?> c, String msg)
	{
		return android.util.Log.i(ROOT_TAG, "[" + c.getSimpleName() + "]\t\t" + msg);
	}

	public static final int d(Class<?> c, String msg)
	{
		return android.util.Log.d(ROOT_TAG, "[" + c.getSimpleName() + "==\t\t" + msg);
	}

	public static final int w(Class<?> c, String msg)
	{
		return android.util.Log.w(ROOT_TAG, "[" + c.getSimpleName() + "==\t\t" + msg);
	}

	public static final int v(Class<?> c, String msg)
	{
		return android.util.Log.v(ROOT_TAG, "[" + c.getSimpleName() + "==\t\t" + msg);
	}

	public static final int e(Class<?> c, String msg)
	{
		return android.util. Log.d(ROOT_TAG, "==" + c.getSimpleName() + "==\t\t" + msg);
	}

	public static final int e(Class<?> c, String msg, Throwable tr)
	{
		return android.util.Log.w(ROOT_TAG, "==" + c.getSimpleName() + "==\t\t" + msg, tr);
	}
}
