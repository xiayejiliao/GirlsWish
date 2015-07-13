package com.tongjo.girlswish.model;

public enum UserSex {
	MAN(1),WOMEN(0);
	private int value;
	private UserSex(int value) {
		// TODO Auto-generated constructor stub
		this.value=value;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
}
