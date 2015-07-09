package com.tongjo.bean;

public class TJMessage {
	private String _id;
	private int type;
	private String content;
	private TJWish wish;
	private String messageUrl;
	public TJMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
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
