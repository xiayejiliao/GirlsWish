package com.tongjo.girlswish.ui;


import java.util.UUID;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.tongjo.bean.TJMessage;
import com.tongjo.bean.TJSchool;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.bean.TJWish;
import com.tongjo.db.OrmLiteHelper;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongjo.girlswish.BaseApplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 
 * Copyright 2015
 * 
 * @author preparing
 * @date 2015-6-14
 */

public class BaseFragment extends Fragment {
	protected OrmLiteHelper ormLiteHelper;
	protected Dao<TJUserInfo, UUID> tjuserinfoDao;
	protected Dao<TJSchool, UUID> tjschoolDao;
	protected Dao<TJWish, UUID> tjwishDao;
	protected Dao<TJMessage, UUID> tjmessageDao;
	protected AsyncHttpClient asyncHttpClient;
	protected SyncHttpClient syncHttpClient;
	protected ImageLoader imageLoader=ImageLoader.getInstance();
	protected Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=getActivity().getApplicationContext();
		ormLiteHelper = OpenHelperManager.getHelper(context, OrmLiteHelper.class);
		tjuserinfoDao = ormLiteHelper.getTJUserInfoDao();
		tjmessageDao = ormLiteHelper.getTJMessageDao();
		tjschoolDao = ormLiteHelper.getTJSchoolDao();
		tjwishDao = ormLiteHelper.getTJWishDao();
		asyncHttpClient = ((BaseApplication)getActivity().getApplication()).getAsyncHttpClient();
		syncHttpClient = ((BaseApplication)getActivity().getApplication()).getSyncHttpClient();
	}
	public Context getContext() {
		return context;
	}

}
