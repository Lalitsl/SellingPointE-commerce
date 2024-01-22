package com.sp.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sp.dao.UserRepository;
import com.sp.entities.Category;
import com.sp.entities.User;
import com.sp.entities.Product;
import com.sp.helper.Message;
import com.sp.helper.RemoveSession;
import com.sp.service.CategoryService;
import com.sp.service.ProductService;
import com.sp.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserService userService;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	

//	 Handler For home page 
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Selling point home page");
		return "index";
	}

//	 Handler For about page 
	@GetMapping("/about")
	public String about() {
		return "about";
	}

//	@GetMapping("/product")
//	public String products(Model model) {
//		List<Category> allCategory = this.categoryService.getAllCategory();
//		List<Product> allProductsByCategoryId = this.productService.getAllProductsByCategoryId(categoryService.get);
//		model.addAttribute("category", allCategory);
//		return "product";
//	}

//	 Handler For product page 
	@GetMapping("/product")
	public String products(Model model) {
		List<Category> allCategory = this.categoryService.getAllCategory();
		// Loop through each category and fetch products
		Map<Integer, List<Product>> productsByCategoryId = new HashMap<>();
		for (Category category : allCategory) {
			int categoryId = category.getCategoryid();
			List<Product> productsForCategory = this.productService.getAllProductsByCategoryId(categoryId);
			productsByCategoryId.put(categoryId, productsForCategory);
		}
		model.addAttribute("category", allCategory);
		model.addAttribute("productsByCategoryId", productsByCategoryId);
		return "product";
	}

	@PostConstruct
	public void init() {
		ModelAndView model = new ModelAndView();
		this.base(model);
		
	}

	@GetMapping("/base")
	public ModelAndView base(ModelAndView model) {
		List<Category> allCategory = this.categoryService.getAllCategory();
		model.addObject("category", allCategory);
		model.setViewName("base");
		return model;
	}
	
	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if(p != null) {
			String email = p.getName();
			User user = userRepository.findUserByEmail(email).get();
			m.addAttribute("user", user);
			System.out.println("EMAIL : "+user.getEmail());
			System.out.println("USERNAME : "+user.getUserName());
		}
	}

//	 Handler For blog page 
	@GetMapping("/blog")
	public String blog() {
		return "blogList";
	}

//	 Handler For contact page 
	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}

//	 Handler For error page 
	@GetMapping("/404")
	public String error() {
		return "404";
	}

//	 Handler For redirect to sign-up page 
	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}
	
//	Handler For user registration process 
	@PostMapping("/submitUserSignup")
	public String submitUserSignup(@ModelAttribute("user") User user, HttpSession session) {
		try {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setRole("ROLE_USER");
			User u = userService.addUser(user);
			if (u != null) {
				session.setAttribute("message", new Message("User saved successfully .....", "success"));
				System.out.println("data saved successfully");
				return "redirect:/signup";
			} else {
				session.setAttribute("message", new Message("something wrong .....", "danger"));
				System.out.println("data not saved !!! ");
				return "redirect:/signup";
			}
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong .....", "danger"));
			System.out.println("Exception in catch block !!! ");
			return "redirect:/signup";
		}
	}



//	 Handler For redirect to sign-in page 
	@GetMapping("/signin")
	public String signin() {
		return "signin";
	}



 
	

	
	
	
}
