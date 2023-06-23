package com.dto.mangoplate;

import java.util.Date;

public class User {

	private String name;
	private String address;
	private String userID;
	private String passwd;
	private String email;
	private String phone;
	private String birth;
	private int user_type;
	
	public User() {
		
	}
	
	public User(String userID, String passwd, String name, String address, String phone, String email, String birth, int user_type) {
		this.userID = userID;
		this.passwd = passwd;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.birth = birth;
		this.user_type = user_type;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public int getUser_type() {
		return user_type;
	}
	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}
	
	
	
}
