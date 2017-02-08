package com.kinstalk.voip.sdk.data;

import java.util.List;

import com.kinstalk.voip.sdk.data.model.AbstractDataItem;

public class DataEvent
{
	public static final class DataEventType
	{
		public static final int ADD = 0;
		public static final int UPDATE = 1;
		public static final int REMOVE = 2;
		public static final int CLEAR = 99;
		public static final int FORCE_REFRESH = 199;
	}

	private final int eventType;
	private final List<? extends AbstractDataItem> datas;
	private final AbstractDataItem data;
	private Class<? extends AbstractDataItem> mObserveType;

	public DataEvent(int dataEventType, List<? extends AbstractDataItem> datas)
	{
		this.eventType = dataEventType;
		this.datas = datas;
		this.data = null;
		this.mObserveType = null;
	}

	public DataEvent(int dataEventType, AbstractDataItem data)
	{
		this.eventType = dataEventType;
		this.datas = null;
		this.data = data;
		this.mObserveType = null;
	}
	
	public DataEvent(int dataEventType, Class<? extends AbstractDataItem> observeType)
	{
		this.eventType = dataEventType;
		this.datas = null;
		this.data = null;
		this.mObserveType = observeType;
	}

	public int getEventType()
	{
		return eventType;
	}

	public List<? extends AbstractDataItem> getDatas()
	{
		return datas;
	}

	public AbstractDataItem getData()
	{
		return data;
	}
	
	public Class<? extends AbstractDataItem> getObserveType() {
		return mObserveType;
	}
}
