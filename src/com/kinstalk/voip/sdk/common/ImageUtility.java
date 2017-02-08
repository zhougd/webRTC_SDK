package com.kinstalk.voip.sdk.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

public class ImageUtility
{

	public static final String TAG = "JPA";

	/**
	 * 获取裁剪后的圆形图片
	 * 
	 * @param radius
	 *            半径
	 */
	public static Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius)
	{
		Matrix m = new Matrix();
		m.setRotate(90);

		Bitmap scaledSrcBmp;
		int diameter = radius * 2;

		// 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		int squareWidth = 0, squareHeight = 0;
		int x = 0, y = 0;
		Bitmap squareBitmap;
		if (bmpHeight > bmpWidth)
		{// 高大于宽
			squareWidth = squareHeight = bmpWidth;
			x = 0;
			y = (bmpHeight - bmpWidth) / 2;
			// 截取正方形图片
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight, m, true);
		}
		else if (bmpHeight < bmpWidth)
		{// 宽大于高
			squareWidth = squareHeight = bmpHeight;
			x = (bmpWidth - bmpHeight) / 2;
			y = 0;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight, m, true);
		}
		else
		{
			squareBitmap = bmp;
		}

		if (squareBitmap.getWidth() != diameter || squareBitmap.getHeight() != diameter)
		{
			scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter, diameter, true);

		}
		else
		{
			scaledSrcBmp = squareBitmap;
		}
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight(), Config.ARGB_8888);

		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 255, 255, 255);
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2, scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2 - 5, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
		paint.setXfermode(null);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(3);
		paint.setFilterBitmap(false);
		paint.setDither(false);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2, scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2 - 3, paint);
		// bitmap回收(recycle导致在布局文件XML看不到效果)
//		bmp.recycle();
		if (squareBitmap != bmp)
		{
			squareBitmap.recycle();
		}
		if (scaledSrcBmp != squareBitmap)
		{
			scaledSrcBmp.recycle();
		}
		bmp = null;
		squareBitmap = null;
		scaledSrcBmp = null;
		return output;
	}

	public static int dip2px(Context context, float dipValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static String getDefaultPortraitUrl(String url)
	{
		String result = null;
		if ("P1".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd239_src.jpg";
		}
		else if ("P2".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd23a_src.jpg";
		}
		else if ("P3".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd23b_src.jpg";
		}
		else if ("P4".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd23c_src.jpg";
		}
		else if ("P5".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd23d_src.jpg";
		}
		else if ("P6".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd23e_src.jpg";
		}
		else if ("P7".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd23f_src.jpg";
		}
		else if ("P8".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd240_src.jpg";
		}
		else if ("P9".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd242_src.jpg";
		}
		else if ("P10".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd241_src.jpg";
		}
		else if ("P11".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd243_src.jpg";
		}
		else if ("P12".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd244_src.jpg";
		}
		else if ("P13".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd245_src.jpg";
		}
		else if ("P14".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd246_src.jpg";
		}
		else if ("P15".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd247_src.jpg";
		}
		else if ("P16".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd248_src.jpg";
		}
		else if ("P17".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd249_src.jpg";
		}
		else if ("P18".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd24a_src.jpg";
		}
		else if ("P19".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd24b_src.jpg";
		}
		else if ("P20".equals(url))
		{
			result = "http://p1.ifaceshow.com/icon/icons/39010c58_28940317ecd24c_src.jpg";
		}

		return result;
	}
}
