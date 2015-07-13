package com.tongjo.girlswish.ui;

import java.util.UUID;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.tongjo.bean.TJLocalUserInfo;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.model.LoginState;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.ImageUtils;
import com.tongjo.girlswish.utils.SexUtils;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.widget.ButtonWithArrow;
import com.tongjo.girlwish.data.DataContainer;

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
		InitData();
		LoadLocalUserInfo();
	}

	public void InitView(){
		mNickName = (ButtonWithArrow)findViewById(R.id.userbasicinfo_nickname);
		mSex = (ButtonWithArrow)findViewById(R.id.userbasicinfo_sex);
		mSchool = (ButtonWithArrow)findViewById(R.id.userbasicinfo_school);
		mPhone = (ButtonWithArrow)findViewById(R.id.userbasicinfo_phone);
		avatar = (ImageView)findViewById(R.id.avatar);
		exit = (Button)findViewById(R.id.exit);
	}
	
	public void InitData(){
		if(DataContainer.userInfo != null){
			if(!StringUtils.isBlank(DataContainer.userInfo.getNickname())){
				mNickName.setRightText(DataContainer.userInfo.getNickname());
			}
			if(!StringUtils.isBlank(DataContainer.userInfo.getTel())){
				mPhone.setRightText(DataContainer.userInfo.getTel());
			}
			mSex.setRightText(SexUtils.getSexString(DataContainer.userInfo.getGender()));
			
			if(!StringUtils.isBlank(DataContainer.userInfo.getAvatarUrl())){
				Bitmap bit = ImageUtils.readBitmapFromLocal(DataContainer.userInfo.getAvatarUrl());
				if(bit != null){
					avatar.setImageBitmap(ImageUtils.getRoundCornerDrawable(bit, 360));
				}
			}
		}
	}
	
	public TJLocalUserInfo LoadLocalUserInfo() {
		TJLocalUserInfo user = new TJLocalUserInfo();
		try {
			String tel = (String) SpUtils.get(getApplicationContext(),
					AppConstant.USER_PHONE, "12");
			String login = (String) SpUtils.get(getApplicationContext(),
					AppConstant.USER_LOGINSTATE, LoginState.LOGIN);
			UUID userId = (UUID) SpUtils.get(getApplicationContext(),
					AppConstant.USER_ID, "");
			int sex = (Integer) SpUtils.get(getApplicationContext(),
					AppConstant.USER_SEX, 0);
			String name = (String)SpUtils.get(getApplicationContext(),
					AppConstant.USER_NAME, "ffg");

			String avatar = (String) SpUtils.get(getApplicationContext(),
					AppConstant.USER_ICONPATH, null);
			user.set_id(userId);
			user.setAvatarUrl(avatar);
			user.setTel(tel);
			user.setGender(sex);
			user.setLoginStatus(login);
			user.setNickname(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, user.toString());
		return user;
	}

}
