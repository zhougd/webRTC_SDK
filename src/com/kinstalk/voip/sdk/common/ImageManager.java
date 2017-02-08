package com.kinstalk.voip.sdk.common;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;

import com.kinstalk.voip.sdk.http.WeaverHttpConnection;
import com.kinstalk.voip.sdk.http.WeaverHttpRequest;

/*
 * 图片管理 异步获取图片，直接调用loadImage()函数，该函数自己判断是从缓存还是网络加载 同步获取图片，直接调用getBitmap()函数，该函数自己判断是从缓存还是网络加载
 * 仅从本地获取图片，调用getBitmapFromNative() 仅从网络加载图片，调用getBitmapFromHttp()
 */

public class ImageManager
{
	private final static String TAG = "ImageManager";
	private ImageFileCache imageFileCache; // 文件缓存
	private int mMaxSupportedWidth = 1024;
	private int mMaxSupportedHeight = 1024;
	private MemoryCache<Uri, MemoryBitmap> mImageBitMap;
	private MemoryCache<String, MemoryByteArray> mImageBytes;

	private static ImageManager gInstance = new ImageManager();
	
	public static class MemoryBitmap extends AbtractMemCacheDataItem {
		public Bitmap mBitmap;
		public MemoryBitmap(Bitmap bitmap) {
			mBitmap = bitmap;
			super.setmAccessedTime();
		}
		
		@Override
		public int getObjectSize() {
			if (mBitmap != null) {
				return mBitmap.getRowBytes()*mBitmap.getHeight();
			}
			return 1;
		}
	}
	public static class MemoryByteArray extends AbtractMemCacheDataItem {
		public byte[] mByteArray;
		public MemoryByteArray(byte[] byteArray) {
			mByteArray = byteArray;
			super.setmAccessedTime();
		}
	}
	
	public static final ImageManager getInstantce()
	{
		return gInstance;
	}

	private ImageManager()
	{
		imageFileCache = new ImageFileCache();
		mImageBitMap = new MemoryCache<Uri, MemoryBitmap>();
		mImageBytes  = new MemoryCache<String, MemoryByteArray>(-1, -1, -1);
	}
	
	public String getFilePath(String url)
	{
		return imageFileCache.getFilePath(url);
	}
	
	private Uri createUri(String url, int w, int h) {
		Uri uri = Uri.parse(url + "?w=" + w + "&h=" + h);
		return uri;
	}

	/**
	 * 依次从内存，缓存文件，网络上加载单个bitmap,不考虑线程的问题
	 */
	public Bitmap getBitmap(String url)
	{
		if (url == null || "".equals(url) || !url.contains("http:"))
		{
			 Log.d(TAG, "Fail get bmp from any, url=" + url);
			return null;
		}

		// 从内存缓存中获取图片
		MemoryBitmap mBp = mImageBitMap.get(createUri(url, mMaxSupportedWidth, mMaxSupportedHeight));
		Bitmap bitmap = null;
		if (mBp != null) {
		    bitmap = mBp.mBitmap;
		}
		if (bitmap == null)
		{
			// 文件缓存中获取
			// bitmap = imageFileCache.getImageFromFile(url);
			MemoryByteArray mBa = mImageBytes.get(url);
			byte[] imageArray = null;
			if (mBa != null) {
				imageArray = mBa.mByteArray;
			}
			if (imageArray != null && imageArray.length > 0) {
			} else {
				imageArray = imageFileCache.getOriginalImageFromFile(url);
			}
			if (imageArray != null && imageArray.length > 0) {
				 Log.d(TAG, "getBitmap: start to decodeByteArray[" + imageArray.length + "] from url=" + url);
				bitmap = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
			}
			if (bitmap == null) {
				bitmap = imageFileCache.getImageFromFile(url);
			}
			if (bitmap != null)
			{
				// 添加到内存缓存
				mImageBitMap.put(createUri(url, mMaxSupportedWidth, mMaxSupportedHeight), new MemoryBitmap(bitmap));
			}
			else
			{
				// 从网络获取
				bitmap = getBitmapFromHttp(url, mMaxSupportedWidth, mMaxSupportedHeight);
			}
		}
		return bitmap;
	}
	
