package com.kinstalk.voip.sdk.api;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.kinstalk.voip.sdk.api.WeaverConstants.WeaverServerType;
import com.kinstalk.voip.sdk.interfaces.WeaverCallStateListener;
import com.kinstalk.voip.sdk.interfaces.WeaverMessageListener;
import com.kinstalk.voip.sdk.interfaces.WeaverRequestListener;
import com.kinstalk.voip.sdk.interfaces.WeaverUserSipStatusListener;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.data.DataService;
import com.kinstalk.voip.sdk.logic.WeaverAbstractLogic;
import com.kinstalk.voip.sdk.logic.allrelaypath.AllRelayPathLogic;
import com.kinstalk.voip.sdk.logic.config.ConfigLogic;
import com.kinstalk.voip.sdk.logic.contact.ContactLogic;
import com.kinstalk.voip.sdk.logic.conversation.ConversationLogic;
import com.kinstalk.voip.sdk.logic.image.ImageLogic;
import com.kinstalk.voip.sdk.logic.sip.SipLogic;
import com.kinstalk.voip.sdk.logic.sip.SipLogicApi;
import com.kinstalk.voip.sdk.logic.sip.aidl.model.CallInfo;
import com.kinstalk.voip.sdk.logic.user.UserConstants.UserStatus;
import com.kinstalk.voip.sdk.logic.user.UserLogic;
import com.kinstalk.voip.sdk.http.WeaverBaseAPI;
import com.kinstalk.voip.sdk.common.CollectionUtility;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.UserPreferences;

/**
 * SDK主要的接口类，提供SDK大部分接口。单例。需要初始化（init）。
 * WeaverService同时管理一个全局线程池，Request通过线程池的线程来执行。同时提供了直接执行runnable的接口。
 * 
 * 
 */
public class WeaverService
{
	private static final String TAG = "WeaverService";

	private static final class EventType
	{
		private static final int USER_STATE_CHANGE = 1;
		private static final int CALL_STATE_CHANGE = 2;
		private static final int INCOMING_MESSAGE = 3;
	}

	public static final WeaverService gInstance = new WeaverService();
	private LinkedList<WeakReference<WeaverUserSipStatusListener>> mUserStatusListeners = new LinkedList<WeakReference<WeaverUserSipStatusListener>>();
	private LinkedList<WeakReference<WeaverMessageListener>> mMessageListeners = new LinkedList<WeakReference<WeaverMessageListener>>();
	private LinkedList<WeakReference<WeaverCallStateListener>> mCallStateListeners = new LinkedList<WeakReference<WeaverCallStateListener>>();
	private HashMap<String, WeaverAbstractLogic> mLogicHandlers = new HashMap<String, WeaverAbstractLogic>();
	private Context mContext = null;
	private SipLogic mSipLogic = null;
	private UserLogic mUserLogic = null;
	private EventHandler mEventHandler = null;

	private int mUserSipStatus = UserStatus.OFFLINE;
	private CallInfo mCurrentCallInfo = null;

	private static final int CORE_POOL_SIZE = 3;// 线程池中可保留的线程数
	private static final int MAX_POOL_SIZE = 10;// 最大并发线程数
	private static final long THREAD_KEEPALIVE_TIME = 30L; // 线程的空闲时间

	/**
	 * 使用Concurrent实现的优先级队列来作为Request队列。此队列是基于数组的，无长度限制。 目前既能使用优先级，又必须是blocking的队列只有这一个。
	 */
	private final BlockingQueue<Runnable> mQueue = new PriorityBlockingQueue<Runnable>();

