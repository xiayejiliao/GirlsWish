package com.tongjo.bean;

public class TJResponse<T> {
	private TJResult result;
	private T data;

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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "TJResponse [result=" + result + ", data=" + data + "]";
	}

}