	private Bitmap decodeBitmapFromBytes(byte[] tmpPicByte, int w, int h, String url) {
		Bitmap bmp = null;
		if (tmpPicByte != null)
		{
			// 添加到内存缓存
			//imageMemoryCache.addImageByteArrayToMemory(url, tmpPicByte);
			
			 Log.d(TAG, "getBitmapFromHttp: start to decodeByteArray[" + tmpPicByte.length + "] from url=" + url);

			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(tmpPicByte, 0, tmpPicByte.length, opts);
			if (opts.outWidth*opts.outHeight > mMaxSupportedWidth*mMaxSupportedHeight) {
				opts.inSampleSize = calculateInSampleSize(opts, mMaxSupportedWidth, mMaxSupportedHeight);
				opts.inJustDecodeBounds = false;
				//opts.inSampleSize = 4;
				 Log.d(TAG, "getBitmapFromHttp: real size: " + opts.outWidth + "x"  + opts.outHeight + " from url=" + url);
				bmp = BitmapFactory.decodeByteArray(tmpPicByte, 0, tmpPicByte.length, opts);
				 Log.d(TAG, "getBitmapFromHttp: decoded size: " + bmp.getWidth() + "x"  + bmp.getHeight() + " from url=" + url);
				//imageFileCache.saveBitmapToFile(bmp, url);
				if (w*h < mMaxSupportedWidth*mMaxSupportedHeight) {
					opts.inSampleSize = calculateInSampleSize(opts, w, h);
					opts.inJustDecodeBounds = false;
					bmp = BitmapFactory.decodeByteArray(tmpPicByte, 0, tmpPicByte.length, opts);
				}
			} else {
				// 添加到内存缓存
				if (opts.outWidth*opts.outHeight > w*h) {
					opts.inSampleSize = calculateInSampleSize(opts, w, h);
					opts.inJustDecodeBounds = false;
					//opts.inSampleSize = 4;
					 Log.d(TAG, "getBitmapFromHttp: real size: " + opts.outWidth + "x"  + opts.outHeight + " from url=" + url);
					bmp = BitmapFactory.decodeByteArray(tmpPicByte, 0, tmpPicByte.length, opts);
					 Log.d(TAG, "getBitmapFromHttp: decoded size: " + bmp.getWidth() + "x"  + bmp.getHeight() + " from url=" + url);
				} else {
				    bmp = BitmapFactory.decodeByteArray(tmpPicByte, 0, tmpPicByte.length);
				}
				mImageBytes.put(url, new MemoryByteArray(tmpPicByte));
				// 添加到文件缓存
				//imageFileCache.saveOriginImageToFile(tmpPicByte, url);
			}
		}
		return bmp;
	}

	/**
	 * 从内存或者缓存文件中获取bitmap
	 */
	public Bitmap getBitmapFromNative(String url, int w, int h)
	{
		if (url == null || "".equals(url) || !url.contains("http:"))
		{
			 Log.d(TAG, "Fail get bmp from any, url=" + url);
			return null;
		}
		
		Bitmap bitmap = null;
		byte[] imageArray = null;
		MemoryBitmap mBp = mImageBitMap.get(createUri(url, w, h)); 
		if (mBp != null) {
		    bitmap = mBp.mBitmap;//imageMemoryCache.getBitmapFromMemory(url);
		}

		if (bitmap == null)
		{
// 			bitmap = imageFileCache.getImageFromFile(url);
			MemoryByteArray  mBa = mImageBytes.get(url); 
			if (mBa != null) {
			    imageArray = mBa.mByteArray;
			}
			if (imageArray != null && imageArray.length > 0) {
			} else {
				imageArray = imageFileCache.getOriginalImageFromFile(url);
			}
			if (imageArray != null && imageArray.length > 0) {
				 Log.d(TAG, "getBitmapFromNative: start to decodeByteArray[" + imageArray.length + "] from url=" + url);
				// bitmap = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
				bitmap = decodeBitmapFromBytes(imageArray, w, h, url);
			}
			if (bitmap == null) {
				imageFileCache.getImageFromFile(url);//???????
			}
			if (bitmap != null)
			{
				// 添加到内存缓存
				mImageBitMap.put(createUri(url, w, h), new MemoryBitmap(bitmap));
			}
		}
		return bitmap;
	}
	
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	/**
	 * 从内存中获取bitmap
	 */
	public Bitmap getDirectiveBitmapFromNative(String url, int w, int h, Bitmap defaultBitmap)
	{
		if (url == null || "".equals(url) || !url.contains("http:"))
		{
			 Log.d(TAG, "Fail get bmp from any, url=" + url);
			return defaultBitmap;
		}
		
		Bitmap bitmap = null;
		//byte[] imageArray = null;
		MemoryBitmap mBp = mImageBitMap.get(createUri(url, w, h)); 
		if (mBp != null) {
		    bitmap = mBp.mBitmap;//imageMemoryCache.getBitmapFromMemory(url);
		}
		return bitmap;
	}

