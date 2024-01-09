package com.sp.controller;

import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sp.dao.CategoryRepository;
import com.sp.entities.Category;
import com.sp.helper.Message;
import com.sp.service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
//@EnableScheduling
public class adminController {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryService categoryService;

//	admin home handler
	@GetMapping("")
	public String admin() {
		return "admin/home";
	}

//	category home handler
	@GetMapping("/category")
	public String categoryHome(Model model) {
		List<Category> allCategory = this.categoryService.getAllCategory();
		model.addAttribute("FAC", allCategory);
		return "admin/categoryHome";
	}

//	redirect to add category handler
	@GetMapping("/addcategory")
	public String addCategory(Model model) {
		model.addAttribute("title", "category add page ");
		model.addAttribute("category", new Category());
		return "admin/categoryAdd";
	}

//	 add category data save handler
	@PostMapping("/saveAddCategory")
	public String saveAddCategory(@ModelAttribute("category") Category category, Model model, HttpSession session) {
		try {
			this.categoryService.addCategory(category);
			session.setAttribute("message", new Message("Category added successfully .....", "success"));

			// Schedule a task to remove the session message after a delay
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					session.removeAttribute("message");
					timer.cancel(); // Cancel the timer after removing the message
				}
			}, 3000);

			// Set the expiry time for the message (10 seconds in this case)
			// Date expiryTime = new Date(System.currentTimeMillis() + 10000);
			// session.setAttribute("messageTime", expiryTime);

			return "redirect:/admin/addcategory";
		} catch (Exception e) {
			return "404";
		}

	}

//	delete category handler
	@GetMapping("/deleteCate/{cid}")
	public String deleteCategory(@PathVariable int cid, HttpSession session) {
		try {
			this.categoryService.deleteCategoryById(cid);
			session.setAttribute("message", new Message("Category deleted .....", "success"));
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					session.removeAttribute("message");
					timer.cancel(); // Cancel the timer after removing the message
				}
			}, 3000);
			return "redirect:/admin/category";
		} catch (Exception e) {
			return "404";
		}
	}

//	update category handler
	@GetMapping("/updateCate/{id}")
	public String updateCategory(@PathVariable("id") int id, Model model, HttpSession session) {
		try {
			Optional<Category> updatecate = categoryService.getCategoryById(id);
			if (updatecate.isPresent()) {
				model.addAttribute("category", updatecate.get());
				session.setAttribute("message", new Message("Category updated successfully .....", "success"));
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						session.removeAttribute("message");
						timer.cancel(); // Cancel the timer after removing the message
					}
				}, 3000);
				return "admin/categoryAdd";
			} else {
				return "404";

			}
		} catch (Exception e) {
			return "404";
		}

	}

}
