package com.tongjo.emchat;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.db.OrmLiteHelper;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlwish.data.DataContainer;

public class UserUtils {
	protected static final String R = null;
	private static String TAG = "UserUtils";

	public static void getUserByHxid(final Context appContext, final String hxid, final UserGetLisener userGetLisener) {
		if (DataContainer.userInfo != null && hxid.equals(DataContainer.userInfo.getHxid())) {
			userGetLisener.onGetUser(DataContainer.userInfo);
		} else if (DataContainer.userInfoMap.containsKey(hxid)) {
			userGetLisener.onGetUser(DataContainer.userInfoMap.get(hxid));
		} else {
			TJUserInfo userInfo = getUserByHxidFromDb(appContext, hxid);
			if (userInfo != null) {
				userGetLisener.onGetUser(userInfo);
			} else {
				getUserByHxidFromNet(appContext, hxid, userGetLisener);
			}
		}
	}

	public static void getUserByHxidFromNetOrMem(final Context appContext, final String hxid, final UserGetLisener userGetLisener) {
		if (DataContainer.userInfo != null && hxid.equals(DataContainer.userInfo.getHxid())) {
			userGetLisener.onGetUser(DataContainer.userInfo);
		} else if (DataContainer.userInfoMap.containsKey(hxid)) {
			userGetLisener.onGetUser(DataContainer.userInfoMap.get(hxid));
		} else {
			getUserByHxidFromNet(appContext, hxid, userGetLisener);
		}
	}

	public static TJUserInfo getUserByHxidFromDb(final Context appContext, final String hxid) {
		try {
			Dao<TJUserInfo, UUID> mTJUserDao = new OrmLiteHelper(appContext).getTJUserInfoDao();
			QueryBuilder<TJUserInfo, UUID> builder = mTJUserDao.queryBuilder();
			Where<TJUserInfo, UUID> where = builder.where();
			where.eq("hxid", hxid);
			builder.setWhere(where);
			PreparedQuery<TJUserInfo> preparedQuery = builder.prepare();
			List<TJUserInfo> queryUserInfoList = mTJUserDao.query(preparedQuery);
			if (queryUserInfoList.size() > 0) {
				return queryUserInfoList.get(0);
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void getUserByHxidFromNet(final Context appContext, final String hxid, final UserGetLisener userGetLisener) {
		Thread backgroundThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// 从服务器获取
				RequestParams requestParams = new RequestParams();
				requestParams.add("hxid", hxid);
				((BaseApplication) appContext.getApplicationContext()).getSyncHttpClient().get(AppConstant.URL_BASE + AppConstant.URL_USERGET, requestParams, new TextHttpResponseHandler("UTF-8") {

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
						Log.d(TAG, "Get userinfo by hxid failure");
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						Log.d(TAG, "arg0:" + arg0 + "arg2:" + arg2);
						if (arg0 == 200) {
							Type type = new TypeToken<TJResponse<TJUserInfo>>() {
							}.getType();
							TJResponse<TJUserInfo> response = new Gson().fromJson(arg2, type);
							if (response.getResult().getCode() == 0) {
								try {
									DataContainer.userInfoMap.put(hxid, response.getData());
									Dao<TJUserInfo, UUID> mTJUserDao = new OrmLiteHelper(appContext).getTJUserInfoDao();
									mTJUserDao.createIfNotExists(response.getData());
									mTJUserDao.update(response.getData());
									userGetLisener.onGetUser(response.getData());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				});
			}
		});
		backgroundThread.start();
	}

	public interface UserGetLisener {
		void onGetUser(TJUserInfo userInfo);
	}
}
