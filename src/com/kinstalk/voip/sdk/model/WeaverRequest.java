package com.kinstalk.voip.sdk.model;

import java.net.URI;
import java.util.Enumeration;
import java.util.Hashtable;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.interfaces.WeaverRequestListener;
import com.kinstalk.voip.sdk.http.AbstractJsonObject;
import com.kinstalk.voip.sdk.common.Log;

public class WeaverRequest implements Comparable<WeaverRequest>
{
	public static final String TAG = "WeaverRequest";
	public static final int PRIORITY_LOW = -1;
	public static final int PRIORITY_NORMAL = 0;
	public static final int PRIORITY_HIGH = 1;
	public static final int PRIORITY_URGENT = 127;

	public static final int STATUS_NEW = 0;
	public static final int STATUS_SUCCEED = 1;
	public static final int STATUS_FAILED = 2;

	protected URI mTargetURI;

	private int mResponseCode = STATUS_NEW;
	private int mPriority = PRIORITY_NORMAL;
	private final long mId;
	private String mTag;
	private Object mResponse;
	private WeaverRequestListener mRequestCallback;
	private Hashtable<String, Object> mParameters;
	private boolean mResponseFlag = true;
	private WeaverRequest mLinkedRequest;

	/**
	 * 构造方法。
	 * 
	 * @param senderClassName
	 *            构造<code>Request</code>的类对象名
	 * @param requestType
	 *            <code>Request</code>的类型
	 * @param actionType
	 *            <code>Request</code>的动作类型
	 */
	public WeaverRequest(URI requestType, WeaverRequestListener callback)
	{
		mTargetURI = requestType;
		mRequestCallback = callback;
		mId = System.currentTimeMillis();
	}
	
	protected void setParam(URI requestType, WeaverRequestListener callback) {
		mTargetURI = requestType;
		mRequestCallback = callback;
	}
	
	public void run() {

	}
	
	public WeaverRequest() {
		mId = System.currentTimeMillis();
	}

	public void setRequestListener(WeaverRequestListener callback){
		mRequestCallback = callback;
	}
	
	public long getId()
	{
		return mId;
	}

	/**
	 * 往<code>Request</code>中添加一个参数
	 * 
	 * @param key
	 *            参数标识，取自<code>Constants</code>中以PARA开头的字段
	 * @param value
	 *            参数值
	 */
	public final void addParameter(String key, Object value)
	{
		if (key == null)
		{
			throw new IllegalArgumentException("The key can not be null!");
		}

		if (value != null)
		{
			// Lazy load
			if (mParameters == null)
			{
				mParameters = new Hashtable<String, Object>();
			}

			mParameters.put(key, value);
		}
	}

