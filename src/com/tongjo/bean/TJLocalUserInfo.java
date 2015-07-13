package com.tongjo.bean;

import java.util.UUID;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="userinfo")
public class TJLocalUserInfo extends TJUserInfo{
	private String loginStatus;
	public TJLocalUserInfo() {
		super();
	}
	
	public String getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	@Override
	public String toString() {
		return super.toString()+"TJLocalUserInfo [loginStatus=" + loginStatus + "]";
	}
	
	
}
