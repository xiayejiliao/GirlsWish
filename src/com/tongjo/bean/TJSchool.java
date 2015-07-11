package com.tongjo.bean;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="school")
public class TJSchool {
	@DatabaseField(id=true,index=true)
	private UUID _id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String coordinates;
	public TJSchool() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UUID get_id() {
		return _id;
	}
	public void set_id(UUID _id) {
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
