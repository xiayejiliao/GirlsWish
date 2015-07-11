package com.tongjo.bean;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="message")
public class TJMessage {
	@DatabaseField(id=true,index=true)
	private UUID _id;
	@DatabaseField
	private int type;
	@DatabaseField
	private String content;
	@DatabaseField(foreign=true,foreignAutoRefresh=true)
	private TJWish wish;
	@DatabaseField
	private String messageUrl;
	public TJMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UUID get_id() {
		return _id;
	}
	public void set_id(UUID _id) {
		this._id = _id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public TJWish getWish() {
		return wish;
	}
	public void setWish(TJWish wish) {
		this.wish = wish;
	}
	public String getMessageUrl() {
		return messageUrl;
	}
	public void setMessageUrl(String messageUrl) {
		this.messageUrl = messageUrl;
	}
	@Override
	public String toString() {
		return "TJMessage [_id=" + _id + ", type=" + type + ", content=" + content + ", wish=" + wish + ", messageUrl=" + messageUrl + "]";
	}
	
}
