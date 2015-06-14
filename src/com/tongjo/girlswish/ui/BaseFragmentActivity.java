package com.tongjo.girlswish.ui;

import com.tongjo.girlswish.R;
import com.tongjo.girlswish.widget.TextWithImageButton;

import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 基本FragmentActivity，主要是提供了操作ActionBar的方法
 * 
 * 后续应该还会添加一些Fragment相关的管理
 * 
 * Copyright 2015 
 * @author preparing
 * @date 2015-6-14
 */
public class BaseFragmentActivity extends FragmentActivity{
	/** 主要用来判断当前Activity是否在前端 */
	public static boolean isFront = false;

	public synchronized void setisFront(boolean bool) {
		this.isFront = bool;
	}

	public synchronized boolean getidFront() {
		return this.isFront;
	}

	/** 标题栏相关信息 */
	private ActionBar actionBar;
	private ImageView backImg = null;
	private TextView backText = null;
	// 左边的按钮组，文字和图片只能显示一个
	private ViewGroup leftBtnContainer = null;
	// 右边的按钮组，文字和图片只能显示一个
	private ViewGroup rightBtnContainer = null;
	// 左边的返回按钮组，图片和文字可以显示一个，也可以都显示
	private TextWithImageButton backBtnContainer = null;
	private TextView centerText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		View addView = getLayoutInflater().inflate(R.layout.titlebar_custom,
				null);
		actionBar.setCustomView(addView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		centerText = (TextView) findViewById(R.id.titlebar_text_center);
		leftBtnContainer = (ViewGroup) findViewById(R.id.titlebar_leftbtn_container);
		rightBtnContainer = (ViewGroup) findViewById(R.id.titlebar_rightbtn_container);
		backBtnContainer = (TextWithImageButton) findViewById(R.id.titlebar_backbtn_container);
		backImg = backBtnContainer.getImageView();
		backText = (TextView) backBtnContainer.getTextView();
		backBtnContainer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				BaseFragmentActivity.this.finish();
			}
		});
	}

	public ActionBar getMyActionBar() {
		return this.actionBar;
	}

	/** 设置按钮对应的监听 */
	public void setbackBtnListener(OnClickListener listener) {
		if (backBtnContainer != null) {
			backBtnContainer.setOnClickListener(listener);
		}
	}

	/** 设置标题文字 */
	public void setCenterText(String text) {
		if (centerText != null) {
			centerText.setText(text);
		}
	}


	/** 设置左边显示返回按钮,默认文字和图片都显示 */
	public void setBackBtn() {
		if (leftBtnContainer != null) {
			leftBtnContainer.setVisibility(View.GONE);
		}
		if (backBtnContainer != null) {
			backBtnContainer.setVisibility(View.VISIBLE);
		}
	}
	
	/** 设置返回的文字 */
	public void setBackBtnText(String title) {
		setBackBtn();
		if (backText != null) {
			backText.setText(title);
		}
	}

	/** 设置返回的图片 */
	public void setBackBtnImage(int resID) {
		Drawable drawable = getResourceDrawable(resID);
		setBackBtn();
		if (backImg != null) {
			backImg.setImageDrawable(drawable);
		}
	}

	/** 隐藏左边的返回按钮 */
	public void hideBackBtn() {
		if (backBtnContainer != null) {
			backBtnContainer.setVisibility(View.GONE);
		}
	}

	/** 隐藏返回按钮的图片,仅仅显示文字 */
	public void hideBackBtnImage() {
		setBackBtn();
		if (backImg != null) {
			backImg.setVisibility(View.GONE);
		}
	}

	/** 隐藏返回按钮的文字,仅仅显示图片 */
	public void hideBackBtnText() {
		setBackBtn();
		if (backText != null) {
			backText.setVisibility(View.GONE);
		}
	}
	
	
	
	
	/** 返回左边按钮的ImageView*/
	public ImageView getLeftBtnImageView(){
		if(leftBtnContainer != null){
			return ((ImageView) leftBtnContainer.getChildAt(0));
		}
		return null;
	}
	
	/** 返回左边按钮的TextView*/
	public TextView getLeftBtnTextView(){
		if(leftBtnContainer != null){
			return ((TextView) leftBtnContainer.getChildAt(1));
		}
		return null;
	}

	/** 设置leftBtn的文字,不显示返回按钮 */
	public void setLeftBtn(String leftBtnText) {
		 hideBackBtn();

		if (leftBtnContainer == null) {
			leftBtnContainer = (ViewGroup) findViewById(R.id.titlebar_leftbtn_container);
		}
		if (leftBtnContainer != null) {
			((TextView) leftBtnContainer.getChildAt(1)).setText(leftBtnText);
			leftBtnContainer.getChildAt(0).setVisibility(View.GONE);
			leftBtnContainer.getChildAt(1).setVisibility(View.VISIBLE);
			leftBtnContainer.setVisibility(View.VISIBLE);
		}
	}

	/** 设置leftBtn的图片,不显示返回按钮 */
	public void setLeftBtn(int resID) {
		hideBackBtn();
		Drawable drawable = getResourceDrawable(resID);
		if (leftBtnContainer == null) {
			leftBtnContainer = (ViewGroup) findViewById(R.id.titlebar_leftbtn_container);
		}
		if (leftBtnContainer != null) {
			((ImageView) leftBtnContainer.getChildAt(0))
					.setImageDrawable(drawable);
			leftBtnContainer.getChildAt(0).setVisibility(View.VISIBLE);
			leftBtnContainer.getChildAt(1).setVisibility(View.GONE);
			leftBtnContainer.setVisibility(View.VISIBLE);
		}
	}

	
	
	
	/** 返回左边按钮的ImageView*/
	public ImageView getRightBtnImageView(){
		if(rightBtnContainer != null){
			return ((ImageView) rightBtnContainer.getChildAt(0));
		}
		return null;
	}
	
	/** 返回左边按钮的TextView*/
	public TextView getRightBtnTextView(){
		if(rightBtnContainer != null){
			return ((TextView) rightBtnContainer.getChildAt(1));
		}
		return null;
	}

	/** 设置右边按钮的显示方式，默认不显示，可以显示文字或者图片 */
	public void setRightBtn(int resID) {
		setRightBtn(getResources().getDrawable(resID));
	}

	public void setRightBtn(Drawable drawable) {
		if (rightBtnContainer == null) {
			rightBtnContainer = (ViewGroup) findViewById(R.id.titlebar_rightbtn_container);
		}
		if (rightBtnContainer != null) {
			((ImageView) rightBtnContainer.getChildAt(1))
					.setImageDrawable(drawable);
			rightBtnContainer.getChildAt(0).setVisibility(View.GONE);
			rightBtnContainer.getChildAt(1).setVisibility(View.VISIBLE);
			rightBtnContainer.setVisibility(View.VISIBLE);
		}
	}

	// 另一种设置添加按钮的方式，直接显示文字
	public void setRightBtn(String title) {
		if (rightBtnContainer == null) {
			rightBtnContainer = (ViewGroup) findViewById(R.id.titlebar_rightbtn_container);
		}
		if (rightBtnContainer != null) {
			((TextView) rightBtnContainer.getChildAt(0)).setText(title);
			rightBtnContainer.getChildAt(1).setVisibility(View.GONE);
			rightBtnContainer.getChildAt(0).setVisibility(View.VISIBLE);
			rightBtnContainer.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		setisFront(true);
	}

	@Override
	public void onPause() {
		super.onPause();
		setisFront(false);
	}

	public String getResourceString(int id) {
		return getResources().getString(id);
	}

	public Drawable getResourceDrawable(int id) {
		return getResources().getDrawable(id);
	}

}