	/**
	 * 自定义的线程池，使用上面定义的参数和队列。线程池为所有业务共用。
	 */
	private ExecutorService mExecService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, THREAD_KEEPALIVE_TIME, TimeUnit.SECONDS, mQueue);

	public static final WeaverService getInstance()
	{
		return gInstance;
	}

	private WeaverService()
	{
		HandlerThread ht = new HandlerThread("Event Handler");
		ht.start();
		mEventHandler = new EventHandler(ht.getLooper());
	}

	public void init(Context context, WeaverServerType serverType)
	{
		 Log.d(TAG, "WeaverService init");
		 Log.d(TAG, "Weaver Engine SDK - Version Ceyes0.3b0713");
		if(mContext != null) {
			return;
		}
		UserPreferences.init(context);
		WeaverBaseAPI.setENV(serverType);
		mContext = context;

		DataService.getInstance().init(context);

		new ConversationLogic(context).start();
		mUserLogic = new UserLogic(context);
		mUserLogic.start();
		new ContactLogic(context).start();
		new AllRelayPathLogic(context).start();
		mSipLogic = new SipLogic(context);
		mSipLogic.start();
//		new CameraLogic(context).start();
//		new PicWallLogic(context).start();
//		new ApkInstallerLogic(context).start();
		new ConfigLogic(context).start();
		new ImageLogic(context).start();
	}
	
	public void initSdkBundledSip(Context context, WeaverServerType serverType, String userAgent, String inCallActivityName, Notification customNotification, int outGoingCallRingtoneId, WeaverRequestListener listener)
	{
		init(context, serverType);
		WeaverRequest req2 = SipLogicApi.sipInit(context, userAgent, inCallActivityName, customNotification, outGoingCallRingtoneId, listener);
		WeaverService.getInstance().dispatchRequest(req2);
	}

	/**
	 * 注册逻辑处理单元。通常都是逻辑处理单元在被初始化的时候自行注册。
	 * 
	 * @param logicUri
	 *            逻辑处理单元对应的URI
	 * @param logic
	 *            逻辑处理单元对象
	 */
	public void registerLogicHandler(URI logicUri, WeaverAbstractLogic logic)
	{
		if (logicUri == null)
		{
			 Log.d(TAG, "logicUri is null!");
			return;
		}

		if (logic == null)
		{
			 Log.d(TAG, "Logic Object is null!");
			return;
		}

		 Log.d(TAG, "WeaverService registerLogicHandler: " + logic.toString());

		synchronized (mLogicHandlers)
		{
			mLogicHandlers.put(logicUri.getHost(), logic);
		}
	}

	/**
	 * 注册用户状态通知。在用户自身（不包括好友）状态改变（上线或者下线）的时候会通知监听者。
	 * 
	 * @param listener
	 *            要注册的监听者对象
	 * @return 是否注册成功。true=成功。
	 */
	public boolean registerUserStatusListener(WeaverUserSipStatusListener listener)
	{
		 Log.d(TAG, "WeaverService registerUserStatusListener: " + listener.toString());
		boolean result = false;
		synchronized(mEventHandler)
		{
			result = CollectionUtility.add(mUserStatusListeners, listener);
		}
		if (result)
		{
			mEventHandler.sendMessage(mEventHandler.obtainMessage(EventType.USER_STATE_CHANGE, listener));
		}
		 Log.d(TAG, "WeaverService mUserStatusListeners size: " + mUserStatusListeners.size());
		return result;
	}

	/**
	 * 取消注册用户状态通知。
	 * 
	 * @param listener
	 *            要取消注册的监听者对象
	 * @return 是否取消成功。true=成功。
	 */
	public boolean unregisterUserStatusListener(WeaverUserSipStatusListener listener)
	{
		 Log.d(TAG, "WeaverService unregisterUserStatusListener: " + listener.toString());
		return CollectionUtility.remove(mUserStatusListeners, listener);
	}

	/**
	 * <此方法客户端不使用>
	 * 
	 * @param status
	 */
	public void onUserStatusChange(int status)
	{
		if (mUserSipStatus != status)
		{
			 Log.d(TAG, "WeaverService onUserStatusChange: " + status);
			mUserSipStatus = status;
			mEventHandler.sendMessage(mEventHandler.obtainMessage(EventType.USER_STATE_CHANGE));
		}
	}

	/**
	 * 注册通话状态通知，包括通话状态改变（来电、振铃、接通、结束等）都会通知。
	 * 
	 * @param listener
	 *            要注册的监听者对象
	 * @return 是否注册成功。true=成功。
	 */
	public boolean registerCallStateListener(WeaverCallStateListener listener)
	{
		 Log.d(TAG, "WeaverService registerCallStateListener: " + listener.toString());
		boolean result = CollectionUtility.add(mCallStateListeners, listener);
		if (result && mCurrentCallInfo != null)
		{
			mEventHandler.sendMessage(mEventHandler.obtainMessage(EventType.CALL_STATE_CHANGE, listener));
		}

		return result;
	}

	/**
	 * 取消通话状态通知。
	 * 
	 * @param listener
	 *            要取消注册的监听者对象
	 * @return 是否取消成功。true=成功。
	 */
	public boolean unregisteCallStateListener(WeaverCallStateListener listener)
	{
		 Log.d(TAG, "WeaverService unregisterCallStateListener: " + listener.toString());
		return CollectionUtility.remove(mCallStateListeners, listener);
	}

	/**
	 * <此方法客户端不使用>
	 * 
	 * @param status
	 */
	public void onCallStateChange(CallInfo call, int state)
	{
		 Log.d(TAG, "WeaverService onCallState: call=" + call.getCallConfiguration().getCallToken() + ", state=" + state);
		mCurrentCallInfo = call;
		mEventHandler.sendMessage(mEventHandler.obtainMessage(EventType.CALL_STATE_CHANGE));
	}

	/**
	 * 注册消息通知，包括接收消息和发送消息结果 通知。
	 * 
	 * @param listener
	 *            要注册的监听者对象
	 * @return 是否注册成功。true=成功。
	 */
	public boolean registerMessageStateListener(WeaverMessageListener listener)
	{
		 Log.d(TAG, "WeaverService registerMessageStateListener: " + listener.toString());
		return CollectionUtility.add(mMessageListeners, listener);
	}

	/**
	 * 取消消息通知。
	 * 
	 * @param listener
	 *            要取消注册的监听者对象
	 * @return 是否取消成功。true=成功。
	 */
	public boolean unregisteMessageStateListener(WeaverMessageListener listener)
	{
		 Log.d(TAG, "WeaverService unregisteMessageStateListener: " + listener.toString());
		return CollectionUtility.remove(mMessageListeners, listener);
	}

	/**
	 * <此方法客户端不使用>
	 * 
	 * TODO 未完成，需要补充Message对象，另外应该由数据管理模块通知事件。
	 */
	public void onMessage(int msgId, String msgGlobalId, final String sender, final String msgContent, String mimeType)
	{
		 Log.d(TAG, "WeaverService onMessage: ");

		synchronized (mMessageListeners)
		{
			Iterator<WeakReference<WeaverMessageListener>> i = mMessageListeners.iterator();
			WeaverMessageListener cur = null;
			while (i.hasNext())
			{
				cur = i.next().get();
				if (cur == null)
				{
					i.remove();
				}
				else
				{
					cur.onMessage(msgId, msgGlobalId, sender, msgContent, mimeType);
				}
			}
		}
	}

	/**
	 * <此方法客户端不使用>
	 * 
	 * TODO 未完成，需要补充Message对象，另外应该由数据管理模块通知事件。
	 */
	public void onMessageSentResult(int msgId, String msgGlobalId, String msgSentTime, String remote_number, boolean isSuccess, String msgContent, String mimeType, String reason)
	{
		 Log.d(TAG, "WeaverService onMessage: ");

		synchronized (mMessageListeners)
		{
			Iterator<WeakReference<WeaverMessageListener>> i = mMessageListeners.iterator();
			WeaverMessageListener cur = null;
			while (i.hasNext())
			{
				cur = i.next().get();
				if (cur == null)
				{
					i.remove();
				}
				else
				{
					cur.onMessageSentResult(msgId, msgGlobalId, msgSentTime, remote_number, isSuccess, msgContent, mimeType, reason);
				}
			}
		}
	}

	/**
	 * 分发Request请求，一旦发现对应的logic，则通过线程池执行。
	 * 
	 * @param req
	 */
	public final void dispatchRequest(WeaverRequest req)
	{
		 Log.d(TAG, "WeaverService dispatchRequest: " + req.getURI().toString());

		synchronized (mLogicHandlers)
		{
			WeaverAbstractLogic l = mLogicHandlers.get(req.getURI().getHost());
			if (l != null)
			{
				mExecService.execute(new RequestExecutable(l, req));
			}
		}
	}

	/**
	 * 提供了一个直接执行Runnable的方法，供进行一些特殊的逻辑或者回调。
	 * 
	 * @param runnable
	 */
	public final void execute(Runnable runnable)
	{
		 Log.d(TAG, "WeaverService execute: " + runnable.toString());

		mExecService.execute(runnable);
	}

	public void registerLogicModule(Class<? extends WeaverAbstractLogic> logicModule)
	{
		try
		{
			logicModule.getConstructor(Context.class).newInstance(mContext).start();
		}
		catch (Exception e)
		{
			 Log.e(TAG, "", e);
		}
	}

	public SipLogic getSipLogic()
	{
		return mSipLogic;
	}
	
	public UserLogic getUserLogic()
	{
		return mUserLogic;
	}

	private class EventHandler extends Handler
	{
		public EventHandler(Looper l)
		{
			super(l);
		}

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case EventType.USER_STATE_CHANGE:
				{
					synchronized(mEventHandler)
					{
					if (msg.obj == null)
					{
						msg.obj = mUserStatusListeners;
					}

					if (msg.obj instanceof LinkedList)
					{
						@SuppressWarnings("unchecked")
						LinkedList<WeakReference<WeaverUserSipStatusListener>> listeners = (LinkedList<WeakReference<WeaverUserSipStatusListener>>) msg.obj;
						// 通知所有的监听者
						synchronized (listeners)
						{
							 Log.d(TAG, "USER_STATE_CHANGE:listener count:" + listeners.size());
							Iterator<WeakReference<WeaverUserSipStatusListener>> i = listeners.iterator();
							WeaverUserSipStatusListener cur = null;
							while (i.hasNext())
							{
								cur = i.next().get();
								if (cur == null)
								{
									 Log.d(TAG, "USER_STATE_CHANGE:found dropped object: null");
									i.remove();
								}
								else
								{
									 Log.d(TAG, "USER_STATE_CHANGE:notifying object:" + cur.toString());
									cur.onStatusChange(mUserSipStatus);
								}
							}
						}
					}
					else
					{
						((WeaverUserSipStatusListener) msg.obj).onStatusChange(mUserSipStatus);
					}
					}

				}
					break;
				case EventType.CALL_STATE_CHANGE:
				{
					synchronized (mCallStateListeners)
					{
						CallInfo call = mCurrentCallInfo;

						if (msg.obj == null)
						{
							msg.obj = mCallStateListeners;
						}

						if (call != null)
						{
							if (msg.obj instanceof LinkedList)
							{
								@SuppressWarnings("unchecked")
								LinkedList<WeakReference<WeaverCallStateListener>> listeners = (LinkedList<WeakReference<WeaverCallStateListener>>) msg.obj;
								Iterator<WeakReference<WeaverCallStateListener>> i = listeners.iterator();
								WeaverCallStateListener cur = null;
								while (i.hasNext())
								{
									cur = i.next().get();
									if (cur == null)
									{
										i.remove();
									}
									else
									{
										cur.onCallStateChange(call, call.getCallState().swigValue());
									}
								}
							}
							else if (msg.obj instanceof WeaverCallStateListener)
							{
								((WeaverCallStateListener) msg.obj).onCallStateChange(call, call.getCallState().swigValue());
							}
						}
					}
				}
					break;
				case EventType.INCOMING_MESSAGE:
				{
					// TODO 后续将message也放入此handler分发
				}
					break;
			}
		}
	}

	private class RequestExecutable implements Runnable, Comparable<Object>
	{
		private final WeaverRequest mRequest;
		private final WeaverAbstractLogic mLogic;

		public RequestExecutable(WeaverAbstractLogic logic, WeaverRequest req)
		{
			if (logic == null || req == null)
			{
				throw new IllegalArgumentException("Illegal null parameters!");
			}
			mRequest = req;
			mLogic = logic;
		}

		@Override
		public int compareTo(Object another)
		{
			if (another instanceof RequestExecutable)
			{
				return mRequest.compareTo(((RequestExecutable) another).mRequest);
			}
			else
			{
				return mRequest.getPriority() - 1;
			}
		}

		@Override
		public void run()
		{
			try
			{
				mLogic.handleRequest(mRequest);
			}
			catch (Exception e)
			{
				 Log.e(TAG, "", e);
			}
			catch (Error e)
			{
				 Log.e(TAG, "", e);
			}
		}

	}
}
