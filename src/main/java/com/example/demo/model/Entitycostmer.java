package com.example.demo.model;

public class Entitycostmer {
    
	private int customerId;
	private String name;
	private String phone;
	private String email;
	private String address;
	private String password;
	
	
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Entitycostmer() {
		
	}
	public Entitycostmer(int customerId, String name, String phone, String email, String address) {
		this.customerId = customerId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.address = address;
	}
	
	public Entitycostmer(String name, String phone, String email, String address, String password) {
		//this.customerId = customerId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.password = password;
	}
	
	public Entitycostmer(int customerId, String name, String phone, String email, String address, String password) {
		this.customerId = customerId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.password = password;
	}
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
    
}