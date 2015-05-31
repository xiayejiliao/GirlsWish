package com.tongjo.girlswish.widget;

import com.tongjo.girlswish.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 带有箭头的按钮‘
 * 左边有一个图片，然后是文字，靠近左边图片，然后是文字，靠近最右边的箭头，最右边是箭头
 * 用户可以自己定义左边的图片和文字,不过目前中间的两种文字的大小和颜色都只能是一致的
 * 使用时记得加入组件的报名：xmlns:tongjo="http://schemas.android.com/apk/res/com.tongjo.girlswish"
 * 然后直接使用tongjo:lefttextValue ; tongjo:textColor即可进行设定 
 * Copyright 2015 
 * @author preparing
 * @date 2015-5-31
 */
public class ButtonWithArrow extends LinearLayout{
	private final static String TAG = "ButtonWithArrow";
	private Context mContext;
	private ImageView mIcon;
	private TextView mLeftText;
	private TextView mRightText;
	
	//用于设置左边的图片是否显示
	public static final int VISIBLE = 0x001;
	public static final int GONE = 0x002;
	
	public ButtonWithArrow(Context context) {
		super(context);
		mContext = context;
		/*init();*/
	}

	public ButtonWithArrow(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(attrs);
	}
	
	public void setLeftText(String title){
		if(mLeftText != null){
			mLeftText.setText(title);
		}
	}
	
	public void setRightText(String title){
		if(mRightText != null){
			mRightText.setText(title);
		}
	}
	
	private void init(AttributeSet attrs) {
		final View mView = LayoutInflater.from(mContext).inflate(
				R.layout.widget_buttonwitharrow, this, true);
		
		mView.setClickable(true);
		mLeftText = (TextView)mView.findViewById(R.id.widget_lefttext);
		mRightText = (TextView)mView.findViewById(R.id.widget_righttext);
		mIcon = (ImageView)mView.findViewById(R.id.widget_icon);
		
		TypedArray typeArray = mContext.obtainStyledAttributes(attrs,
				R.styleable.ButtonWithArrow);
		
		int N = typeArray.getIndexCount();
		for (int i = 0; i < N; i++) {
			int attr = typeArray.getIndex(i);
			switch (attr) {
			case R.styleable.ButtonWithArrow_textColor:{
				int textColor  = typeArray.getColor(R.styleable.ButtonWithArrow_textColor, Color.BLACK);
				mLeftText.setTextColor(textColor);
				mRightText.setTextColor(textColor);
				break;
			}
			case R.styleable.ButtonWithArrow_textSize:{
				float textSize  = typeArray.getDimension(R.styleable.ButtonWithArrow_textSize, 24);
				mRightText.setTextSize(textSize);
				mRightText.setTextSize(textSize);
				break;
			}
			case R.styleable.ButtonWithArrow_lefttextValue:{
				int resouceId = -1;
				resouceId = typeArray.getResourceId(
						R.styleable.ButtonWithArrow_lefttextValue, 0);
				mLeftText.setText(resouceId > 0 ? typeArray.getResources().getText(
						resouceId) : typeArray
						.getString(R.styleable.ButtonWithArrow_lefttextValue));
				break;
			}
			case R.styleable.ButtonWithArrow_righttextValue:{
				int resouceId = -1;
				resouceId = typeArray.getResourceId(
						R.styleable.ButtonWithArrow_righttextValue, 0);
				mRightText.setText(resouceId > 0 ? typeArray.getResources().getText(
						resouceId) : typeArray
						.getString(R.styleable.ButtonWithArrow_righttextValue));
				break;
			}
			case R.styleable.ButtonWithArrow_iconsrc:{
				int resouceId = -1;
				resouceId = typeArray.getResourceId(
						R.styleable.ButtonWithArrow_iconsrc, 0);
				if(resouceId > 0){
					Log.d(TAG, "setImageResourse");
					mIcon.setImageDrawable(typeArray.getDrawable(attr));
				}else{
					Log.d(TAG, " no setImageResourse");
				}
				break;
			}
			case R.styleable.ButtonWithArrow_iconvisiable:{
				int resouceId = 0;
				//默认为1，显示左边图片
				resouceId = typeArray.getInt(R.styleable.ButtonWithArrow_iconvisiable, 1);
				if(resouceId == VISIBLE){
					mIcon.setVisibility(View.VISIBLE);
				}else if(resouceId == GONE){
					mIcon.setVisibility(View.GONE);
				}
			}
			}
		}
		
		typeArray.recycle();
		
	}
}