package com.sp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sp.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

//	search product according to particular category for user section
	List<Product> findAllByCategory_Categoryid(int categoryId);
	
}
