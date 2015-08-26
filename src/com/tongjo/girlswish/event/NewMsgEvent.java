package com.tongjo.girlswish.event;

public class NewMsgEvent extends BaseEvent{
	int new_msg_count;

	public int getNew_msg_count() {
		return new_msg_count;
	}

	public void setNew_msg_count(int new_msg_count) {
		this.new_msg_count = new_msg_count;
	}
}
