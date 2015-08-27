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

				addChatMessage(appContext, message);
			} else {
				// 将消息存入数据库
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
		} finally{
			NewMsgEvent event = new NewMsgEvent();
			EventBus.getDefault().post(event);
		}
	}

	public static void updateChatMessage(final Context appContext, final TJMessage message){
		Iterator<TJMessage> msgIterator = DataContainer.MessageList.iterator();
		TJMessage updateMessage = null;
        while(msgIterator.hasNext()){  
        	TJMessage next = msgIterator.next();
        	if(next.getHxid() != null && next.getHxid().equals(message.getHxid())){
        		msgIterator.remove();
        		updateMessage = next;
        		break;
        	}
        }
        if(updateMessage == null){
        	updateMessage = new TJMessage();
        	if(DataContainer.userInfoMap.containsKey(message.getHxid())){
            	TJUserInfo userInfo  = DataContainer.userInfoMap.get(message.getHxid());
            	updateMessage.setUserId(userInfo.get_id().toString());
            	updateMessage.setTitle(userInfo.getNickname());
            	updateMessage.setAvatarUrl(userInfo.getAvatarUrl());
            }
        }
        updateMessage.setContent(message.getContent());
        updateMessage.setCreatedTime(message.getCreatedTime());
        updateMessage.setRead(true);
        DataContainer.MessageList.add(0, updateMessage);
        Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(appContext).getTJMessageDao();
        try {
			mTJMessageDao.update(updateMessage);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			NewMsgEvent event = new NewMsgEvent();
			event.setNew_msg_count(1);
			EventBus.getDefault().post(event);
		}
	}
	public static void addChatMessage(final Context appContext, final TJMessage message) {
		Iterator<TJMessage> msgIterator = DataContainer.MessageList.iterator();
        while(msgIterator.hasNext()){ 
        	TJMessage tempMessage = msgIterator.next();
        	if(!StringUtils.isBlank(tempMessage.getHxid()) && tempMessage.getHxid().equals(message.getHxid())){
        		msgIterator.remove();
        		break;
        	}
        }  
        if(message.getUserId() == null && DataContainer.userInfoMap.containsKey(message.getHxid())){
        	TJUserInfo userInfo  = DataContainer.userInfoMap.get(message.getHxid());
        	message.setUserId(userInfo.get_id().toString());
        	message.setTitle(userInfo.getNickname());
        	message.setAvatarUrl(userInfo.getAvatarUrl());
        }
		DataContainer.MessageList.add(0, message);
		try {
			Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(appContext).getTJMessageDao();
			QueryBuilder<TJMessage, UUID> builder = mTJMessageDao.queryBuilder();
			Where<TJMessage, UUID> where = builder.where();
			where.in("type", Arrays.asList(AppConstant.MSG_TYPE_CHAT));
			where.and();
			where.eq("hxid", message.getHxid());
			builder.setWhere(where);
			builder.orderBy("createdTime", false);
			PreparedQuery<TJMessage> preparedQuery = builder.prepare();
			List<TJMessage> querMessageList = mTJMessageDao.query(preparedQuery);
			if (querMessageList.size() <= 0) {
				message.set_id(UUID.randomUUID());
				mTJMessageDao.create(message);
				if(message.getUserId() == null){
				UserUtils.getUserByHxid(appContext, message.getHxid(), new UserGetLisener() {

					@Override
					public void onGetUser(TJUserInfo userInfo) {
						DataContainer.userInfoMap.put(userInfo.getHxid(), userInfo);
						message.setTitle(userInfo.getNickname());
			        	message.setAvatarUrl(userInfo.getAvatarUrl());
			        	message.setUserId(userInfo.get_id().toString());
			        	
			        	Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(appContext).getTJMessageDao();
			        	try {
							mTJMessageDao.update(message);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
				}
			} else {
				message.set_id(querMessageList.get(0).get_id());
				mTJMessageDao.update(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		} finally {
			NewMsgEvent event = new NewMsgEvent();
			event.setNew_msg_count(1);
			EventBus.getDefault().post(event);
		}
	}

	public static void addSysMessage(final Context appContext, final TJMessage message) {
		DataContainer.SystemMsgList.add(message);
		Iterator<TJMessage> msgIterator = DataContainer.MessageList.iterator();
		TJMessage sysMessage = null;
        while(msgIterator.hasNext()){  
        	TJMessage next = msgIterator.next();
        	if(next.getType() == AppConstant.MSG_TYPE_SYSTEM){
        		sysMessage = next;
        		msgIterator.remove();
        		break;
        	}
        }  
        if(sysMessage == null){
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
