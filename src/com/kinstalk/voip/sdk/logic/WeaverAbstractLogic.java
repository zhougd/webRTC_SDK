package com.kinstalk.voip.sdk.logic;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.net.Uri;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.http.AbstractJsonObject;
import com.kinstalk.voip.sdk.common.AbtractMemCacheDataItem;
import com.kinstalk.voip.sdk.common.MemoryCache;

/**
 * 目前修改为通过静态线程池来处理请求。线程池为所有Logic共享，ThreadPoolExecutor接口使用BlockingQueue来实现
 * 
 * @author luolong1
 * 
 */
public abstract class WeaverAbstractLogic extends Thread
{
	private static final int CORE_POOL_SIZE = 2;
	private static final int MAX_POOL_SIZE = 4;
	private static final long THREAD_KEEPALIVE_TIME = 1000L; // in seconds

	private static final BlockingQueue<Runnable> mQueue = new PriorityBlockingQueue<Runnable>();// 默认构造的长度是Integer.MAX_VALUE
	private static ExecutorService mExecService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, THREAD_KEEPALIVE_TIME, TimeUnit.SECONDS, mQueue);

	private final Context mContext;
	
	private final static MemoryCache<Uri, AbtractMemCacheDataItem> mJsonObjectMemoryCache = new MemoryCache<Uri, AbtractMemCacheDataItem>();
	
	public static MemoryCache<Uri, AbtractMemCacheDataItem> getMemCache() {
		return mJsonObjectMemoryCache;
	}

	public WeaverAbstractLogic(Context context)
	{
		assert context != null : "Context can not be null!";
		mContext = context;

	}

	public Context getContext()
	{
		return mContext;
	}

	/**
	 * 主要接口：向SDK发送请求。请求执行结果会异步通知，同时如果引起了数据变化，会再通过数据变化接口进行通知。
	 * 
	 * @param req
	 *            要执行的请求。
	 */
	public final void handleRequest(WeaverRequest req)
	{
		// 因为引入了线程池，此方法退化。（也可以和doHandleRequest合并，直接让具体的logic实现即可）
		doHandleRequest(req);
	}

	abstract public void doHandleRequest(WeaverRequest req);

	protected final void handleResponse(WeaverRequest req, AbstractJsonObject result)
	{
		if (result != null)
		{
			if (result.getError_code() != null)
			{
				req.setResponse(WeaverConstants.RequestReturnCode.SERVER_RETURNED_ERROR, result);
			}
			else
			{
				req.setResponse(WeaverConstants.RequestReturnCode.OK, result);
			}
		}
		else
		{
			req.setResponse(WeaverConstants.RequestReturnCode.SERVER_RETURNED_ERROR, result);
		}
	}

}
