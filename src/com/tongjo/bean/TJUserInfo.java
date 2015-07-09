package com.tongjo.bean;

public class TJUserInfo {
	private String _id;
	private String email;
	private String tel;
	private String realname;
	private int gender;
	private String nickname;
	private String avatarUrl;
	private TJSchool school;
	public TJUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
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
