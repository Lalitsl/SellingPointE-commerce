package com.sp.entities;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int categoryid;
	@Column(nullable = false)
	private String categoryname;
	@Column(nullable = false)
	private String categorydetails;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
	private List<Product> products=new ArrayList<>();

	
	
	public Category(int categoryid, String categoryname, String categorydetails) {
		super();
		this.categoryid = categoryid;
		this.categoryname = categoryname;
		this.categorydetails = categorydetails;
	}
	public Category() {
		super();
	}
	
	
	public int getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}
	public String getCategoryname() {
		return categoryname;
	}
	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}
	public String getCategorydetails() {
		return categorydetails;
	}
	public void setCategorydetails(String categorydetails) {
		this.categorydetails = categorydetails;
	}
	@Override
	public String toString() {
		return "category [categoryid=" + categoryid + ", categoryname=" + categoryname + ", categorydetails="
				+ categorydetails + "]";
	}
	
	
	
}
