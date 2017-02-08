package com.kinstalk.voip.sdk.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.LruCache;

public class MemoryCache<K, V extends AbtractMemCacheDataItem>{
	private static final String TAG = "MemoryCache";
	private LruCache<K, V> mLruCache;
	private int mMinSize = 50*1024*1024;
	private int mIncrementSize = mMinSize;
	private int mMinAloneTimeMs = 1000*60*5;// ms
	private MemoryCache<K, V> mSynchAtom = null;
	
	public MemoryCache() {
		mSynchAtom = this;
		mLruCache = createLruCache(mMinSize);
	}
	
	public MemoryCache(int minSize, int incrementSize, int minCachedTimeMs) {
		mSynchAtom = this;
		if (minSize > 0) {
			mMinSize = minSize;
		}
		if (incrementSize >= 0) {
		    mIncrementSize = incrementSize;
		}
		if (minCachedTimeMs > 0) {
			mMinAloneTimeMs = minCachedTimeMs;
		}
		mLruCache = createLruCache(mMinSize);
	}
	
	private LruCache<K, V> createLruCache(int cacheSize) {
		LruCache<K, V> lruCache = null;
		synchronized(mSynchAtom) {
		lruCache = new LruCache<K, V>(cacheSize)
		{
			@Override
			// sizeOf返回为单个hashmap value的大小
			protected int sizeOf(K key, V value)
			{
				if (value != null)
					return value.getObjectSize();
				else
					return 0;
			}

			@Override
			protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue)
			{
				synchronized(mSynchAtom) {
				if (evicted)
				{
					if (oldValue.getAloneTime() <= mMinAloneTimeMs) {
						enlargeLruCache(mIncrementSize, key, oldValue);
					} else {
						Map<K, V> snapMap = mLruCache.snapshot();
						Set<K> set = snapMap.keySet();
						List<K> list = new ArrayList<K>(set);
						for (int i = list.size();i > 0; i--) {
							if (mLruCache.get(list.get(i)).getAloneTime() > mMinAloneTimeMs) {
								 Log.d(TAG, "LruCache item " + list.get(i) + " is removed as too long time alone.");
								mLruCache.remove(list.get(i));
							}
						}
					}
				}
				}
			}
		};
		}
		return lruCache;
	}
	
	private void enlargeLruCache(int incrementSize, K key, V addedValue) {
		LruCache<K, V> lruCache = createLruCache(mLruCache.maxSize() + incrementSize);
		Map<K, V> snapMap = mLruCache.snapshot();
		Set<K> set = snapMap.keySet();
		K oldKey;
		for (Iterator<K> itr = set.iterator(); itr.hasNext();) {
			oldKey = (K) itr.next();
			lruCache.put(oldKey, mLruCache.get(oldKey));
		}
		lruCache.put(key, addedValue);
		for (Iterator<K> itr = set.iterator(); itr.hasNext();) {
			oldKey = (K) itr.next();
			mLruCache.remove(oldKey);
		}
		mLruCache = lruCache;
		 Log.d(TAG, "LruCache is full,enlarge size to " + mLruCache.maxSize());
	}
	
	public synchronized final V get(K key) {
		V value = mLruCache.get(key);
		if (value != null) {
			value.setmAccessedTime();
			 Log.d(TAG, "LruCache get value of " + key + mLruCache.maxSize());
		}
		return value;
	}
	
	public synchronized final V put(K key, V value) {
		if (value != null) {
			value.setmAccessedTime();
			if (value.getObjectSize() > mLruCache.maxSize()) {
				if (mIncrementSize < value.getObjectSize()) {
					mIncrementSize += value.getObjectSize();
				}
				enlargeLruCache(mIncrementSize, key, value);
			}
		    return mLruCache.put(key, value);
		}
		return null;
	}
}
