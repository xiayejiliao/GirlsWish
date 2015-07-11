package com.tongjo.girlswish.ui;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;
import com.tongjo.girlswish.BaseApplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 
 * Copyright 2015 
 * @author preparing
 * @date 2015-6-14
 */
public class BaseFragment extends Fragment{
	protected AsyncHttpClient asyncHttpClient;
	protected SyncHttpClient syncHttpClient;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		asyncHttpClient = ((BaseApplication)getActivity().getApplication()).getAsyncHttpClient();
		syncHttpClient = ((BaseApplication)getActivity().getApplication()).getSyncHttpClient();
	}
}
