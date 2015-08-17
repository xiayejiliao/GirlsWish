package com.tongjo.emchat;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.tongjo.bean.TJMessage;
import com.tongjo.bean.TJMessageList;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.db.OrmLiteHelper;
import com.tongjo.emchat.UserUtils.UserGetLisener;
import com.tongjo.girlswish.utils.AppConstant;

public class MessageUtils {
	public static String TAG = "MessageUtils";

	public static String getMessageDigest(EMMessage emMessage) {
		return "";
	}

	public static void addMessageToDb(final Context appContext, final EMMessage emMessage) {
		try {
			int msg_type = emMessage.getIntAttribute("type", AppConstant.MSG_TYPE_SYSTEM);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
			if (msg_type == AppConstant.MSG_TYPE_CHAT) {
				Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(appContext).getTJMessageDao();
				QueryBuilder<TJMessage, UUID> builder = mTJMessageDao.queryBuilder();
				Where<TJMessage, UUID> where = builder.where();
				// 消息界面只显示聊天消息（和一个人的聊天为一条记录）和系统消息
				where.in("type", Arrays.asList(AppConstant.MSG_TYPE_CHAT));
				where.eq("hxid", emMessage.getFrom());
				builder.setWhere(where);
				builder.orderBy("createdTime", false);
				PreparedQuery<TJMessage> preparedQuery = builder.prepare();
				List<TJMessage> querMessageList = mTJMessageDao.query(preparedQuery);
				TJMessage message;
				if (querMessageList.size() <= 0) {
					message = new TJMessage();
					message.setHxid(emMessage.getFrom());
					message.setTitle(emMessage.getFrom());
					mTJMessageDao.createIfNotExists(message);

					UserUtils.getUserByHxid(appContext, emMessage.getFrom(), new UserGetLisener() {

						@Override
						public void onGetUser(TJUserInfo userInfo) {
							try {
								Dao<TJUserInfo, UUID> mTJUerInfoDao = new OrmLiteHelper(appContext).getTJUserInfoDao();
								mTJUerInfoDao.createIfNotExists(userInfo);
								Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(appContext).getTJMessageDao();
								QueryBuilder<TJMessage, UUID> builder = mTJMessageDao.queryBuilder();
								Where<TJMessage, UUID> where = builder.where();
								// 消息界面只显示聊天消息（和一个人的聊天为一条记录）和系统消息
								where.in("type", Arrays.asList(AppConstant.MSG_TYPE_CHAT));
								where.eq("hxid", emMessage.getFrom());
								builder.setWhere(where);
								builder.orderBy("createdTime", false);
								PreparedQuery<TJMessage> preparedQuery = builder.prepare();
								List<TJMessage> querMessageList = mTJMessageDao.query(preparedQuery);
								if (querMessageList.size() > 0) {
									querMessageList.get(0).setTitle(userInfo.getRealname());
									querMessageList.get(0).setAvatarUrl(userInfo.getAvatarUrl());
									mTJMessageDao.update(querMessageList.get(0));
								}
							} catch (SQLException e) {
								Log.e(TAG, e.getStackTrace().toString());
							}
						}
					});
				} else {
					message = querMessageList.get(0);
				}
				message.setRead(false);
				message.setType(AppConstant.MSG_TYPE_CHAT);
				message.setCreatedTime(sdf.format(new Date(emMessage.getMsgTime())));
				message.setContent(((TextMessageBody) emMessage.getBody()).getMessage());
				mTJMessageDao.update(message);
			} else {
				Type type = new TypeToken<TJMessageList>() {
				}.getType();
				TJMessageList data = new Gson().fromJson(((TextMessageBody) emMessage.getBody()).getMessage(), type);
				List<TJMessage> messageList = (List<TJMessage>) data.getNotices();
				Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(appContext).getTJMessageDao();
				for (TJMessage message : messageList) {
					message.setRead(false);
					mTJMessageDao.createIfNotExists(message);
				}

				QueryBuilder<TJMessage, UUID> builder = mTJMessageDao.queryBuilder();
				Where<TJMessage, UUID> where = builder.where();
				where.in("type", Arrays.asList(AppConstant.MSG_TYPE_SYSTEM));
				builder.setWhere(where);
				PreparedQuery<TJMessage> preparedQuery = builder.prepare();
				List<TJMessage> querMessageList = mTJMessageDao.query(preparedQuery);
				TJMessage message;
				if (querMessageList.size() <= 0) {
					message = new TJMessage();
					message.set_id(UUID.fromString(AppConstant.MSG_SYSTEM_UUID));
					message.setTitle("系统消息");
					mTJMessageDao.createIfNotExists(message);
				} else {
					message = querMessageList.get(0);
				}
				message.setContent(((TextMessageBody) emMessage.getBody()).getMessage());
				message.setCreatedTime(sdf.format(new Date(emMessage.getMsgTime())));
				message.setRead(false);
				message.setType(AppConstant.MSG_TYPE_SYSTEM);
				mTJMessageDao.update(message);
			}
		} catch (SQLException e) {
			Log.e(TAG, e.getStackTrace().toString());
		}
	}
}
