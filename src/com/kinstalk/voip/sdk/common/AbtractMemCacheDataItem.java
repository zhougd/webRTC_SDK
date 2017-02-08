package com.kinstalk.voip.sdk.common;


public abstract class AbtractMemCacheDataItem {
	private long mAccessedTime;

	public long getmAccessedTime() {
		return mAccessedTime;
	}

	public void setmAccessedTime() {
		this.mAccessedTime = System.currentTimeMillis();
	}
	
	public long getAloneTime() {
		return (System.currentTimeMillis() - mAccessedTime);
	}
	
	public int getObjectSize() {
		return 1;
	}
}
