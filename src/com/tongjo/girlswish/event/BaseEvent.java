package com.tongjo.girlswish.event;

import android.R.integer;

public class BaseEvent {
	public int eventtype;
	public int arg0;
	public int arg1;
	public String str;
	public Object obj;
	public int getEventtype() {
		return eventtype;
	}
	public void setEventtype(int eventtype) {
		this.eventtype = eventtype;
	}
	public int getArg0() {
		return arg0;
	}
	public void setArg0(int arg0) {
		this.arg0 = arg0;
	}
	public int getArg1() {
		return arg1;
	}
	public void setArg1(int arg1) {
		this.arg1 = arg1;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
