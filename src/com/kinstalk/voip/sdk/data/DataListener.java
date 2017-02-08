package com.kinstalk.voip.sdk.data;

import java.util.List;

import com.kinstalk.voip.sdk.data.model.AbstractDataItem;

public interface DataListener
{
	public void onDataChange(int dataType, int actionType, List<? extends AbstractDataItem> data);

	public void onDataChange(int dataType, int actionType, AbstractDataItem data);

}
