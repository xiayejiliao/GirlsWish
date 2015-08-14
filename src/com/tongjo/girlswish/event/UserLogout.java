package com.tongjo.girlswish.event;

public class UserLogout extends BaseEvent {
	private int state;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}