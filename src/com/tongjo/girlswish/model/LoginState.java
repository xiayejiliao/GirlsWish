package com.tongjo.girlswish.model;

public enum LoginState {
	LOGIN(0),LOGOUT(1);
	private int value;
	LoginState(int value){
		this.value=value;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
}
