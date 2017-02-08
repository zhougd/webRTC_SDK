package com.kinstalk.voip.sdk.common;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import android.hardware.Camera;

public class DeviceInfo
{
	private static final Class<?> TAG = DeviceInfo.class.getClass();
	static File file;

	// 获取核心cpu数量
	public static int getNumCores()
	{
		class CpuFilter implements FileFilter
		{
			@Override
			public boolean accept(File pathname)
			{
				if (Pattern.matches("cpu[0-9]", pathname.getName()))
				{
					return true;
				}
				return false;
			}
		}
		try
		{
			File dir = new File("/sys/devices/system/cpu/");
			File[] files = dir.listFiles(new CpuFilter());
			return files.length;
		}
		catch (Exception e)
		{
			return 1;
		}
	}

	// 获取CPU最大频率（单位KHZ）
	public static String getMaxCpuFreq() throws IOException
	{
		StringBuilder result = new StringBuilder();
		FileInputStream inStream = null;
		try
		{
			file = new File("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
			inStream = new FileInputStream(file);
			byte[] re = new byte[24];
			int r = 0;
			while ((r = inStream.read(re)) != -1)
			{
				result.append(new String(re, 0, r));
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			result = new StringBuilder("N/A");
		}
		finally
		{
			if (inStream != null)
			{
				inStream.close();
			}
		}
		return result.toString().trim();
	}

	// 获取CPU最小频率（单位KHZ）
	// public static String getMinCpuFreq() {
	// String result = "";
	// ProcessBuilder cmd;
	// try {
	// file = new File("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
	// FileInputStream inStream = new FileInputStream(file);
	// byte[] re = new byte[24];
	// while (inStream.read(re) != -1) {
	// result = result + new String(re);
	// }
	// inStream.close();
	// } catch (IOException ex) {
	// ex.printStackTrace();
	// result = "N/A";
	// }
	// return result.trim();
	// }

	// // 实时获取CPU当前频率（单位KHZ）
	// public static String getCurCpuFreq() {
	// String result = "N/A";
	// try {
	// file = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
	// FileInputStream inStream = new FileInputStream(file);
	// BufferedReader br = new BufferedReader(inStream);
	// String text = br.readLine();
	// result = text.trim();
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return result;
	// }
	//
	// public static String getTotalMemory() {
	// String str1 = "/proc/meminfo";
	// String str2="";
	// try {
	// FileReader fr = new FileReader(str1);
	// BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
	// while ((str2 = localBufferedReader.readLine()) != null) {
	//
	// }
	// return str2;
	// } catch (IOException e) {
	// return str2;
	// }
	// }

	public static Camera.Size getMaxCameraSize(Camera.Parameters param)
	{
		Camera.Size result = null;
		try
		{
			List<Camera.Size> sizeList = param.getSupportedPreviewSizes();
			Collections.sort(sizeList, new CameraSizeComparator());
			result = sizeList.get(sizeList.size() - 1);
		}
		catch (Exception e)
		{
			 Log.e(TAG, "Open camera error!", e);
		}

		if (result != null)
		{
			 Log.d(TAG, "Compared Max Camera Size: " + result.width + "," + result.height);
		}
		else
		{
			 Log.d(TAG, "Compared Max Camera Size: null!");
		}

		return result;
	}

	/**
	 * Sort by width first ( from small to big), if equal, then sort by height ( from small to bit).
	 * 
	 */
	static class CameraSizeComparator implements Comparator<Camera.Size>
	{

		@Override
		public int compare(Camera.Size o1, Camera.Size o2)
		{
			if (o1.width > o2.width)
			{
				return 1;
			}
			else if (o1.width == o2.width)
			{
				if (o1.height > o2.height)
				{
					return 1;
				}
				else
				{
					return -1;
				}
			}
			else
			{
				return -1;
			}
		}

	}
}
