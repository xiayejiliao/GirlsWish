package com.tongjo.girlswish.event;

public class UnReadSetEvent extends BaseEvent{
	private boolean isUnread;

	public boolean isUnread() {
		return isUnread;
	}

	public void setUnread(boolean isUnread) {
		this.isUnread = isUnread;
	}
}
