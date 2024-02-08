package com.sp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sp.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

//	search product according to particular category for user section
	List<Product> findAllByCategory_Categoryid(int categoryId);

	List<Product> findAllByProductNameContaining(String productName);
	
//	search all products by price range
//	List<Product> findAllByProductPriceContaining(Integer productPrice);
	
	List<Product> findAllByProductPriceBetween(int i, int j);

	List<Product> findAllByDiscountBetween(int minDiscount, int maxDiscount);

	
}
