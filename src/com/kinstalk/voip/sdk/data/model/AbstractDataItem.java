package com.kinstalk.voip.sdk.data.model;

import java.sql.SQLException;
import java.util.List;

import android.net.Uri;

import com.kinstalk.voip.sdk.data.DataService;
import com.kinstalk.voip.sdk.logic.WeaverAbstractLogic;
import com.kinstalk.voip.sdk.common.AbtractMemCacheDataItem;
import com.kinstalk.voip.sdk.common.Log;

public abstract class AbstractDataItem extends AbtractMemCacheDataItem
{
	protected boolean mIsPersisted = false; 
	protected boolean mShouldPersist = true;

	public Uri getUri()
	{
		return DataService.getInstance().getUri(this.getClass());
	}
	
	
	public boolean getmShouldPersist() {
		return mShouldPersist;
	}
	
	protected void setmShouldPersist(boolean persistThisTime) {
		mShouldPersist = persistThisTime;
	}
	
	public void putToDB() {
		if (mIsPersisted == true) {
			Log.i("AbstractDataItem", "put to memory:" + getUri());
			WeaverAbstractLogic.getMemCache().put(getUri(), this);
			if (mShouldPersist == true) {
		        DataService.getInstance().putDataItem(this);
			}
		}
	}
	
	public <T extends AbstractDataItem> void postProcess(T objectForPostProcess) {
	}
	
	public void removeFromDB() {
		if (mIsPersisted == true) {
		    DataService.getInstance().delteDataItem(this);
		}
	}

	public void setmTobePersisted() {
		mIsPersisted = true;		
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractDataItem> T updateFromDB() {
		T newObject = null;
		try {
			newObject = DataService.getInstance().getDao((Class<T>)this.getClass()).queryForSameId((T)this);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return newObject;
		}
		
		return newObject;
	}
	
	public static <T extends AbstractDataItem> List<T> queryForAll(Class<T> clazz) {
		List<T> newObject = null;
		try {
			DataService.getInstance().getDao(clazz).queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return newObject;
		}
		
		return newObject;
	}
	
	public static <T extends AbstractDataItem> List<T> queryForEq(Class<T> clazz, String arg0, Object arg1) {
		List<T> list = null;
		try {
			list = DataService.getInstance().getDao(clazz).queryForEq(arg0, arg1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return list;
		}
		
		return list;
	}
	
	public <T extends AbstractDataItem> List<T> queryForEq(String arg0, Object arg1) {
		List<T> list = null;
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>)this.getClass();
		try {
			list = DataService.getInstance().getDao(clazz).queryForEq(arg0, arg1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return list;
		}
		
		return list;
	}
	
	///////
	

	public void attachAllForeignCollection() {
		// TODO Auto-generated method stub
		
	}


}