	/**
	 * 通过网络下载图片,与线程无关
	 */
	public Bitmap getBitmapFromHttp(String url, int w, int h)
	{
		Bitmap bmp = null;
		
		if (url == null || "".equals(url) || !url.contains("http:"))
		{
			 Log.d(TAG, "Fail get bmp from HTTP, url=" + url);
			return null;
		}

		try
		{
			byte[] tmpPicByte = getImageBytes(url);

			if (tmpPicByte != null)
			{
				// 添加到内存缓存
				//imageMemoryCache.addImageByteArrayToMemory(url, tmpPicByte);
				
				 Log.d(TAG, "getBitmapFromHttp: start to decodeByteArray[" + tmpPicByte.length + "] from url=" + url);

				Options opts = new Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeByteArray(tmpPicByte, 0, tmpPicByte.length, opts);
				 Log.d(TAG, "getBitmapFromHttp: real size ="  + opts.outWidth + "x" + opts.outHeight + "req size=" + w + "x" + h+ "max size=" + mMaxSupportedWidth + "x" + mMaxSupportedHeight);
				if (opts.outWidth*opts.outHeight > mMaxSupportedWidth*mMaxSupportedHeight) {
					opts.inSampleSize = calculateInSampleSize(opts, mMaxSupportedWidth, mMaxSupportedHeight);
					opts.inJustDecodeBounds = false;
					//opts.inSampleSize = 4;
					 Log.d(TAG, "getBitmapFromHttp: real size: " + opts.outWidth + "x"  + opts.outHeight + " from url=" + url);
					bmp = BitmapFactory.decodeByteArray(tmpPicByte, 0, tmpPicByte.length, opts);
					 Log.d(TAG, "getBitmapFromHttp: decoded size: " + bmp.getWidth() + "x"  + bmp.getHeight() + " from url=" + url);
					imageFileCache.saveBitmapToFile(bmp, url);
					if (w*h < mMaxSupportedWidth*mMaxSupportedHeight) {
						opts.inSampleSize = calculateInSampleSize(opts, w, h);
						opts.inJustDecodeBounds = false;
						bmp = BitmapFactory.decodeByteArray(tmpPicByte, 0, tmpPicByte.length, opts);
					}
				} else {
					// 添加到内存缓存
					if (opts.outWidth*opts.outHeight > w*h) {
						opts.inSampleSize = calculateInSampleSize(opts, w, h);
						opts.inJustDecodeBounds = false;
						//opts.inSampleSize = 4;
						 Log.d(TAG, "getBitmapFromHttp: real size: " + opts.outWidth + "x"  + opts.outHeight + " from url=" + url);
						bmp = BitmapFactory.decodeByteArray(tmpPicByte, 0, tmpPicByte.length, opts);
						 Log.d(TAG, "getBitmapFromHttp: decoded size: " + bmp.getWidth() + "x"  + bmp.getHeight() + " from url=" + url);
					} else {
					    bmp = BitmapFactory.decodeByteArray(tmpPicByte, 0, tmpPicByte.length);
					}
					mImageBytes.put(url, new MemoryByteArray(tmpPicByte));
					// 添加到文件缓存
					imageFileCache.saveOriginImageToFile(tmpPicByte, url);
				}
			}
			tmpPicByte = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (bmp != null)
		{
			// 添加到文件缓存
			// imageFileCache.saveBitmapToFile(bmp, url);
			// 添加到内存缓存
			mImageBitMap.put(createUri(url, w, h), new MemoryBitmap(bmp));
		}

		return bmp;
	}

	/**
	 * 下载链接的图片资源
	 * 
	 * @param url
	 * 
	 * @return 图片
	 */
	public byte[] getImageBytes(String url)
	{
		byte[] pic = null;
		if (url != null && !"".equals(url))
		{
			WeaverHttpRequest req = new WeaverHttpRequest();
			req.setMethod("GET");
			req.setUrl(url);
			req.setTimeoutMS(30000);
			try
			{
				WeaverHttpConnection.sendHttp(req);
				pic = req.getResponseData();
				 Log.d(TAG, "icon bytes.length=" + pic.length);
			}
			catch (Exception e3)
			{
				e3.printStackTrace();
				try
				{
					 Log.d(TAG, "download shortcut icon faild and responsecode=" + req.getResponseCode());
				}
				catch (Exception e4)
				{
					e4.printStackTrace();
				}
			}
		}
		return pic;
	}

	/**
	 * 图片变为圆角
	 * 
	 * @param bitmap
	 *            :传入的bitmap
	 * @param pixels
	 *            ：圆角的度数，值越大，圆角越大
	 * @return bitmap:加入圆角的bitmap
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels)
	{
		if (bitmap == null)
			return null;

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
}
