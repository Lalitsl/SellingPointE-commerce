package com.sp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.dao.CategoryRepository;
import com.sp.entities.Category;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
//	add category 
	public void addCategory(Category category) {
		categoryRepository.save(category);
	}
	
//	get all category
	public List<Category> getAllCategory(){
		return categoryRepository.findAll();
	}
	
//	delete category 
	public void deleteCategoryById(int id) {
		categoryRepository.deleteById(id);
	}
	
//	delete all category
	public void deleteAllCategory() {
		categoryRepository.deleteAll();
	}
	
//	update category 
	public Optional<Category> getCategoryById(int id){
		return categoryRepository.findById(id);
		
	}
	
	
}
