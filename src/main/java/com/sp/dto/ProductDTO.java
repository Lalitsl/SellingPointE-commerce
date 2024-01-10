package com.sp.dto;

import lombok.Data;

@Data
public class ProductDTO {
	
	private int productId;
	private String productName;
	private String productDescription;
	private Double productPrice;
	private int stock;
	private String productImage;
	private int categoryId;
	
	
}
