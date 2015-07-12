package com.tongjo.girlswish.ui;

import android.os.Bundle;

import com.tongjo.girlswish.R;

/**
 * 用户基本信息页面，修改头像也统一在这边修改
 * Copyright 2015 
 * @author preparing
 * @date 2015-7-12
 */
public class UserBasicInfoActivity extends BaseActivity {
	private final String TAG = "AddWishActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userbasicinfo);

		setTitle("个人信息");
		InitView();
	}

	public void InitView(){
		
	}
	

}
