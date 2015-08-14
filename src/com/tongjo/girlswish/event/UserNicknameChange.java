package com.tongjo.girlswish.event;

public class UserNicknameChange extends BaseEvent {
	public String name;

	public UserNicknameChange(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
