package com.user.entity;

import java.util.ArrayList;
import java.util.List;

public class User 
{
	private Long userId;
	private String name;
	private String phone;
	private String department;
	
	public User(Long userId, String name, String phone,String department) 
	{
		this.userId = userId;
		this.name = name;
		this.phone = phone;	
		this.department = department;	
	}
	
	public User(Long userId, String name, String phone, String department,List<Contact> contacts) 
	{
		this.userId = userId;
		this.name = name;
		this.phone = phone;
		this.department = department;
	}
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
