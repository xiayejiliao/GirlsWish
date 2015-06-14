package com.tongjo.girlswish.widget;

import com.tongjo.girlswish.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 图片在左边，文字在右边的一种按钮，按下去之后可以同时有效果
 * Copyright 2015 
 * @author preparing
 * @date 2015-6-14
 */
public class TextWithImageButton extends LinearLayout implements OnTouchListener{
	private final static String TAG = "TextWithImageButton";
	private Context mContext;
	private TextView mTextView;
	private ImageView mImageView;
	
	private int textColor;
	private int textClickColor;
	private Drawable imageSrc = null;
	private Drawable imageClickSrc = null;
	
	public TextWithImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(attrs);
		setOnTouchListener(this);
	}

	private void init(AttributeSet attrs) {
		View mView = LayoutInflater.from(mContext).inflate(
				R.layout.widget_textwithimage, this, true);
		mTextView = (TextView) mView.findViewById(R.id.widget_lefttext);
		mImageView = (ImageView) mView.findViewById(R.id.widget_icon);
		
		TypedArray typeArray = mContext.obtainStyledAttributes(attrs,
				R.styleable.TextWithImageButton);

		int N = typeArray.getIndexCount();
		for (int i = 0; i < N; i++) {
			int attr = typeArray.getIndex(i);
			switch (attr) {
			case R.styleable.TextWithImageButton_text: {
				int resouceId = -1;
				resouceId = typeArray.getResourceId(
						R.styleable.ButtonWithArrow_lefttextValue, 0);
				mTextView.setText(resouceId > 0 ? typeArray.getResources().getText(
						resouceId) : typeArray
						.getString(R.styleable.TextWithImageButton_text));
				break;
			}
			case R.styleable.TextWithImageButton_textColor: {
				textColor = typeArray.getColor(
						R.styleable.TextWithImageButton_textColor, Color.BLACK);
				mTextView.setTextColor(textColor);
				break;
			}
			case R.styleable.TextWithImageButton_textClickColor: {
				textClickColor = typeArray.getColor(
						R.styleable.TextWithImageButton_textClickColor, Color.BLACK);
				break;
			}
			case R.styleable.TextWithImageButton_textSize: {
				float textSize = typeArray.getDimension(
						R.styleable.TextWithImageButton_textSize, 16);
				mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,textSize);
				break;
			}
			case R.styleable.TextWithImageButton_imageSrc: {
				int image = typeArray.getResourceId(
						R.styleable.TextWithImageButton_imageSrc, 0);
				if(image > 0){
					Log.d(TAG, "setImageResourse");
					mImageView.setImageDrawable(typeArray.getDrawable(attr));
					imageSrc = typeArray.getDrawable(attr);
				}else{
					Log.d(TAG, " no setImageResourse");
				}
				break;
			}
			case R.styleable.TextWithImageButton_imageClickSrc: {
				int image = typeArray.getResourceId(
						R.styleable.TextWithImageButton_imageClickSrc, 0);
				if(image > 0){
					Log.d(TAG, "setImageResourse");
					imageClickSrc = typeArray.getDrawable(attr);
				}else{
					Log.d(TAG, " no setImageResourse");
				}
				break;
			}
			}
		}

		typeArray.recycle();
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			//设置背景为选中状态
			mImageView.setImageDrawable(imageClickSrc);
			mTextView.setTextColor(textClickColor);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			//设置背景为未选中正常状态
			mImageView.setImageDrawable(imageSrc);
			mTextView.setTextColor(textColor);
		}
		else  if(event.getAction() == MotionEvent.ACTION_CANCEL){
			mImageView.setImageDrawable(imageSrc);
			mTextView.setTextColor(textColor);
		}
		return false;
	}
	
	
	public TextView getTextView(){
		return mTextView;
	}
	
	public ImageView getImageView(){
		return mImageView;
	}
	
}
