package com.kinstalk.voip.sdk.common;

import android.hardware.Camera;
import android.media.AudioManager;

/**
 * @author wangxiang 2013-4-19
 */
public class Compatibility
{
	public static final Class<?> TAG = Compatibility.class.getClass();

	public static enum BRAND
	{
		LENOVO, SAMSUNG, XIAOMI, ZTE, HUAWEI, COOLPAD, UNKNOWN
	}

	public static final boolean isHTCOneX()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("htc one x"))
		{
			 Log.d(TAG, "HTC One X.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isZTEU950()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("zte u950"))
		{
			 Log.d(TAG, "ZTE U950.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isHUAWEIU9508()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("huawei u9508"))
		{
			 Log.d(TAG, "HUAWEI U9508.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isCoolpad9960()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("yl-coolpad 9960"))
		{
			 Log.d(TAG, "YL-Coolpad 9960");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isLenovoK900()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("lenovo k900"))
		{
			 Log.d(TAG, "Lenovo K900");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isP770()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("p770"))
		{
			 Log.d(TAG, "YL-Coolpad 9960");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isLenovoS870E()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("lnv-lenovo s870e"))
		{
			 Log.d(TAG, "LNV-Lenovo S870e");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isCoolpad8190()
	{
		String model = android.os.Build.MODEL;
		String brand = android.os.Build.BRAND;
		if (model != null && brand != null && model.toLowerCase().contains("8190") && brand.toLowerCase().contains("coolpad"))
		{
			 Log.d(TAG, "Coolpad 8190.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isLenovoS899t()
	{
		String model = android.os.Build.MODEL;
		String brand = android.os.Build.BRAND;
		if (model != null && brand != null && model.toLowerCase().contains("lenovo s899t"))
		{
			 Log.d(TAG, "Lenovo S899t.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static int getFrontCamera()
	{
		int result = -1;
		try
		{
			int cameraCount = 0;
			Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
			cameraCount = Camera.getNumberOfCameras();
			for (int camIndex = 0; camIndex < cameraCount; camIndex++)
			{
				Camera.getCameraInfo(camIndex, cameraInfo);
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
				{
					result = camIndex;
					break;
				}
			}
		}
		catch (Exception e)
		{
			 Log.e(TAG, "Exception when get front camera id.", e);
			result = -1;
		}
		return result;
	}

	public static int getBackCamera()
	{
		int result = -1;
		try
		{
			int cameraCount = 0;
			Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
			cameraCount = Camera.getNumberOfCameras();
			for (int camIndex = 0; camIndex < cameraCount; camIndex++)
			{
				Camera.getCameraInfo(camIndex, cameraInfo);
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
				{
					result = camIndex;
					break;
				}
			}
		}
		catch (Exception e)
		{
			 Log.e(TAG, "Exception when get back camera id.", e);
			result = -1;
		}
		return result;
	}

	public static final boolean isSamsung9300()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("gt-i9300"))
		{
			 Log.d(TAG, "GT-I9300");
			return true;
		}
		else
		{
			return false;
		}
	}

	/* Mi2, Mi2s, etc. */
	public static final boolean isMi2Series()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("mi 2"))
		{
			 Log.d(TAG, "MI 2");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static int getAndroidSDKVersion()
	{
		return android.os.Build.VERSION.SDK_INT;
	}

	public static final boolean isSamsung7100()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("gt-n7100"))
		{
			 Log.d(TAG, "GT-N7100");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isLenovoS720()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("lenovo s720"))
		{
			 Log.d(TAG, "Lenovo S720");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isLenovoS890()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("lenovo s890"))
		{
			 Log.d(TAG, "Lenovo S890");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isLenovoA706()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("lenovo a706"))
		{
			 Log.d(TAG, "Lenovo A706");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isLenovoA765e()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("lenovo a765e"))
		{
			 Log.d(TAG, "Lenovo A765e");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isSonylt26w()
	{
		String model = android.os.Build.MODEL;
		String brand = android.os.Build.BRAND;
		if (model != null && brand != null && model.toLowerCase().contains("lt26w") && brand.toLowerCase().contains("semc"))
		{
			 Log.d(TAG, "sonylt26w");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isMeizuMX2()
	{
		String model = android.os.Build.MODEL;
		String brand = android.os.Build.BRAND;
		if (model != null && brand != null && model.toLowerCase().contains("m040") && brand.toLowerCase().contains("meizu"))
		{
			 Log.d(TAG, "Meizu Mx2");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final BRAND getBrand()
	{
		String model = android.os.Build.BRAND;
		if (model != null)
		{
			if (model.toLowerCase().contains("lenovo"))
			{
				return BRAND.LENOVO;
			}
			else if (model.toLowerCase().contains("samsung"))
			{
				return BRAND.SAMSUNG;
			}
			else if (model.toLowerCase().contains("xiaomi"))
			{
				return BRAND.XIAOMI;
			}
			else if (model.toLowerCase().contains("zte"))
			{
				return BRAND.ZTE;
			}
			else if (model.toLowerCase().contains("huawei"))
			{
				return BRAND.HUAWEI;
			}
			else if (model.toLowerCase().contains("coolpad"))
			{
				return BRAND.COOLPAD;
			}
			else
			{
				return BRAND.UNKNOWN;
			}
		}
		else
		{
			return BRAND.UNKNOWN;
		}
	}

	public static final boolean isHuaweiMT1U06()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("huawei mt1-u06"))
		{
			 Log.d(TAG, "HUAWEI MT1-U06.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isLenovoS950()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("lenovo s950"))
		{
			 Log.d(TAG, "Lenovo S950.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isLenovoS960()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("lenovo s960"))
		{
			 Log.d(TAG, "Lenovo S960.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isGoogleGalaxyNexus()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("galaxy nexus"))
		{
			 Log.d(TAG, "Galaxy Nexus.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isLenovoS5000()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("lenovo s5000"))
		{
			 Log.d(TAG, "Lenovo S5000.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isHWpreviewAvailable()
	{
		return !isGoogleGalaxyNexus();
	}

	public static final boolean isLenovoB8000()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("lenovo b8000"))
		{
			 Log.d(TAG, "Lenovo B8000.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isLenovoB6000()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("lenovo b6000"))
		{
			 Log.d(TAG, "Lenovo B6000.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isLenovoS920()
	{
		String model = android.os.Build.MODEL;
		if (model != null && model.toLowerCase().contains("lenovo s920"))
		{
			 Log.d(TAG, "Lenovo S920.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isLenovoK860()
	{
		String model = android.os.Build.MODEL;
		// Must be equals because there is K860i.
		if (model != null && model.toLowerCase().equals("lenovo k860"))
		{
			 Log.d(TAG, "Lenovo K860.");
			return true;
		}
		else
		{
			return false;
		}
	}

	public static final boolean isInCallModeDevice()
	{
		 Log.d(TAG, "isInCallModeDevice.");
		String model = android.os.Build.MODEL;
		if (model == null)
		{
			 Log.d(TAG, "model is null, cannot detect device type.");
			return false;
		}
		String lowerCaseModel = model.toLowerCase();
		if (lowerCaseModel.contains("lenovo k900") || lowerCaseModel.contains("lenovo a820e") || lowerCaseModel.contains("lenovo s820e") || lowerCaseModel.contains("gt-i9300")
				|| lowerCaseModel.contains("sch-i939") || lowerCaseModel.contains("gt-n7100") || lowerCaseModel.contains("sch-n719") || lowerCaseModel.contains("sch-i959")
				|| lowerCaseModel.contains("lenovo s890"))
		{
			 Log.d(TAG, "in call mode device, model:" + model);
			return true;
		}
		else
		{
			 Log.d(TAG, "NOT in call mode device, model:" + model);
			return false;
		}

	}
	
	 public static final boolean isLenovoA278t() {
		String model = android.os.Build.MODEL;
		if(model != null && model.toLowerCase().equals("lenovo a278t")){
		 Log.d(TAG, "Lenovo A278t.");
		    return true;
		}else{
		    return false;
		}	
	  }
 
	public static final int getCurrentCallModeDevice(){
     	//String model = android.os.Build.MODEL;
     	//if(model != null){
     	//	String lowerCaseModel = model.toLowerCase(); 
        // 	if(lowerCaseModel.contains("sch-i939")) { //|| lowerCaseModel.contains("lenovo k860")
        // 		return AudioManager.STREAM_VOICE_CALL;
        // 	}
     	//}
     	
     	return AudioManager.STREAM_MUSIC;
    }

}
