package com.sp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sp.dao.CategoryRepository;
import com.sp.entities.Category;
import com.sp.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class adminController {

	@Autowired
	private CategoryRepository categoryRepository;
	
//	admin home handler
	@GetMapping("/admin")
	public String admin() {
		return "admin/home";
	}
	
//	admin category handler
	@GetMapping("/category")
	public String categoryHome(Model model) {
		List<Category> findAllCategory = this.categoryRepository.findAll();
		model.addAttribute("FAC", findAllCategory);
		System.out.println("all data : "+findAllCategory);
		return "admin/categoryHome";
	}
	
//	add category handler
	@GetMapping("/addcategory")
	public String addCategory(Model model){
		model.addAttribute("title", "category add page ");
		model.addAttribute("obj",new Category());
		return "admin/categoryAdd";
	}
	
//	 add category data save handler
	@PostMapping("/saveAddCategory")
	public String saveAddCategory(@ModelAttribute("category") Category category, Model model ,HttpSession session) {
		System.out.println("NAME : "+category.getClass());
		
		model.addAttribute("obj",new Category());
		category.setCategoryname(category.getCategoryname());
		category.setCategorydetails(category.getCategorydetails());
		Category save = this.categoryRepository.save(category);
		System.out.println("category added" + save);
		session.setAttribute("message", new Message("Category added successfully.....","success"));
		return "admin/categoryAdd";
		
		
		
	}
	
	
	
}
