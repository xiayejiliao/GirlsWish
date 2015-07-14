package com.tongjo.girlswish;

import java.util.UUID;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.SyncHttpClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tongjo.bean.TJLocalUserInfo;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.db.OrmLiteHelper;
import com.tongjo.girlswish.model.LoginState;
import com.tongjo.girlswish.model.UserSex;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlwish.data.DataContainer;

import android.app.Application;
import android.util.Log;

/**
 * 基本的Application，暂时无代码，但这个后面肯定是需要用到的 Copyright 2015
 * 
 * @author preparing
 * @date 2015-5-31
 */
public class BaseApplication extends Application {
	private final String TAG = "BaseApplication";
	// 异步网络请求
	private AsyncHttpClient asyncHttpClient;
	// 同步网络请求
	private SyncHttpClient syncHttpClient;
	// 持久化在 SharedPreferences里的cookie，每个app只创建一个
	private PersistentCookieStore persistentCookieStore;

	@Override
	public void onCreate() {
		asyncHttpClient = new AsyncHttpClient();
		syncHttpClient = new SyncHttpClient();
		persistentCookieStore = new PersistentCookieStore(
				getApplicationContext());
		syncHttpClient.setCookieStore(persistentCookieStore);
		asyncHttpClient.setCookieStore(persistentCookieStore);
		OpenHelperManager.getHelper(getApplicationContext(),
				OrmLiteHelper.class);
		super.onCreate();
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getBaseContext()));
		DataContainer.userInfo = LoadLocalUserInfo();
	}

	/**
	 * 从本地加载用户数据
	 * 设置多个try/catch在于防止前面一个数据读取出错，后面的数据全部无法读取
	 * 
	 * @return
	 */
	public TJLocalUserInfo LoadLocalUserInfo() {
		TJLocalUserInfo user = new TJLocalUserInfo();
		try {
			String tel = (String) SpUtils.get(getApplicationContext(),
					AppConstant.USER_PHONE, "12");
			user.setTel(tel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String login = (String) SpUtils.get(getApplicationContext(),
					AppConstant.USER_LOGINSTATE, LoginState.LOGIN);
			user.setLoginStatus(login);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String userId = (String) SpUtils.get(getApplicationContext(),
					AppConstant.USER_ID, "7d1bc94c-1019-11e5-bb7f-525400271610");
			user.set_id(UUID.fromString(userId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			int sex = (Integer) SpUtils.get(getApplicationContext(),
					AppConstant.USER_SEX, 0);
			user.setGender(sex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String name = (String) SpUtils.get(getApplicationContext(),
					AppConstant.USER_NAME, "Name");
			user.setNickname(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String school = (String) SpUtils.get(getApplicationContext(),
					AppConstant.USER_SCHOOL, "School");
			user.setSchoolName(school);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String avatar = (String) SpUtils.get(getApplicationContext(),
					AppConstant.USER_ICONPATH, "");
			user.setAvatarUrl(avatar);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, user.toString());
		return user;
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

	public void setPersistentCookieStore(
			PersistentCookieStore persistentCookieStore) {
		this.persistentCookieStore = persistentCookieStore;
	}

}
