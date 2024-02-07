package com.sp.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data

public class SubCategory {

	private int subCategoryId;
	private String subCategoryTitle;
	private String subCategoriDetails;
	
	@ManyToOne
	private Category category;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
	private List<Product> products=new ArrayList<>();
	
}
