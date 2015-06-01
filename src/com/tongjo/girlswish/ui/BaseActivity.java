package com.tongjo.girlswish.ui;

import com.tongjo.girlswish.R;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseActivity extends Activity {
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
	private Button backBtn;
	private TextView backText;
	private ViewGroup leftBtn;
	private ViewGroup rightBtn;
	private TextView centerText;

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
		leftBtn = (ViewGroup) findViewById(R.id.titlebar_btn_left);
		rightBtn = (ViewGroup) findViewById(R.id.titlebar_btn_right);
		backText = (TextView) findViewById(R.id.titlebar_text_back);
		backBtn = (Button) findViewById(R.id.titlebar_btn_back);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				BaseActivity.this.finish();
			}
		});
	}

	public ActionBar getMyActionBar() {
		return this.actionBar;
	}

	/** 设置按钮对应的监听 */
	public void setbackBtnListener(OnClickListener listener) {
		if (backBtn != null) {
			backBtn.setOnClickListener(listener);
		}
	}


	/** 设置标题文字 */
	public void setCenterText(String text) {
		if (centerText != null) {
			centerText.setText(text);
		}
	}

	public void setBackBtnText(String title) {
		if (backText != null) {
			backText.setText(title);
		}
	}

	/**设置左边显示返回按钮*/
	public void setBackBtn(){
		if (leftBtn != null) {
			leftBtn.setVisibility(View.GONE);
		}
		if(backBtn != null){
			backBtn.setVisibility(View.VISIBLE);
		}
	}
	
	/**隐藏左边的返回按钮*/
	public void hideBackBtn(){
		if(backBtn != null){
			backBtn.setVisibility(View.GONE);
		}
	}
	
	/**设置leftBtn的图片和文字,不显示返回按钮*/
	public void setleftBtn(int resID,String leftBtnText){
		Drawable drawable = getResources().getDrawable(resID);
		if (backBtn != null) {
			backBtn.setVisibility(View.GONE);
		}
		
		if (leftBtn == null) {
			leftBtn = (ViewGroup) findViewById(R.id.titlebar_btn_left);
		}
		if (leftBtn != null) {
			((ImageView) leftBtn.getChildAt(0)).setImageDrawable(drawable);
			((TextView) leftBtn.getChildAt(1)).setText(leftBtnText);
			leftBtn.setVisibility(View.VISIBLE);
		}
	}
	
	/**设置leftBtn的文字,不显示返回按钮*/
	public void setleftBtn(String leftBtnText){
		if (backBtn != null) {
			backBtn.setVisibility(View.GONE);
		}
		
		if (leftBtn == null) {
			leftBtn = (ViewGroup) findViewById(R.id.titlebar_btn_left);
		}
		if (leftBtn != null) {
			((TextView) leftBtn.getChildAt(1)).setText(leftBtnText);
			leftBtn.setVisibility(View.VISIBLE);
		}
	}
	
	/**设置leftBtn的图片,不显示返回按钮*/
	public void setleftBtn(int resID){
		Drawable drawable = getResources().getDrawable(resID);
		if (backBtn != null) {
			backBtn.setVisibility(View.GONE);
		}
		
		if (leftBtn == null) {
			leftBtn = (ViewGroup) findViewById(R.id.titlebar_btn_left);
		}
		if (leftBtn != null) {
			((ImageView) leftBtn.getChildAt(0)).setImageDrawable(drawable);
			leftBtn.setVisibility(View.VISIBLE);
		}
	}
	
	/** 设置右边按钮的显示方式，默认不显示，可以显示文字或者图片 */
	public void setRightButton() {
		if (rightBtn == null) {
			rightBtn = (ViewGroup) findViewById(R.id.titlebar_btn_right);
		}
		if (rightBtn != null) {
			rightBtn.getChildAt(0).setVisibility(View.GONE);
			rightBtn.getChildAt(1).setVisibility(View.VISIBLE);
			rightBtn.setVisibility(View.VISIBLE);
		}
	}
	
	public void setRightButton(int resID) {
		setRightButton(getResources().getDrawable(resID));
	}

	public void setRightButton(Drawable drawable) {
		
		if (rightBtn == null) {
			rightBtn = (ViewGroup) findViewById(R.id.titlebar_btn_right);
		}
		if (rightBtn != null) {
			((ImageView) rightBtn.getChildAt(1)).setImageDrawable(drawable);
			rightBtn.getChildAt(0).setVisibility(View.GONE);
			rightBtn.getChildAt(1).setVisibility(View.VISIBLE);
			rightBtn.setVisibility(View.VISIBLE);
		}
	}

	// 另一种设置添加按钮的方式，直接显示文字
	public void setRightButton(String title) {
		if (rightBtn == null) {
			rightBtn = (ViewGroup) findViewById(R.id.titlebar_btn_right);
		}
		if (rightBtn != null) {
			((TextView) rightBtn.getChildAt(0)).setText(title);
			rightBtn.getChildAt(1).setVisibility(View.GONE);
			rightBtn.getChildAt(0).setVisibility(View.VISIBLE);
			rightBtn.setVisibility(View.VISIBLE);
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