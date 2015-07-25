package com.tongjo.bean;

import java.util.UUID;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="userinfo")
public class TJLocalUserInfo extends TJUserInfo{
	private String loginStatus;
	private String schoolName;
	public TJLocalUserInfo() {
		super();
	}
	
	public String getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	@Override
	public String toString() {
		return super.toString()+"TJLocalUserInfo [loginStatus=" + loginStatus + "]";
	}
	
	
}
