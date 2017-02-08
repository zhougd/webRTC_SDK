package com.kinstalk.voip.sdk.logic.sip.service;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.kinstalk.voip.sdk.logic.sip.delegate.CallDataListener;

/**
 * <p>
 * SipService并不知道客户端最终用哪个具体的Activity类作为实际的通话显示界面，在这样的情况下，为了能够实现更好的进程隔离，因此就需要这样一个中间的Activity抽象类。
 * </p>
 * <p>
 * 首先解释一下进程隔离。因为实际通话界面所使用的那个具体的Activity类——为了表述方便，假定这个类名为TheInCallActivity，
 * 当然这个名字最后是由客户端工程决定的——是由客户端工程持有的， 因此客户端可以随意地在客户端进程启动TheInCallActivity。
 * 但是若是在客户端进程启动TheInCallActivity实例，而客户端进程是无法直接访问SipService的 ，那么这个TheInCallActivity
 * 实际上是无法完成期望的通话界面显示工作的。 所以最好是有一种设计，使得TheInCallActivity只能在SDK的进程中被启动， 如果尝试在客户端进程中被实例化，则会抛出异常。
 * </p>
 * <p>
 * 其实达到这个目的非常简单，只需要在TheInCallActivity的onCreate方法中做一下判断，是否能取到SipService实例即可。若不能取到SipService实例，
 * 则说明此时TheInCallActivity 的实例没有存在于SDK的进程中，此时这个实例没有存在的价值。
 * </p>
 * <p>
 * 但是TheInCallActivity是由客户端工程所持有，SDK并不能直接控制其代码逻辑。若仅仅是通过文档或者接口描述对此做限定，那么客户端工程很有可能忽略了这个规则，
 * 导致一些不可预见的情况发生。
 * </p>
 * <p>
 * 所以为了更好的控制客户端代码逻辑，实现进程隔离，在此增加一个TheInCallActivity的抽象类AbstractInCallActivity。作用如下：<br>
 * 1、缩小SipService实例的作用域，使得其不能被除了AbstractInCallActivity以外的其他类直接访问（其他类只能通过bindService接口访问，
 * 无法取到SipService的实例 ），因此客户端若想取得通话数据， 只能继承AbstractInCallActivity；<br>
 * 2、AbstractInCallActivity在onCreate方法中完成对进程中SipService实例的判断，若无法取得此实例，则抛出异常中止自身的实例化。<br>
 * </p>
 * 
 * @author LuoLong
 * 
 */
public abstract class AbstractInCallActivity extends Activity implements CallDataListener
{
	private EngineLoader mLoader;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mHandler = new Handler(getMainLooper());

		mLoader = EngineLoader.getInstance();

		// 修改了注册监听的位置，将其置前。子类需要在onCreate方法和onCallStateChange方法加上同步锁，
		// 保证这两个方法串行执行，否则可能会产生一些状态错乱。
		//mLoader.addEngineListener(this);
		mLoader.getDataReceiver().setListener(this);
	}

	protected EngineLoader getLoader()
	{
		return mLoader;
	}

	protected Handler getMainHandler()
	{
		return mHandler;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		//mLoader.removeEngineListener(this);
	}
}
