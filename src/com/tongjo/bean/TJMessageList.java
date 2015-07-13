package com.tongjo.bean;

import java.util.List;

public class TJMessageList {
	private int total;
	private List<TJMessage> messageList;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<TJMessage> getMessageList() {
		return messageList;
	}
	public void setMessageList(List<TJMessage> messageList) {
		this.messageList = messageList;
	}
}