	public final synchronized boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}

		if (obj instanceof WeaverRequest)
		{
			WeaverRequest req = (WeaverRequest) obj;

			if (mTargetURI == req.mTargetURI)
			{

				if (req.mParameters.size() == 0 && mParameters.size() == 0)
				{
					return true;
				}

				if (req.mParameters.size() != mParameters.size())
				{
					return false;
				}

				Enumeration<String> e = req.mParameters.keys();
				while (e.hasMoreElements())
				{
					String key = e.nextElement();
					Object a, b;
					a = req.mParameters.get(key);
					b = mParameters.get(key);
					if (a == b || (a == null && b == null))
					{
						continue;
					}
					else
					{
						if (a == null || b == null || !a.equals(b))
						{
							return false;
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	public int hashCode()
	{
		// 没有制定hashCode规则，有需求的时候再考虑
		throw new IllegalArgumentException("Request is not proposed to be put into any hash container.");
	}

	public String toString()
	{
		return "";
	}

	/**
	 * 取得一个参数值
	 * 
	 * @param key
	 *            参数标识
	 * @return 参数值。如果没找到该参数，则返回null
	 */
	public final Object getParameter(String key)
	{
		if (mParameters == null)
		{
			return null;
		}
		else
		{
			return mParameters.get(key);
		}
	}

	/**
	 * 取得该<code>Request</code>的优先值
	 * 
	 * @return 该<code>Request</code>的优先值
	 */
	public final int getPriority()
	{
		return mPriority;
	}

	/**
	 * 取得该<code>Request</code>的类型常量。
	 * 
	 * @return 该<code>Request</code>的类型常量。常量表见<code>Constants</code>
	 */
	public final URI getURI()
	{
		return mTargetURI;
	}

	/**
	 * 取得一个结果数据。
	 * 
	 * @param key
	 *            结果数据的标识
	 * @return 结果数据
	 */
	public final synchronized Object getResponse()
	{
		return mResponse;
	}
	
	public final synchronized <T> T getResponse(Class<T> clazz)
	{
		return clazz.cast(mResponse);
	}

	public boolean isSameUriWith(URI uri) {
		if (mTargetURI.equals(uri)) {
			return true;
		}
		return false;
	}
	

	/**
	 * 取得返回结果状态码
	 * 
	 * @return 返回结果状态码
	 */
	public final int getResponseCode()
	{
		return mResponseCode;
	}

	/**
	 * 设置该<code>Request</code>的优先值
	 * 
	 * @param priority
	 *            要设置的优先值
	 */
	public final void setPriority(int priority)
	{
		mPriority = priority;
	}

	/**
	 * 设置该<code>Request</code>的状态，并在状态改变后通知所有的监听者。
	 * 因为引入了线程池，故回调不再单独启用线程，直接在当前线程回调。
	 * 
	 * @param responseCode
	 *            返回状态码
	 * @param response
	 *            server返回的数据
	 */
	public final synchronized void setResponse(final int responseCode, Object response)
	{
		// 只能响应一次
		if (mResponseFlag)
		{
			mResponseFlag = false;
			mResponse = response;
			mResponseCode = responseCode;
			if (mRequestCallback != null)
			{
				mRequestCallback.onRequestFinshed(this);
			}
		}
		else
		{
			 Log.d(TAG, "setResponse() can be called only once for a single request~!");
		}
	}
	
	public final synchronized void preSetResponse(final int responseCode, Object response)
	{
		if (mResponseFlag)
		{
			mResponse = response;
			mResponseCode = responseCode;
			if (mRequestCallback != null)
			{
				mRequestCallback.onRequestFinshed(this);
			}
		}
		else
		{
			 Log.d(TAG, "preSetResponse() can be called only before handleResponse!");
		}
	}
	
	public final void handleResponse(AbstractJsonObject result)
	{
		if (result != null)
		{
			if (result.getError_code() != null)
			{
				setResponse(WeaverConstants.RequestReturnCode.SERVER_RETURNED_ERROR, result);
			}
			else if (result.getmShouldPersist() == true)
			{
				setResponse(WeaverConstants.RequestReturnCode.OK, result);
			}
		}
		else
		{
			setResponse(WeaverConstants.RequestReturnCode.NETWORK_FAILURE, result);
		}
	}
	
	public final void preHandleResponse(AbstractJsonObject result)
	{
		if (result != null)
		{
			if (result.getError_code() != null)
			{
				preSetResponse(WeaverConstants.RequestReturnCode.SERVER_RETURNED_ERROR, result);
			}
			else
			{
				preSetResponse(WeaverConstants.RequestReturnCode.OK, result);
			}
		}
		else
		{
			preSetResponse(WeaverConstants.RequestReturnCode.NETWORK_FAILURE, result);
		}
	}

	public WeaverRequest getLinkedRequest()
	{
		return mLinkedRequest;
	}

	public void setLinkedRequest(WeaverRequest linkedRequest)
	{
		mLinkedRequest = linkedRequest;
	}

	public String getmTag()
	{
		return mTag;
	}

	public void setmTag(String mTag)
	{
		this.mTag = mTag;
	}

	public WeaverRequestListener getRequestCallback()
	{
		return mRequestCallback;
	}

	@Override
	public int compareTo(WeaverRequest another)
	{
		return (mPriority - another.getPriority());
	}
}
