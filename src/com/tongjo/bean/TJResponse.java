package com.tongjo.bean;

public class TJResponse {
	private TJResult result;
	private String data;
	public TJResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TJResult getResult() {
		return result;
	}
	public void setResult(TJResult result) {
		this.result = result;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "TJResponse [result=" + result + ", data=" + data + "]";
	}
	
}
