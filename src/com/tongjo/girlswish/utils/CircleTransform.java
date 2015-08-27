package com.tongjo.girlswish.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * 
 * @Description: 用于图片加载picasso工具，加载圆角图片
 * @author 16ren
 * @date 2015年8月26日 下午5:31:36
 *
 */
public class CircleTransform implements Transformation {

	@Override
	public String key() {
		// TODO Auto-generated method stub
		return "circle";
	}

	@Override
	public Bitmap transform(Bitmap source) {
		if (source == null || source.isRecycled()) {
			return null;
		}
		int size = Math.min(source.getWidth(), source.getHeight());

		int x = (source.getWidth() - size) / 2;
		int y = (source.getHeight() - size) / 2;

		Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
		if (squaredBitmap != source) {
			source.recycle();
		}

		Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
		paint.setShader(shader);
		paint.setAntiAlias(true);

		float r = size / 2f;
		canvas.drawCircle(r, r, r, paint);

		squaredBitmap.recycle();
		return bitmap;
	}
}
