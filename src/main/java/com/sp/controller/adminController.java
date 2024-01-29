package com.sp.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sp.dao.UserRepository;
import com.sp.dto.ProductDTO;
import com.sp.entities.Category;
import com.sp.entities.Product;
import com.sp.entities.User;
import com.sp.helper.Message;
import com.sp.service.CategoryService;
import com.sp.service.ProductService;
import com.sp.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
//@EnableScheduling
public class adminController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserService userService;

//	image directory
	public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/upload";

//	Admin home handler
	@GetMapping("")
	public String admin(Model model) {
		model.addAttribute("title", "sp admin home page");
		long TotalUsers = userService.countTotalUsers();
		model.addAttribute("TotalUsers", TotalUsers);
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
	@GetMapping("/productHome/{page}")
	public String productHome(@PathVariable("page") Integer page, Model model) {
		model.addAttribute("title", "Product home page ");
//		pagination code 
		Pageable pageable = PageRequest.of(page, 3);
		Page<Product> allProduct = productService.getAllProduct(pageable);
		model.addAttribute("products", allProduct);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPage", allProduct.getTotalPages());
		return "admin/productHome";

	}

//	redirect request to add product page
	@GetMapping("/sendAddProductPage")
	public String sendProductPage(Model model) {
		model.addAttribute("productDto", new ProductDTO());
		model.addAttribute("category", categoryService.getAllCategory());
		return "admin/productAdd";
	}

//	add product handler

	/*
	 * @PostMapping("/addProduct") public String
	 * addProduct(@ModelAttribute("productDto") ProductDTO productDto,
	 * 
	 * @RequestParam("proImage") String proImage, HttpSession session, Model model)
	 * throws IOException { // @RequestParam("productImage") MultipartFile file, try
	 * { Product product = new Product();
	 * product.setProductName(productDto.getProductName());
	 * product.setProductDescription(productDto.getProductDescription());
	 * product.setProductId(productDto.getProductId());
	 * product.setProductPrice(productDto.getProductPrice());
	 * product.setStock(productDto.getStock());
	 * product.setCategory(categoryService.getCategoryById(productDto.getCategoryid(
	 * )).get()); String imageUUID;
	 * 
	 * if (!productDto.getProductImage().isEmpty()) { MultipartFile file =
	 * productDto.getProductImage(); imageUUID = file.getOriginalFilename(); Path
	 * FileNameAndPath = Paths.get(uploadDir, imageUUID);
	 * Files.write(FileNameAndPath, file.getBytes()); } else { imageUUID = proImage;
	 * System.out.println("else block for image upload"); }
	 * product.setProductImage(imageUUID); productService.addProduct(product);
	 * session.setAttribute("message", new
	 * Message("Product saved successfully .....", "success")); Timer timer = new
	 * Timer(); timer.schedule(new TimerTask() {
	 * 
	 * @Override public void run() { session.removeAttribute("message");
	 * timer.cancel(); // Cancel the timer after removing the message } }, 3000);
	 * return "redirect:/admin/productHome";
	 * 
	 * } catch (Exception e) { System.out.println("something is wrong .........");
	 * return "404"; }
	 * 
	 * }
	 * 
	 * // if(!file.isEmpty()) { // imageUUID=file.getOriginalFilename(); // Path
	 * FileNameAndPath = Paths.get(uploadDir,imageUUID); //
	 * Files.write(FileNameAndPath, file.getBytes()); //
	 * System.out.println("if block "); //}else { // imageUUID=proImage; //
	 * System.out.println("else block "); //}
	 * 
	 */

	@PostMapping("/addProduct")
	public String processContactForm(@ModelAttribute ProductDTO productDTO,
			@RequestParam("productImage") MultipartFile file, Principal principal, HttpSession session)
			throws IOException {
		try {
			Product product = new Product();
			product.setProductName(productDTO.getProductName());
			product.setProductDescription(productDTO.getProductDescription());
			product.setProductCompanyName(productDTO.getProductCompanyName());
			product.setProductId(productDTO.getProductId());
			product.setProductPrice(productDTO.getProductPrice());
			product.setStock(productDTO.getStock());
			int cateID = Integer.parseInt(productDTO.getCategoryid());
			product.setCategory(categoryService.getCategoryById(cateID).get());
			// String imageUUID;
			if (!productDTO.getProductImage().isEmpty()) {
				product.setProductImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/images/upload/").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("image is uploaded successfully ");
			} else {
				// If the file field is empty, set the default image
				product.setProductImage("final_logo1.png");
				// You also need to copy the default image to the destination folder
				File saveFile = new ClassPathResource("static/images/upload/").getFile();
				Path defaultImagePath = Paths.get(saveFile.getAbsolutePath() + File.separator + "final_logo1.png");
				Files.copy(new ClassPathResource("static/images/final_logo1.png").getInputStream(), defaultImagePath,
						StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Default image set");
				System.out.println("IMAGE IS EMPTY ");
			}
			productService.addProduct(product);
			session.setAttribute("message", new Message("Product saved successfully .....", "success"));
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					session.removeAttribute("message");
					timer.cancel(); // Cancel the timer after removing the message
				}
			}, 3000);
			return "redirect:/admin/productHome/0";
		} catch (Exception e) {
			return "404";
		}
	}

