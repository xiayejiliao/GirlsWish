package com.tongjo.girlswish.utils;

import android.os.Environment;

/**
 * 存放一些常量
 * 
 * @author 16ren
 * @date 2015-7-8
 */
public class AppConstant {
	// path
	public static final String path = Environment.getExternalStorageDirectory().getPath() + "/girlWish";

	// SharedPreferences
	public static final String USER_PHONE = "user_pohone";
	public static final String USER_PASSWORD = "user_password";
	public static final String USER_LOGINSTATE = "user_loginstate";
	public static final String USER_ICONPATH = "user_iconpath";
	public static final String USER_ICONURL = "user_iconurl";
	public static final String USER_ISREMEMBER = "user_isremeber";
	public static final String USER_SEX = "user_sex";
	public static final String USER_NAME = "user_name";
	public static final String USER_ID = "user_id";
	public static final String USER_SCHOOL = "user_school";

	// web
	public static final String URL_BASE = "http://api.wish.tongjo.com";
	public static final String URL_LOGIN = "/login";
	public static final String URL_LOGOUT = "/logout";
	public static final String URL_WISHDEL = "/wish/delete";
	public static final String URL_WISHUPDATE= "/wish/update";

	public static final String URL_WISHLIST = "/wish/mylist";
	public static final String URL_WISH = "/wish";
	public static final String URL_ADDWISH = "/wish/create";
	public static final String URL_PICKWISH = "/wish/pick";
	public static final String URL_MESSAGE = "/notice";

	// intent
	public static final int FORRESULT_LOG = 10101;
	public static final int FORRESULT_LOG_OK = 10102;
	public static final int FORRESULT_LOG_CANCANL = 10103;
	public static final int FORRESULT_MEINFO = 10200;
	public static final int FORRESULT_MEINFO_LOGOUT = 10201;

	// refreshView
	public static final int REFRESH_WISH = 0x100;
	public static final int REFRESH_INFO = 0x101;

	// evenbus
	public static final int MESSAGE_WHAT_GIRLWISH_CLICK_UNPICK= 10303;
	public static final int MESSAGE_WHAT_GIRLWISH_CLICK_PCIK = 10304;
	public static final int MESSAGE_WHAT_GIRLWISH_CLICK_FINISH = 10305;
	public static final int MESSAGE_WHAT_BOYWISH_CLICK_UNCOMPLETE = 10306;
	public static final int MESSAGE_WHAT_BOYWISH_CLICK_COMPLETE = 10307;
	
	public static final int MESSAGE_WHAT_GIRLWISH_UPDATE = 10308;
	public static final int MESSAGE_WHAT_GIRLWISH_DEL = 10309;
	
	

}
