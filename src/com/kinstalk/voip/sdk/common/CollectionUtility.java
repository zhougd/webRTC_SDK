package com.kinstalk.voip.sdk.common;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;



public class CollectionUtility
{
	private static final Class<?> TAG = CollectionUtility.class.getClass();

	public static <T> boolean contains(Collection<WeakReference<T>> c, T o)
	{
		if (c == null)
		{
			 Log.d(TAG, "Collection is null!");
			return false;
		}
		
		if (o == null)
		{
			 Log.d(TAG, "Object is null!");
			return false;
		}

		T cur = null;
		synchronized (c)
		{
			Iterator<WeakReference<T>> i = c.iterator();
			while (i.hasNext())
			{
				cur = i.next().get();
				if (cur == o)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static <T> boolean add(Collection<WeakReference<T>> c, T o)
	{
		if (c == null)
		{
			 Log.d(TAG, "Collection is null!");
			return false;
		}

		if (o == null)
		{
			 Log.d(TAG, "Object is null!");
			return false;
		}
		
		T cur = null;
		synchronized (c)
		{
			Iterator<WeakReference<T>> i = c.iterator();
			while (i.hasNext())
			{
				cur = i.next().get();
				if (cur == o)
				{
					return false;
				}
			}
			
			c.add(new WeakReference<T>(o));
			return true;
		}
	}
	
	public static <T> boolean remove(Collection<WeakReference<T>> c, T o)
	{
		if (c == null)
		{
			 Log.d(TAG, "Collection is null!");
			return false;
		}

		if (o == null)
		{
			 Log.d(TAG, "Object is null!");
			return false;
		}
		
		T cur = null;
		synchronized (c)
		{
			Iterator<WeakReference<T>> i = c.iterator();
			while (i.hasNext())
			{
				cur = i.next().get();
				if (cur == o)
				{
					i.remove();
					return true;
				}
			}
		}
		return false;
	}
	
	public static <T> boolean addSoftReference(Collection<SoftReference<T>> c, T o)
	{
		if (c == null)
		{
			 Log.d(TAG, "Collection is null!");
			return false;
		}

		if (o == null)
		{
			 Log.d(TAG, "Object is null!");
			return false;
		}
		
		T cur = null;
		synchronized (c)
		{
			Iterator<SoftReference<T>> i = c.iterator();
			while (i.hasNext())
			{
				cur = i.next().get();
				if (cur == o)
				{
					return false;
				}
			}
			
			c.add(new SoftReference<T>(o));
			return true;
		}
	}

	public static <T> boolean removeSoftReference(Collection<SoftReference<T>> c, T o)
	{
		if (c == null)
		{
			 Log.d(TAG, "Collection is null!");
			return false;
		}

		if (o == null)
		{
			 Log.d(TAG, "Object is null!");
			return false;
		}
		
		T cur = null;
		synchronized (c)
		{
			Iterator<SoftReference<T>> i = c.iterator();
			while (i.hasNext())
			{
				cur = i.next().get();
				if (cur == o)
				{
					i.remove();
					return true;
				}
			}
		}
		return false;
	}
	
	public static <T> boolean addStrongReference(Collection<T> c, T o)
	{
		if (c == null)
		{
			 Log.d(TAG, "Collection is null!");
			return false;
		}

		if (o == null)
		{
			 Log.d(TAG, "Object is null!");
			return false;
		}
		
		T cur = null;
		synchronized (c)
		{
			Iterator<T> i = c.iterator();
			while (i.hasNext())
			{
				cur = i.next();
				if (cur == o)
				{
					return false;
				}
			}

			c.add(o);
			return true;
		}
	}
	
	public static <T> boolean removeStrongReference(Collection<T> c, T o)
	{
		if (c == null)
		{
			 Log.d(TAG, "Collection is null!");
			return false;
		}

		if (o == null)
		{
			 Log.d(TAG, "Object is null!");
			return false;
		}
		
		T cur = null;
		synchronized (c)
		{
			Iterator<T> i = c.iterator();
			while (i.hasNext())
			{
				cur = i.next();
				if (cur == o)
				{
					i.remove();
					return true;
				}
			}
		}
		return false;
	}
}
