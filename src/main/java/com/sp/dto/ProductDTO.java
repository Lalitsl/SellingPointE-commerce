package com.sp.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProductDTO {
    private int productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private int stock;
    private MultipartFile productImage; // Change to MultipartFile
  //  private String productImage;
    private int categoryid;
	
	
}
