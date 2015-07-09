package com.tongjo.bean;

public class TJResult {
	private int code;
	private String message;
	public TJResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "TJResult [code=" + code + ", message=" + message + "]";
	}
	
}
