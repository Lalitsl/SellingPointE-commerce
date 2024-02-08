package com.sp.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sp.dao.UserRepository;
import com.sp.entities.Category;
import com.sp.entities.User;
import com.sp.global.GlobalData;
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
		try {
			model.addAttribute("title", "Selling point home page");
//			get side-bar drop-down all category from database
			List<Category> allCategory = this.categoryService.getAllCategory();
			model.addAttribute("category", allCategory);
			List<Product> allProduct = this.productService.getAllProduct();
			model.addAttribute("AllProduct", allProduct);
//			cart quantity counting
			model.addAttribute("cartCount", GlobalData.cart.size());
			return "index";
		} catch (Exception e) {
			return "404";
		}
	}

//	 Handler For about page 
	@GetMapping("/about")
	public String about(Model model) {
//		get side-bar drop-down all category from database
		List<Category> allCategory = this.categoryService.getAllCategory();
		model.addAttribute("category", allCategory);
		return "about";
	}

	@GetMapping("/product")
	public String products(Model model) {
		try {
//			get side-bar drop-down all category from database
			List<Category> allCategory = this.categoryService.getAllCategory();
			List<Product> allProduct = this.productService.getAllProduct();
			model.addAttribute("AllProduct", allProduct);
			model.addAttribute("category", allCategory);
//			cart quantity counting
			model.addAttribute("cartCount", GlobalData.cart.size());
			return "product";
		} catch (Exception e) {
			return "404";
		}
	}

	@GetMapping("/showAllProductByCategoryId/{id}")
	public String showAllProductByCategoryId(@PathVariable Integer id, Model model) {
		try {
//			get side-bar drop-down all category from database
			List<Category> allCategory = this.categoryService.getAllCategory();
			model.addAttribute("category", allCategory);
			List<Product> allProductsByCategoryId = this.productService.getAllProductsByCategoryId(id);
			Optional<Category> categoryById = this.categoryService.getCategoryById(id);
			model.addAttribute("allProducts", allProductsByCategoryId);
			model.addAttribute("category01", categoryById.orElse(null)); // Extract the value or provide a default value
			return "particularProductItem";
		} catch (Exception e) {
			return "404";
		}
	}

//	particular product details page
	@GetMapping("/productDetailsPageById/{id}")
	public String productDetailsPageById(@PathVariable Integer id, Model model) {
		try {
//			get side-bar drop-down all category from database
			List<Category> allCategory = this.categoryService.getAllCategory();
			model.addAttribute("category", allCategory);

			Product product = this.productService.getProductById(id).get();
			model.addAttribute("product01", product); // Extract the value or provide a default value
			return "productDetails";
		} catch (Exception e) {
			return "404";
		}
	}

// 	search products 
	@GetMapping("/searchProduct")
	public String searchProduct(@RequestParam("productName") String productName, Model model) {
		try {
//			get side-bar drop-down all category from database
			List<Category> allCategory = this.categoryService.getAllCategory();
			model.addAttribute("category", allCategory);

			List<Product> allProductByProductName = this.productService.getAllProductByProductName(productName);
			model.addAttribute("AllProduct", allProductByProductName);
			return "product";
		} catch (Exception e) {
			return "404";
		}

	}

//	search product by product price 
	@GetMapping("/searchProductsByPrice")
	public String searchProductsByPriceRange(@RequestParam Integer minValue, @RequestParam Integer maxValue,
			Model model) {
		try {
//			get side-bar drop-down all category from database
			List<Category> allCategory = this.categoryService.getAllCategory();
			model.addAttribute("category", allCategory);
			
			List<Product> products = this.productService.searchProductsByPriceRange(minValue, maxValue);
			model.addAttribute("AllProduct", products);
			return "product";
		} catch (Exception e) {
			return "404";
		}
	}

//	search product by Discount
	@GetMapping("/searchProductByDiscount")
	public String searchProductByDiscount(@RequestParam Integer minDiscount, @RequestParam Integer maxDiscount, Model model) {
		try {
//			get side-bar drop-down all category from database
			List<Category> allCategory = this.categoryService.getAllCategory();
			model.addAttribute("category", allCategory);
			
			List<Product> searchProductsByDiscount = this.productService.searchProductsByDiscount(minDiscount, maxDiscount);
			model.addAttribute("AllProduct", searchProductsByDiscount);
			return "product";
		} catch (Exception e) {
			return "404";
		}
		
	}
	
//	 Handler For product page 
//	@GetMapping("/product")
//	public String products(Model model) {
//		List<Category> allCategory = this.categoryService.getAllCategory();
//		// Loop through each category and fetch products
//		Map<Integer, List<Product>> productsByCategoryId = new HashMap<>();
//		for (Category category : allCategory) {
//			int categoryId = category.getCategoryid();
//			List<Product> productsForCategory = this.productService.getAllProductsByCategoryId(categoryId);
//			productsByCategoryId.put(categoryId, productsForCategory);
//		}
//		model.addAttribute("category", allCategory);
//		model.addAttribute("productsByCategoryId", productsByCategoryId);
//		return "product";
//	}

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
		if (p != null) {
			String email = p.getName();
			User user = userRepository.findUserByEmail(email).get();
			m.addAttribute("user", user);
		}
	}

//	 Handler For Blog page 
	@GetMapping("/blog")
	public String blog(Model model) {
//		get side-bar drop-down all category from database
		List<Category> allCategory = this.categoryService.getAllCategory();
		model.addAttribute("category", allCategory);
		return "blogList";
	}

//	 Handler For contact page 
	@GetMapping("/contact")
	public String contact(Model model) {
//		get side-bar drop-down all category from database
		List<Category> allCategory = this.categoryService.getAllCategory();
		model.addAttribute("category", allCategory);
		return "contact";
	}

//	 Handler For error page 
	@GetMapping("/404")
	public String error() {
		return "404";
	}

//	 Handler For redirect to sign-up page 
	@GetMapping("/signup")
	public String signup(Model model) {
//		get side-bar drop-down all category from database
		List<Category> allCategory = this.categoryService.getAllCategory();
		model.addAttribute("category", allCategory);
		model.addAttribute("user", new User());
		return "signup";
	}

//	Handler For user registration process 
	@PostMapping("/submitUserSignup")
	public String submitUserSignup(@ModelAttribute("user") User user, HttpSession session, Model model) {
		try {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setUserName(bCryptPasswordEncoder.encode(user.getUserName()));
			user.setAddress(bCryptPasswordEncoder.encode(user.getAddress()));
			user.setMobile(bCryptPasswordEncoder.encode(user.getMobile()));
			user.setRole("ROLE_USER");
			User u = userService.addUser(user);
			if (u != null) {
				session.setAttribute("message", new Message("User saved successfully .....", "success"));
				return "redirect:/signup";
			} else {
				session.setAttribute("message", new Message("something wrong .....", "danger"));
				return "redirect:/signup";
			}
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong .....", "danger"));
			return "redirect:/signup";
		}
	}

//	 Handler For redirect to sign-in page 
	@GetMapping("/signin")
	public String signin(Model model, HttpSession session) {
//		get side-bar drop-down all category from database
		List<Category> allCategory = this.categoryService.getAllCategory();
		model.addAttribute("category", allCategory);
//		For Empty cart items
//		GlobalData.cart.clear();
		return "signin";
	}

//	testimonial page
	@GetMapping("/testimonial")
	public String testimonial() {
		return "testimonial";
	}

}
