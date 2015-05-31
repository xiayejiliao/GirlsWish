package com.tongjo.girlswish.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

/**
 * 图片处理工具类
 * Copyright 2015 
 * @author preparing
 * @date 2015-5-31
 */
public class ImageUtils {
	/**
	 * 设置图片四个边角的角度，圆形的图片设置角度有360°即可
	 * 
	 * 这边的角度貌似和图片大小有关，改天再修改一下
	 * @param image
	 * @param roundPX
	 * @return
	 */
	public static Bitmap getRoundCornerDrawable(Bitmap image, float roundPX) {
	  	
		Bitmap output = Bitmap.createBitmap(image.getWidth(),
				image.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, image.getWidth(), image.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = roundPX;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(image, rect, rect, paint);

    	return output; 
    }
	
}
