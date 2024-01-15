package com.sp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sp.dao.ProductRepository;
import com.sp.entities.Product;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
//	add product 
	public void addProduct(Product product) {
		productRepository.save(product);
	}
	
//	get all product 
	 public Page<Product> getAllProduct(Pageable pageable) {
	        return productRepository.findAll(pageable);
	    }

//	update product 
	public Optional<Product> getProductById(int id){
		return productRepository.findById(id);
	}
	
//	delete product
	public void deleteProductById(int id) {
		productRepository.deleteById(id);
	}
	
//	delete all product
	public void deleteAllProduct() {
		productRepository.deleteAll();
	}
	
//	user sorting 
	public List<Product> getAllProductsByCategoryId(int categoryId){
	    return productRepository.findAllByCategory_Categoryid(categoryId);
	}
	
	

}

