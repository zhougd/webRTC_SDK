package com.kinstalk.voip.sdk.logic.image;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;

import com.kinstalk.voip.sdk.api.WeaverConstants;
import com.kinstalk.voip.sdk.api.WeaverService;
import com.kinstalk.voip.sdk.interfaces.WeaverRequestListener;
import com.kinstalk.voip.sdk.model.WeaverRequest;
import com.kinstalk.voip.sdk.logic.WeaverAbstractLogic;
import com.kinstalk.voip.sdk.common.ImageManager;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.StringUtility;



public class ImageLogic extends WeaverAbstractLogic {
	public static final String LOGIC_HOST = "image";
	private URI mImageLogicUri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, LOGIC_HOST, null);

	public ImageLogic(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		WeaverService.getInstance().registerLogicHandler(mImageLogicUri, this);
	}
	
	public static class GetBitmap extends WeaverRequest {
		private static final String mMethodName = "/" + GetBitmap.class.getSimpleName();
		public static final URI mTargetURI = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME, ImageLogic.LOGIC_HOST, mMethodName);
		public static final Class<Bitmap> mRetDataType = Bitmap.class;
		private String mUrl;
		private int mWidth;
		private int mHeight;
		
		public GetBitmap(String url, int w, int h, WeaverRequestListener listener) {
			super.setParam(mTargetURI, listener);
			super.setRequestListener(listener);
			mUrl = url;
			mWidth = w;
			mHeight = h;
		}
		
	
	public static Bitmap getBitmap(HashMap<String, Object> paramHashMap, String url, int w, int h, Bitmap defaultBitmap, WeaverRequestListener listener) {
		Bitmap bitmap = ImageManager.getInstantce().getDirectiveBitmapFromNative(url, w, h, defaultBitmap);
		if (bitmap == null){
		WeaverRequest req =	new ImageLogic.GetBitmap(url, w, h, listener);
		//req.addParameter("ImageView", icon);
		if (paramHashMap != null && !paramHashMap.isEmpty()) {
			Set<String> set = paramHashMap.keySet();
			String key;
			for (Iterator<String> itr = set.iterator(); itr.hasNext();) {
				key = (String)itr.next();
				req.addParameter(key, paramHashMap.get(key));
			}
		}
		WeaverService.getInstance().dispatchRequest(req);
		}
		return bitmap;
	}
	@Override
	public void run() {
		Bitmap response = null;
		response = ImageManager.getInstantce().getBitmapFromNative(mUrl, mWidth, mHeight);
		if (response == null) {
		    response = ImageManager.getInstantce().getBitmapFromHttp(mUrl, mWidth, mHeight);
//		    try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		if (response == null) {
		setResponse(-200, response);}
		else {
			setResponse(200, response);
		}
	}
	}

	@Override
	public void doHandleRequest(WeaverRequest req) {
		// TODO Auto-generated method stub

		 Log.d(getClass(), "ImageLogic handle request:" + req.toString());
		req.run();
	}

}
