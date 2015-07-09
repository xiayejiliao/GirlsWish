package com.tongjo.bean;

public class TJWish {
	private String _id;
	private TJUserInfo creatorUser;
	private TJUserInfo pickerUser;
	private String content;
	private int isCompleted;
	private String createdTime;
	private int isPicked;
	private String pickedTime;
	private String backgroundColor;
	
	public TJWish() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public TJUserInfo getCreatorUser() {
		return creatorUser;
	}
	public void setCreatorUser(TJUserInfo creatorUser) {
		this.creatorUser = creatorUser;
	}
	public TJUserInfo getPickerUser() {
		return pickerUser;
	}
	public void setPickerUser(TJUserInfo pickerUser) {
		this.pickerUser = pickerUser;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(int isCompleted) {
		this.isCompleted = isCompleted;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public int getIsPicked() {
		return isPicked;
	}
	public void setIsPicked(int isPicked) {
		this.isPicked = isPicked;
	}
	public String getPickedTime() {
		return pickedTime;
	}
	public void setPickedTime(String pickedTime) {
		this.pickedTime = pickedTime;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	@Override
	public String toString() {
		return "TJWish [_id=" + _id + ", creatorUser=" + creatorUser + ", pickerUser=" + pickerUser + ", content=" + content + ", isCompleted=" + isCompleted + ", createdTime=" + createdTime
				+ ", isPicked=" + isPicked + ", pickedTime=" + pickedTime + ", backgroundColor=" + backgroundColor + "]";
	}
	
}
