package com.tongjo.bean;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "message")
public class TJMessage {
	@DatabaseField(id = true, index = true)
	private UUID _id;
	@DatabaseField
	private int type;
	@DatabaseField
	private String title;
	@DatabaseField
	private String content;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private TJWish wish;
	@DatabaseField
	private String noticeUrl;
	@DatabaseField
	private String userId;
	@DatabaseField
	private String createdTime;
	/* 0:未读；1:已读 */
	@DatabaseField
	private boolean isRead;
	@DatabaseField
	private String hxid;
	@DatabaseField
	private String avatarUrl;
	@DatabaseField
	private String wishId;

	public TJMessage() {
		super();
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getNoticeUrl() {
		return noticeUrl;
	}

	public void setNoticeUrl(String noticeUrl) {
		this.noticeUrl = noticeUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public String getHxid() {
		return hxid;
	}

	public void setHxid(String hxid) {
		this.hxid = hxid;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getWishId() {
		return wishId;
	}

	public void setWishId(String wishId) {
		this.wishId = wishId;
	}

	@Override
	public String toString() {
		return "TJMessage [_id=" + _id + ", type" + type + ", title=" + title + ", content=" + content + ", wish=" + wish + ", noticeUrl=" + noticeUrl + ", createdTime=" + createdTime + "]";
	}

}
