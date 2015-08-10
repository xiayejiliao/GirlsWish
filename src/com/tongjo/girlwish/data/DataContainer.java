package com.tongjo.girlwish.data;

import java.util.ArrayList;
import java.util.List;

import com.tongjo.bean.TJLocalUserInfo;
import com.tongjo.bean.TJMessage;
import com.tongjo.bean.TJWish;
import com.tongjo.bean.TJWishList;

/**
 * 用于在内存在存储临时数据
 * 新建一个存储的时候，记得在clear中写清除方法
 * @author fuzhen
 *
 */
public class DataContainer {
	public final static List<TJWish> WishList = new ArrayList<TJWish>();
	// 消息界面的消息
	public final static List<TJMessage> MessageList = new ArrayList<TJMessage>();
	// 系统消息界面的系统消息
	public final static List<TJMessage> SystemMsgList = new ArrayList<TJMessage>();
	public static TJWishList mewishs=new TJWishList();
	public static TJLocalUserInfo userInfo= new TJLocalUserInfo();
	
	public void clear(){
		if(WishList != null){
			WishList.clear();
		}
		if(MessageList != null){
			MessageList.clear();
		}
		if(SystemMsgList != null){
			SystemMsgList.clear();
		}
		if(mewishs!=null){
			mewishs.clear();
		}
	}
} 
