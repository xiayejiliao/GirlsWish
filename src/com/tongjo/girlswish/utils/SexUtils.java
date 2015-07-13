package com.tongjo.girlswish.utils;
/**
 * 性别判断，需要与服务端对应
 * Copyright 2015 
 * @author preparing
 * @date 2015-7-13
 */
public class SexUtils {
	private static final int womenInt = 0;
	private static final int menInt = 1;
	private static final String womenString = "女";
	private static final String menString = "男";
	
	
	public static String getSexString(int sex){
		if(sex == womenInt){
			return womenString;
		}else if(sex == menInt){
			return menString;
		}else{
			return "不公开";
		}
	}
}
