package com.tongjo.girlswish.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tongjo.girlswish.R;

public class AddWishActivity extends BaseActivity{
	private EditText mEditText = null;
	private View mBtn1 = null;
	private View mBtn2 = null;
	private View mBtn3 = null;
	private View mBtn4 = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addwish);
		
		setRightBtn("完成");
		InitView();
	}
	
	public void InitView(){
		mEditText = (EditText)findViewById(R.id.addwish_edit);
		mBtn1 = (View)findViewById(R.id.addwish_btn1);
		mBtn2 = (View)findViewById(R.id.addwish_btn2);
		mBtn3 = (View)findViewById(R.id.addwish_btn3);
		mBtn4 = (View)findViewById(R.id.addwish_btn4);
	}
}
