package com.tongjo.emchat;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.db.OrmLiteHelper;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.StringUtils;

public class UserUtils {
	protected static final String R = null;
	private static String TAG = "UserUtils";

	public static void getUserByHxid(final Context appContext, final String hxid, final UserGetLisener userGetLisener) {
		try {
			Dao<TJUserInfo, UUID> mTJUserDao = new OrmLiteHelper(appContext).getTJUserInfoDao();
			QueryBuilder<TJUserInfo, UUID> builder = mTJUserDao.queryBuilder();
			Where<TJUserInfo, UUID> where = builder.where();
			where.eq("hxid", hxid);
			builder.setWhere(where);
			PreparedQuery<TJUserInfo> preparedQuery = builder.prepare();
			List<TJUserInfo> queryUserInfoList = mTJUserDao.query(preparedQuery);
			if (queryUserInfoList.size() > 0) {
				userGetLisener.onGetUser(queryUserInfoList.get(0));
			} else {
				Thread backgroundThread = new Thread(new Runnable() {

					@Override
					public void run() {
						// 从服务器获取
						RequestParams requestParams = new RequestParams();
						requestParams.add("hxid", hxid);
						((BaseApplication) appContext.getApplicationContext()).getSyncHttpClient().get(AppConstant.URL_BASE + AppConstant.URL_USERGET, requestParams,
								new TextHttpResponseHandler("UTF-8") {

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
													Dao<TJUserInfo, UUID> mTJUserDao = new OrmLiteHelper(appContext).getTJUserInfoDao();
													mTJUserDao.createIfNotExists(response.getData());
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
		} catch (SQLException e) {
			Log.e(TAG, e.getStackTrace().toString());
		}
	}

	/**
	 * 设置用户头像
	 * 
	 * @param username
	 */
	public static void setUserAvatar(final Context context, String hxid, final ImageView imageView) {
		getUserByHxid(context, hxid, new UserGetLisener() {

			@Override
			public void onGetUser(final TJUserInfo userInfo) {

				((Activity) context).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (userInfo != null && !StringUtils.isBlank(userInfo.getAvatarUrl())) {
							Picasso.with(context).load(userInfo.getAvatarUrl()).placeholder(com.tongjo.girlswish.R.drawable.default_avatar).into(imageView);
						} else {
							Picasso.with(context).load(com.tongjo.girlswish.R.drawable.default_avatar).into(imageView);
						}
					}
				});
			}
		});
	}

	public interface UserGetLisener {
		void onGetUser(TJUserInfo userInfo);
	}
}
