package com.sp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sp.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	

}

