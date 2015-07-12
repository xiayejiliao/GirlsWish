package com.tongjo.bean;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="wish")
public class TJWish {
	@DatabaseField(id=true,index=true)
	private UUID _id;
	@DatabaseField(foreign=true,foreignAutoRefresh=true)
	private TJUserInfo creatorUser;
	@DatabaseField(foreign=true,foreignAutoRefresh=true)
	private TJUserInfo pickerUser;
	@DatabaseField
	private String content;
	@DatabaseField
	private int isCompleted;
	@DatabaseField
	private String createdTime;
	@DatabaseField
	private int isPicked;
	@DatabaseField
	private String pickedTime;
	@DatabaseField
	private String backgroundColor;
	
	public TJWish() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UUID get_id() {
		return _id;
	}
	public void set_id(UUID _id) {
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
