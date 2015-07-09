package com.tongjo.bean;

public class TJSchool {
	private String _id;
	private String name;
	private String coordinates;
	public TJSchool() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	@Override
	public String toString() {
		return "TJSchool [_id=" + _id + ", name=" + name + ", coordinates=" + coordinates + "]";
	}
	
}
