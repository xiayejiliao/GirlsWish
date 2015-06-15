package com.tongjo.girlswish;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.SyncHttpClient;

import android.app.Application;

/**
 * 基本的Application，暂时无代码，但这个后面肯定是需要用到的
 * Copyright 2015 
 * @author preparing
 * @date 2015-5-31
 */
public class BaseApplication extends Application {
	//异步网络请求
	private AsyncHttpClient asyncHttpClient;
	//同步网络请求
	private SyncHttpClient syncHttpClient;
	//持久化在 SharedPreferences里的cookie，每个app只创建一个
	private PersistentCookieStore persistentCookieStore;
	
	@Override
	public void onCreate() {
		asyncHttpClient=new AsyncHttpClient();
		syncHttpClient=new SyncHttpClient();
		persistentCookieStore=new PersistentCookieStore(getApplicationContext());
		syncHttpClient.setCookieStore(persistentCookieStore);
		asyncHttpClient.setCookieStore(persistentCookieStore);
		super.onCreate();
	}

	public AsyncHttpClient getAsyncHttpClient() {
		return asyncHttpClient;
	}

	public void setAsyncHttpClient(AsyncHttpClient asyncHttpClient) {
		this.asyncHttpClient = asyncHttpClient;
	}

	public SyncHttpClient getSyncHttpClient() {
		return syncHttpClient;
	}

	public void setSyncHttpClient(SyncHttpClient syncHttpClient) {
		this.syncHttpClient = syncHttpClient;
	}

	public PersistentCookieStore getPersistentCookieStore() {
		return persistentCookieStore;
	}

	public void setPersistentCookieStore(PersistentCookieStore persistentCookieStore) {
		this.persistentCookieStore = persistentCookieStore;
	}
}
