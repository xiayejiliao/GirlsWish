package com.tongjo.bean;

import java.util.UUID;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="userinfo")
public class TJUserInfo {
	@DatabaseField(id=true,index=true)
	private UUID _id;
	@DatabaseField
	private String email;
	@DatabaseField
	private String tel;
	@DatabaseField
	private String realname;
	@DatabaseField
	private int gender;
	@DatabaseField
	private String nickname;
	@DatabaseField
	private String avatarUrl;
	@DatabaseField(foreign=true,foreignAutoRefresh=true)
	private TJSchool school;
	public TJUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UUID get_id() {
		return _id;
	}
	public void set_id(UUID _id) {
		this._id = _id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public TJSchool getSchool() {
		return school;
	}
	public void setSchool(TJSchool school) {
		this.school = school;
	}
	@Override
	public String toString() {
		return "TJUserInfo [_id=" + _id + ", email=" + email + ", tel=" + tel + ", realname=" + realname + ", gender=" + gender + ", nickname=" + nickname + ", avatarUrl=" + avatarUrl + ", school="
				+ school + "]";
	}
	
}
