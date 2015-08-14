package com.tongjo.girlswish.event;

public class UserSchoolnameChange extends BaseEvent{
	public String schoolname;

	public UserSchoolnameChange(String schoolname) {
		super();
		this.schoolname = schoolname;
	}

	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}
	
}
