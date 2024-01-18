package com.sp.entities;

import java.sql.Date;

public class Order {
	
	private int orderId;
	private Date orderDate;
	private Float orderAmount;
	private String paymentReceived;
	private int quantity;
	
//	foreign key
	private User customer;
	private Product product;
	
	
}
