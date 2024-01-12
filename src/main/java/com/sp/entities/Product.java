package com.sp.entities;


import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int productId;
	@Column(nullable = false)
	private String productName;
	@Column(nullable = false)
	private String productDescription;
	@Column(nullable = false)
	private String productCompanyName;
	@Column(nullable = false)
	private Double productPrice;
	private int stock;
	private String productImage;
//	foreign KEY 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="categoryid", referencedColumnName ="categoryid")
	private Category category;

	
	
}
