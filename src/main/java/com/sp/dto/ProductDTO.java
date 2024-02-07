package com.sp.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProductDTO {
    private int productId;
    private String productName;
    private String productDescription;
    private String productCompanyName;
    private Double productPrice;
    private int quantity;
    private int discount;
	private String Option1;
	private String Option2;
	private String Option3;
	private String Option4;
	private Date productDate;
    private MultipartFile productImage; // Change to MultipartFile
  //  private String productImage;
    private String categoryid;
	
	
}
