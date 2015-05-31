package com.tongjo.girlswish.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.ImageUtils;

public class TestActivity extends Activity{
	private ImageView mImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		mImageView = (ImageView)findViewById(R.id.test_image);
		Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.testimg);
		mImageView.setImageBitmap(ImageUtils.getRoundCornerDrawable(bmp, 360));
	}
}
