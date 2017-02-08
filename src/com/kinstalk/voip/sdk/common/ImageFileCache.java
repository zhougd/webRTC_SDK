package com.kinstalk.voip.sdk.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

public class ImageFileCache
{
	private static final String TAG = "ImageFileCache";
	// 图片缓存目录
	private static final String IMGCACHDIR = "/sdcard/RandomCall/ImgCach";
	// 保存的cache文件宽展名
	private static final String CACHETAIL = ".cach";
	private static final int MB = 1024 * 1024;
	private static final int CACHE_SIZE = 1;
	// 当SD卡剩余空间小于10M的时候会清理缓存
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;

	public ImageFileCache()
	{
		// 清理部分文件缓存 removeCache(IMGCACHDIR);
	}

	/** * 从缓存中获取图片 */
	public Bitmap getImageFromFile(final String url)
	{
		final String path = IMGCACHDIR + "/" + convertUrlToFileName(url);
		File file = new File(path);
		if (file != null && file.exists())
		{
			Bitmap bmp = BitmapFactory.decodeFile(path);
			if (bmp == null)
			{
				file.delete();
			}
			else
			{
				updateFileTime(path);
				 Log.d(TAG, "get bmp from FileCache,url=" + url);
				return bmp;
			}
		}
		 Log.d(TAG, "Fail get bmp from File,url=" + url);
		return null;
	}
	
	public String getFilePath(String url) {
		if (url == null || "".equals(url) || !url.contains("http:")) {
			 Log.d(TAG, "Fail get file path from any, url=" + url);
			return null;
		}

		final String path = IMGCACHDIR + "/" + convertUrlToFileName(url);
		File file = new File(path);
		if (file != null && file.exists()) {
			 Log.d(TAG, "get file path from FileCache,url=" + url);
			return path;
		}
		return null;
	}
	
	public byte[] getOriginalImageFromFile(final String url)
	{
		final String path = IMGCACHDIR + "/" + convertUrlToFileName(url);
		File file = new File(path);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (file != null && file.exists())
		{
			//Bitmap bmp = null;
			try {
				InputStream inStream = new FileInputStream(file);
				
				byte[] buffer = new byte[128];
				int len = 0;

				try
				{
					while ((len = inStream.read(buffer)) > 0)
					{
						baos.write(buffer, 0, len);
					}
				}
				catch (Exception e1)
				{
					inStream.close();
				}
				inStream.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (baos.size() <= 0)
			{
				file.delete();
			}
			else
			{
				updateFileTime(path);
				 Log.d(TAG, "get orginal image from FileCache,url=" + url);
				return baos.toByteArray();
			}
		}
		 Log.d(TAG, "Fail get orginal image from File,url=" + url);
		return null;
	}

	/** * 将图片存入文件缓存 */
	public void saveBitmapToFile(Bitmap bm, String url)
	{
		if (bm == null)
		{
			return;
		}
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > SdCardFreeSpace())
		{
			// SD空间不足
			return;
		}
		String filename = convertUrlToFileName(url);
		File dirFile = new File(IMGCACHDIR);
		if (!dirFile.exists())
			dirFile.mkdirs();
		File file = new File(IMGCACHDIR + "/" + filename);
		try
		{
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
		}
		catch (FileNotFoundException e)
		{
			 Log.d(TAG, "FileNotFoundException");
		}
		catch (IOException e)
		{
			 Log.d(TAG, "IOException");
		}
	}
	
	public void saveOriginImageToFile(byte[] byteArray, String url)
	{
		if (byteArray == null)
		{
			return;
		}
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > SdCardFreeSpace())
		{
			// SD空间不足
			return;
		}
		String filename = convertUrlToFileName(url);
		File dirFile = new File(IMGCACHDIR);
		if (!dirFile.exists())
			dirFile.mkdirs();
		File file = new File(IMGCACHDIR + "/" + filename);
		try
		{
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			outStream.write(byteArray);
			//bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
		}
		catch (FileNotFoundException e)
		{
			 Log.d(TAG, "FileNotFoundException");
		}
		catch (IOException e)
		{
			 Log.d(TAG, "IOException");
		}
	}

	/**
	 * * 计算存储目录下的文件大小， * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定 *
	 * 那么删除40%最近没有被使用的文件
	 */
	private boolean removeCache(String dirPath)
	{
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null)
		{
			return true;
		}
		if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			return false;
		}
		int dirSize = 0;
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].getName().contains(CACHETAIL))
			{
				dirSize += files[i].length();
			}
		}
		if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > SdCardFreeSpace())
		{
			int removeFactor = (int) (0.4 * files.length);
			Arrays.sort(files, new FileLastModifSort());
			for (int i = 0; i < removeFactor; i++)
			{
				if (files[i].getName().contains(CACHETAIL))
				{
					files[i].delete();
				}
			}
		}
		if (SdCardFreeSpace() <= CACHE_SIZE)
		{
			return false;
		}
		return true;
	}

	/** * 修改文件的最后修改时间 */

	public void updateFileTime(String path)
	{
		File file = new File(path);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}

	/** * 计算SD卡上的剩余空间 */
	private int SdCardFreeSpace()
	{
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	/** * 将url转成文件名 */
	private String convertUrlToFileName(String url)
	{
		return url.hashCode() + CACHETAIL;
	}

	/** * 根据文件的最后修改时间进行排序 */
	private class FileLastModifSort implements Comparator<File>
	{
		public int compare(File file0, File file1)
		{
			if (file0.lastModified() > file1.lastModified())
			{
				return 1;
			}
			else if (file0.lastModified() == file1.lastModified())
			{
				return 0;
			}
			else
			{
				return -1;

			}
		}
	}

}
