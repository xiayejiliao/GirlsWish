package com.tongjo.girlswish.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongjo.girlswish.widget.SmoothImageView;

public class SpaceImageDetailActivity extends Activity {

	private String mImageUrl;
	private int mLocationX;
	private int mLocationY;
	private int mWidth;
	private int mHeight;
	private SmoothImageView imageView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mImageUrl = getIntent().getStringExtra("image");
		mLocationX = getIntent().getIntExtra("locationX", 0);
		mLocationY = getIntent().getIntExtra("locationY", 0);
		mWidth = getIntent().getIntExtra("width", 0);
		mHeight = getIntent().getIntExtra("height", 0);

		imageView = new SmoothImageView(this);
		imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
		imageView.transformIn();
		imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
		imageView.setScaleType(ScaleType.FIT_CENTER);
		setContentView(imageView);
		ImageLoader.getInstance().displayImage(mImageUrl, imageView);
		
		imageView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				onBackPressed();
				return false;
			}
		});

	}

	@Override
	public void onBackPressed() {
		imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
			@Override
			public void onTransformComplete(int mode) {
				if (mode == 2) {
					finish();
				}
			}
		});
		imageView.transformOut();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(0, 0);
		}
	}

}
