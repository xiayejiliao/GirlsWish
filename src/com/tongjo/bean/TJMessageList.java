package com.tongjo.bean;

import java.util.List;

public class TJMessageList {
	private int total;
	private List<TJMessage> notices;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<TJMessage> getNotices() {
		return notices;
	}
	public void setNotices(List<TJMessage> notices) {
		this.notices = notices;
	}
}
