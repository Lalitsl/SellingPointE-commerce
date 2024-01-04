package com.sp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int customerId;
	private String customerName;
	private String address;
	/* private String type; */
	private String mobile;
	private String email;
	private String password;
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public customer(int customerId, String customerName, String address, String type, String mobile, String email,
			String password) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.address = address;
		this.mobile = mobile;
		this.email = email;
		this.password = password;
	}
	public customer() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "customer [customerId=" + customerId + ", customerName=" + customerName + ", address=" + address
				+ ", mobile=" + mobile + ", email=" + email + ", password=" + password + "]";
	}
	
	
	

}
