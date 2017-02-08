package com.kinstalk.voip.sdk.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

	public static String getDateTime(long time) {
		java.text.DateFormat sdf= SimpleDateFormat.getDateTimeInstance();//new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		java.util.Date dt = new Date(time);  
		String sDateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
		return sDateTime;
	}
}
