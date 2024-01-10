package com.sp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sp.dao.CategoryRepository;
import com.sp.entities.Category;
import com.sp.entities.Product;
import com.sp.helper.Message;
import com.sp.service.CategoryService;
import com.sp.service.ProductService;
import org.springframework.util.StringUtils;


import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
//@EnableScheduling
public class adminController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

//	image directory
	public String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/upload";

	
	
//	Admin home handler
	@GetMapping("")
	public String admin(Model model) {
		model.addAttribute("title", "sp admin home page");
		return "admin/home";
	}

//	================= category module start here ========================
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

//	delete particular category handler
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

//	Delete all category
	@GetMapping("/deleteAllCate")
	public String deleteAllCategory(HttpSession session) {
		try {
			categoryService.deleteAllCategory();
			System.out.println("all category deleteed ,............");
			session.setAttribute("message", new Message("All category deleted .....", "success"));
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

//	================= category module end here ========================

//	================= product module start here ========================

//	product home page handler 
	@GetMapping("/productHome")
	public String productHome(Model model) {
		model.addAttribute("title", "welcome");
		model.addAttribute("products", productService.getAllProduct());
		return "admin/productHome";
	}

//	redirect request to add product page
	@GetMapping("/sendAddProductPage")
	public String sendProductPage(Model model) {
		model.addAttribute("pro", new Product());
		model.addAttribute("category", categoryService.getAllCategory());
		return "admin/productAdd";
	}

//	add product handler
	@PostMapping("/addProduct")
	public String addProduct(Product pro, 
			@RequestParam("productImage") MultipartFile file, 
			@RequestParam("proImage") String proImage) throws IOException {
		
		System.out.println(pro.getProductName());
		System.out.println(pro.getProductPrice());
		System.out.println(pro.getProductImage());
		Product product=new Product();
		product.setProductName(pro.getProductName());
		product.setProductDescription(pro.getProductDescription());
		product.setProductId(pro.getProductId());
		product.setProductPrice(pro.getProductPrice());
		product.setStock(pro.getStock());
		product.setCategory(categoryService.getCategoryById(pro.getCategory().getCategoryid()).get());
		
		String imageUUID;
		if(!file.isEmpty()) {
			imageUUID=file.getOriginalFilename();
			Path FileNameAndPath = Paths.get(uploadDir,imageUUID);
			Files.write(FileNameAndPath, file.getBytes());
		}else {
			imageUUID=proImage;
		}
		product.setProductImage(imageUUID);
		productService.addProduct(product);
		return "redirect:/admin/productHome";
		
		
		
	}
	
	
	
//	================= product module start here ========================
	
	
	
	
	

}
