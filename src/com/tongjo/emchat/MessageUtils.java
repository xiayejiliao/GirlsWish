package com.tongjo.emchat;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.core.e;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.tongjo.bean.TJMessage;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.db.OrmLiteHelper;
import com.tongjo.emchat.UserUtils.UserGetLisener;
import com.tongjo.girlswish.event.NewMsgEvent;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlwish.data.DataContainer;

import de.greenrobot.event.EventBus;

public class MessageUtils {
	public static String TAG = "MessageUtils";

	public static String getMessageDigest(EMMessage emMessage) {
		return "";
	}

	public static void addMessage(final Context appContext, final EMMessage emMessage) {
		try {
			int msg_type = emMessage.getIntAttribute("type", AppConstant.MSG_TYPE_CHAT);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
			if (msg_type == AppConstant.MSG_TYPE_CHAT) {

				TJMessage message = new TJMessage();
				message.setTitle(emMessage.getFrom());
				message.setHxid(emMessage.getFrom());
				message.setType(AppConstant.MSG_TYPE_CHAT);
				message.setCreatedTime(sdf.format(new Date(emMessage.getMsgTime())));
				message.setContent(((TextMessageBody) emMessage.getBody()).getMessage());
				message.setRead(false);

				addChatMessage(appContext, message, false);
			} else {
				TJMessage tjMessage = new TJMessage();
				tjMessage.set_id(UUID.fromString(emMessage.getStringAttribute("_id")));
				tjMessage.setTitle(emMessage.getStringAttribute("title"));
				tjMessage.setContent(emMessage.getStringAttribute("content"));
				tjMessage.setWishId(emMessage.getStringAttribute("wishId"));
				tjMessage.setUserId(emMessage.getStringAttribute("userId"));
				tjMessage.setNoticeUrl(emMessage.getStringAttribute("noticeUrl"));
				tjMessage.setType(emMessage.getIntAttribute("type"));
				tjMessage.setCreatedTime(emMessage.getStringAttribute("createdTime"));
				tjMessage.setRead(false);

				addSysMessage(appContext, tjMessage);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getStackTrace().toString());
		} finally {
			NewMsgEvent event = new NewMsgEvent();
			EventBus.getDefault().post(event);
		}
	}

	public static void addChatMessage(final Context appContext, final TJMessage message, boolean hasRead) {
		try {
			TJMessage addOrUpdateTJMessage = null;
			for (TJMessage tjMessage : DataContainer.MessageList) {
				if (!StringUtils.isBlank(tjMessage.getHxid()) && tjMessage.getHxid().equals(message.getHxid())) {
					// 如果消息最近时间未改变则表示没有更新的聊天消息，不需要更新消息记录
					if(tjMessage.getCreatedTime().equals(message.getCreatedTime())){
						tjMessage.setRead(message.isRead());
						return;
					}
					addOrUpdateTJMessage = tjMessage;
					DataContainer.MessageList.remove(tjMessage);
					break;
				}
			}
			if (addOrUpdateTJMessage == null) {
				addOrUpdateTJMessage = new TJMessage();
				addOrUpdateTJMessage.set_id(UUID.randomUUID());
				addOrUpdateTJMessage.setHxid(message.getHxid());
				addOrUpdateTJMessage.setType(AppConstant.MSG_TYPE_CHAT);
			}
			if (message.getUserId() == null && DataContainer.userInfoMap.containsKey(message.getHxid())) {
				TJUserInfo userInfo = DataContainer.userInfoMap.get(message.getHxid());
				addOrUpdateTJMessage.setUserId(userInfo.get_id().toString());
				addOrUpdateTJMessage.setTitle(userInfo.getNickname());
				addOrUpdateTJMessage.setAvatarUrl(userInfo.getAvatarUrl());
			} else {

			}
			addOrUpdateTJMessage.setContent(message.getContent());
			addOrUpdateTJMessage.setCreatedTime(message.getCreatedTime());
			addOrUpdateTJMessage.setRead(hasRead);
			DataContainer.MessageList.add(0, addOrUpdateTJMessage);
			final TJMessage tjMessage = addOrUpdateTJMessage;
			Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(appContext).getTJMessageDao();
			mTJMessageDao.createOrUpdate(addOrUpdateTJMessage);
			if (addOrUpdateTJMessage.getUserId() == null) {
				UserUtils.getUserByHxidFromNet(appContext, tjMessage.getHxid(), new UserGetLisener() {

					@Override
					public void onGetUser(TJUserInfo userInfo) {
						DataContainer.userInfoMap.put(userInfo.getHxid(), userInfo);
						tjMessage.setTitle(userInfo.getNickname());
						tjMessage.setAvatarUrl(userInfo.getAvatarUrl());
						tjMessage.setUserId(userInfo.get_id().toString());
						NewMsgEvent event = new NewMsgEvent();
						event.setNew_msg_count(1);
						EventBus.getDefault().post(event);

						Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(appContext).getTJMessageDao();
						try {
							mTJMessageDao.update(tjMessage);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			NewMsgEvent event = new NewMsgEvent();
			event.setNew_msg_count(1);
			EventBus.getDefault().post(event);
		}
	}

	public static void addSysMessage(final Context appContext, final TJMessage message) {
		DataContainer.SystemMsgList.add(message);
		TJMessage sysMessage = null;
		for (TJMessage tjMessage : DataContainer.MessageList) {
			if (tjMessage.getType() == AppConstant.MSG_TYPE_SYSTEM) {
				sysMessage = tjMessage;
				DataContainer.MessageList.remove(tjMessage);
				break;
			}
		}
		if (sysMessage == null) {
			sysMessage = new TJMessage();
			sysMessage.set_id(UUID.fromString(AppConstant.MSG_SYSTEM_UUID));
			sysMessage.setTitle("系统消息");
		}
		sysMessage.setContent(message.getTitle());
		sysMessage.setCreatedTime(message.getCreatedTime());
		sysMessage.setRead(false);
		sysMessage.setType(AppConstant.MSG_TYPE_SYSTEM);
		DataContainer.MessageList.add(0, sysMessage);
		try {
			Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(appContext).getTJMessageDao();
			mTJMessageDao.create(message);

			QueryBuilder<TJMessage, UUID> builder = mTJMessageDao.queryBuilder();
			Where<TJMessage, UUID> where = builder.where();
			where.in("type", Arrays.asList(AppConstant.MSG_TYPE_SYSTEM));
			builder.setWhere(where);
			PreparedQuery<TJMessage> preparedQuery = builder.prepare();
			List<TJMessage> querMessageList = mTJMessageDao.query(preparedQuery);
			if (querMessageList.size() <= 0) {
				mTJMessageDao.create(sysMessage);
			} else {
				mTJMessageDao.update(sysMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
	}
}