//	delete product handler
	@GetMapping("/deleteProduct/{productid}")
	public String deleteProduct(@PathVariable("productid") int productid, HttpSession session, Model model) {
		try {
			this.productService.deleteProductById(productid);
			session.setAttribute("message", new Message("Product deleted successfully", "success"));
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					session.removeAttribute("message");
					timer.cancel(); // Cancel the timer after removing the message
				}
			}, 3000);
			return "redirect:/admin/productHome/0";
		} catch (Exception e) {
			System.out.println("something went wrong in delete handler");
		}
		return "404";
	}

//	redirect to update Product form page 
	@GetMapping("/sendUpdateProductPage/{id}")
	public String sendUpdateProductPage(@PathVariable int id, Model model) {
		Product product = this.productService.getProductById(id).get();
		model.addAttribute("category", categoryService.getAllCategory());
		model.addAttribute("product", product);
		return "admin/productUpdate";
	}

//	update product process handler
	@PostMapping("/updateProduct")
	public String updateproduct(@ModelAttribute("product") ProductDTO productDto,
			@RequestParam("productImage") MultipartFile file, Model model, HttpSession session) throws IOException {
		// Retrieve the existing product
		Product existingProduct = this.productService.getProductById(productDto.getProductId()).get();
		existingProduct.setProductName(productDto.getProductName());
		existingProduct.setProductDescription(productDto.getProductDescription());
		existingProduct.setProductCompanyName(productDto.getProductCompanyName());
		existingProduct.setProductPrice(productDto.getProductPrice());
		existingProduct.setStock(productDto.getStock());
		int cateID = Integer.parseInt(productDto.getCategoryid());
		existingProduct.setCategory(categoryService.getCategoryById(cateID).get());
		if (!file.isEmpty()) {
			// Update the product image
			existingProduct.setProductImage(file.getOriginalFilename());
			File saveFile = new ClassPathResource("static/images/upload").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Image is uploaded successfully");
		} else {
			// If no new file is provided, retain the existing image
			System.out.println("No new image provided, using the existing one");
		}

		productService.addProduct(existingProduct);
		session.setAttribute("message", new Message("Product updated successfully", "success"));
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				session.removeAttribute("message");
				timer.cancel(); // Cancel the timer after removing the message
			}
		}, 3000);
		return "redirect:/admin/productHome/0";
	}

//	delete all product
	@GetMapping("/deleteAllProduct")
	public String deleteAllProduct(HttpSession session) {
		try {
			this.productService.deleteAllProduct();
			session.setAttribute("message", new Message("All Product Deleted Successfully ", "success"));
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					session.removeAttribute("message");
					timer.cancel(); // Cancel the timer after removing the message
				}
			}, 3000);
			return "redirect:/admin/productHome/0";
		} catch (Exception e) {
			return "404";
		}
	}

//	================= product module end here ========================

//	================= Ad-min profile start here ========================

	@GetMapping("/adminProfile")
	public String adminProfile(Principal p, Model m) {
		String email = p.getName();
		User user = userRepository.findUserByEmail(email).get();
		m.addAttribute("user", user);
		return "admin/adminProfile";
	}

//	update ad-min profile 
	@GetMapping("/sendUpdateAdminProfile/{id}")
	public String sendUpdateAdminProfile(@PathVariable("id") int id, Model model, HttpSession session) {
		Optional<User> updateUserById = this.userRepository.findById(id);
		if (updateUserById.isPresent()) {
			model.addAttribute("user", updateUserById.get());
			return "/signup";
		} else {
			return "404";
		}

	}

//	================= Ad-min profile end here ========================

	
}
