package com.kinstalk.voip.sdk.data;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.kinstalk.voip.sdk.data.DataEvent.DataEventType;
import com.kinstalk.voip.sdk.data.model.AbstractDataItem;
import com.kinstalk.voip.sdk.common.Log;

public class DataService extends Thread
{
	private static final String TAG = "DataService";
	public static final DataService gInstance = new DataService();
	private Context mContext = null;
	private final LinkedList<DataEvent> mEventList = new LinkedList<DataEvent>();
	private int mState = 0;

	public static final DataService getInstance()
	{
		return gInstance;
	}

	private DataService()
	{

	}

	public void init(Context context)
	{
		 Log.d(TAG, "init");

		mContext = context;
		DatabaseHelper.setContext(context);
		this.start();
	}

	private void sendDataEvent(DataEvent event)
	{
		synchronized (mEventList)
		{
			mEventList.addLast(event);
			mEventList.notifyAll();
		}
	}

	@Override
	public void run()
	{
		try
		{
			DataEvent event = null;

			while (mState != -1)
			{
				try
				{
					if (event == null)
					{
						synchronized (mEventList)
						{
							if (mEventList.size() == 0)
							{
								mEventList.wait();
								continue;
							}
							else
							{
								event = mEventList.pop();
							}
						}
					}
					handleDataEvent(event);
					event = null;
				}
				catch (InterruptedException ie)
				{
					 Log.e(TAG, "The module thread is interrupted", ie);
					// 如果取到了Request并且未处理完，则继续处理
				}
				catch (Exception e)
				{
					 Log.e(TAG, "", e);
					event = null;
				}
			}
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

	private void handleDataEvent(DataEvent event) throws SQLException
	{
		// 此处先将数据存入数据库，然后根据数据库的存入结果来判断是update or add
		// 不过照之前的讨论，可以先完全按照event的type来通知上层

		List<? extends AbstractDataItem> datas = event.getDatas();
		AbstractDataItem data = event.getData();
		Class<? extends AbstractDataItem> observeType = event.getObserveType();
		Iterator<? extends AbstractDataItem> i = null;
		AbstractDataItem a;
		HashSet<Uri> s = new HashSet<Uri>();
		
		if (observeType != null) {
			s.add(DataService.getInstance().getUri(observeType));
		}
		
		if (data != null) {
			s.add(data.getUri());
			if (event.getEventType() == DataEventType.ADD) {
				DataService.getInstance().createOrUpdate(data);
			} else if (event.getEventType() == DataEventType.REMOVE) {
				DataService.getInstance().delete(data);
			}
		}
		
		if (datas != null) {
			i = datas.iterator();
			while (i.hasNext()) {
				a = i.next();
				s.add(a.getUri());
				if (event.getEventType() == DataEventType.ADD) {
					DataService.getInstance().createOrUpdate(a);
				} else if (event.getEventType() == DataEventType.REMOVE) {
					DataService.getInstance().delete(a);
				}
			}
		}

		for (Uri u : s)
		{
			mContext.getContentResolver().notifyChange(u, null);
		}

	}

	public void putDataItem(AbstractDataItem data)
	{
		DataEvent event = new DataEvent(DataEvent.DataEventType.ADD, data);
		sendDataEvent(event);
	}
	
	public void delteDataItem(AbstractDataItem data)
	{
		DataEvent event = new DataEvent(DataEvent.DataEventType.REMOVE, data);
		sendDataEvent(event);
	}
	
	
	public void forceRefresh(Class<? extends AbstractDataItem> observeType)
	{
		DataEvent event = new DataEvent(DataEvent.DataEventType.FORCE_REFRESH, observeType);
		sendDataEvent(event);
	}

	public void putDataItems(List<? extends AbstractDataItem> datas)
	{
		DataEvent event = new DataEvent(DataEvent.DataEventType.ADD, datas);
		sendDataEvent(event);
	}

	public void registerDataObserver(Class<? extends AbstractDataItem> observeType, boolean notifyForDescendents, ContentObserver observer)
	{
		mContext.getContentResolver().registerContentObserver(DatabaseHelper.getInstance().getUri(observeType), notifyForDescendents, observer);
	}

	public Uri getUri(Class<? extends AbstractDataItem> c)
	{
		return DatabaseHelper.getInstance().getUri(c);
	}

	public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException
	{
		return DatabaseHelper.getInstance().getDao(clazz);
	}

	//throws SQLException
	@SuppressWarnings("unchecked")
	public <T extends AbstractDataItem> void createOrUpdate(T item)
	{
		try
		{
			CreateOrUpdateStatus cUStatus = getDao((Class<T>)item.getClass()).createOrUpdate(item);
			Log.d(TAG, "createOrUpdate: CreateOrUpdateStatus-" + "isCreated=" + cUStatus.isCreated() + "isUpdated=" + cUStatus.isUpdated() + "NumLinesChanged=" + cUStatus.getNumLinesChanged() );
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractDataItem> void delete(T item)
	{
		try {
			getDao((Class<T>)item.getClass()).delete(item);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
