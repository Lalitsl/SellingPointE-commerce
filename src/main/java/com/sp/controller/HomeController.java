package com.sp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sp.dao.CustomerRepository;
import com.sp.dao.RoleRepository;
import com.sp.entities.Category;
import com.sp.entities.Customer;
import com.sp.entities.Product;
import com.sp.helper.Message;
import com.sp.service.CategoryService;
import com.sp.service.ProductService;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Selling point home page");
		return "index";
	}
	
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
        System.out.println("Initialization method called on application startup");
        ModelAndView model = new ModelAndView();
        this.base(model);
    }
    @GetMapping("/base")
	public ModelAndView base(ModelAndView model) {
    	 System.out.println("automatically controller base method called  ");
		List<Category> allCategory = this.categoryService.getAllCategory();
		model.addObject("category", allCategory);
		model.setViewName("base");
		return model;
	}
    
	 
	@GetMapping("/blog")
	public String blog() {
		return "blogList";
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}
	
	@GetMapping("/404")
	public String error() {
		return "404";
	}
	
	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}
	
//	customer registration handler 
	@PostMapping("/submitCustomerSignup")
	public String submitCustomerSignup(@ModelAttribute("customer") Customer customer ,HttpSession session) {
		customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
		Customer save = customerRepository.save(customer);
		session.setAttribute("message", new Message("User saved successfully .....", "success"));
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				session.removeAttribute("message");
				timer.cancel(); // Cancel the timer after removing the message
			}
		}, 3000);
		System.out.println("data saved successfully");
		
		return "signin";
	}
	
	
	@GetMapping("/signin")
	public String signin() {
		return "signin";
	}
	 

}
