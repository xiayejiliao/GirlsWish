package com.tongjo.girlswish.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.tongjo.girlswish.R;
import com.tongjo.girlswish.widget.ButtonWithArrow;

/**
 * 用户基本信息页面，修改头像也统一在这边修改
 * Copyright 2015 
 * @author preparing
 * @date 2015-7-12
 */
public class UserBasicInfoActivity extends BaseActivity {
	private final String TAG = "AddWishActivity";
	private ButtonWithArrow mNickName;
	private ButtonWithArrow mSex;
	private ButtonWithArrow mSchool;
	private ButtonWithArrow mPhone;
	private ImageView avatar;
	private Button exit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userbasicinfo);

		setTitle(getResources().getString(R.string.basicinfo));
		InitView();
	}

	public void InitView(){
		mNickName = (ButtonWithArrow)findViewById(R.id.userbasicinfo_nickname);
		mSex = (ButtonWithArrow)findViewById(R.id.userbasicinfo_sex);
		mSchool = (ButtonWithArrow)findViewById(R.id.userbasicinfo_school);
		mPhone = (ButtonWithArrow)findViewById(R.id.userbasicinfo_phone);
		avatar = (ImageView)findViewById(R.id.avatar);
		exit = (Button)findViewById(R.id.exit);
	}
	

}
