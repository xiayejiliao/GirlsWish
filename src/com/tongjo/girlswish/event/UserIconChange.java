package com.tongjo.girlswish.event;

public class UserIconChange extends BaseEvent {
	public String iconurl;

	public UserIconChange(String iconurl) {
		super();
		this.iconurl = iconurl;
	}

	public String getIconurl() {
		return iconurl;
	}

	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}
}
