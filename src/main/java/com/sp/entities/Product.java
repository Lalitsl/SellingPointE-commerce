package com.sp.entities;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productId;
	@Column(nullable = false)
	private String productName;
	@Column(nullable = false)
	private String productDescription;
	@Column(nullable = false)
	private String productCompanyName;
	@Column(nullable = false)
	private Double productPrice;
	private int quantity;
	private String productImage;
	private double discount;
	private String Option1;
	private String Option2;
	private String Option3;
	private String Option4;
	private LocalDate productDate;
	
	

//	foreign KEY 

	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name="categoryid", referencedColumnName ="categoryid")
	 private Category category;


//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "subCategoryId", referencedColumnName = "subCategoryId")
//	SubCategory subCategory;

	 
}
