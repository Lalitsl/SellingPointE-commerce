package com.sp.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sp.dao.UserRepository;
import com.sp.entities.CartItem;
import com.sp.entities.Category;
import com.sp.entities.Product;
import com.sp.entities.User;
import com.sp.global.GlobalData;
import com.sp.service.CategoryService;
import com.sp.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class cartController {

	@Autowired
	ProductService productService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CategoryService categoryService;

//	Handler for user cart 
	@GetMapping("/cartHomePage")
	public String cartHomePage(Principal p, Model m) {
//		get logged in user
		String email = p.getName();
		User user = userRepository.findUserByEmail(email).get();
		m.addAttribute("user", user);
//		get side-bar drop-down all category from database
		List<Category> allCategory = this.categoryService.getAllCategory();
		m.addAttribute("category", allCategory);

//		cart calculationSSS
		if (GlobalData.cart.isEmpty()) {
// 		Cart is empty, set a message
			m.addAttribute("cartEmptyMessage", "Cart is empty. Please add items to the cart.");
			m.addAttribute("Shipping", 0);
			m.addAttribute("otherValue", 0);
			m.addAttribute("total", 0);

		} else {
			m.addAttribute("cartCount", GlobalData.cart.size());
			m.addAttribute("total", GlobalData.cart.stream().mapToDouble(
					product -> product.getProductPrice() - (product.getProductPrice() * (product.getDiscount() / 100)))
					.sum());
			m.addAttribute("cart", GlobalData.cart);
			// generate random values for shipping and other fees
//	        Random random = new Random();
//	        int shippingValue = random.nextInt(41) + 10; // Generates a random integer between 10 and 50 (inclusive)
//	        int otherValue = random.nextInt(41) + 20;
			m.addAttribute("Shipping", 30);
			m.addAttribute("otherValue", 40);
			m.addAttribute("setProductQuantity", 1);
		}
		return "/User/cartHomePage";
	}

	@GetMapping("/addToCart/{id}")
	private String addToCart(@PathVariable int id, Model model) {
		Optional<Product> productOptional = productService.getProductById(id);

		if (productOptional.isPresent()) {
			Product product = productOptional.get();

			// Check if the product is in stock
			if (product.getQuantity() > 0) {
				// Update the product quantity and stock
				int updatedStock = product.getQuantity() - 1;
				product.setQuantity(updatedStock);

				// Save the updated product to the database
				productService.addProduct(product);
				// Add the product to the cart
				System.out.println("GetDISCOUNT :::: " + productOptional.get().getDiscount());
				GlobalData.cart.add(product);

				// Redirect to the cart page
				return "redirect:/cart/cartHomePage";
			} else {
				// Product is out of stock, handle accordingly (e.g., show a message)
				// You may redirect to an error page or the product page with a message
				return "404"; // Adjust the URL accordingly
			}
		} else {
			// Product not found, handle accordingly (e.g., show a message)
			// You may redirect to an error page or the home page with a message
			return "404"; // Adjust the URL accordingly
		}
	}

	@PostMapping("/updateQuantity1")
	public String updateQuantity1(@ModelAttribute("cartItem") CartItem cartItem) {
		// Retrieve the product from the database
		Optional<Product> productOptional = productService.getProductById(cartItem.getProductId());
		if (productOptional.isPresent()) {
			Product product = productOptional.get();
			// Update the product quantity
			product.setQuantity(cartItem.getProductQuantity());
			// Save the updated product to the database
			productService.addProduct(product);
			// Redirect to the cart page
			return "redirect:/cart/cartHomePage";
		} else {
			// Product not found so redirect to an error page
			return "404";
		}
	}

	@GetMapping("/updateQuantity")
	@ResponseBody
	public String updateQuantity(@RequestParam("newQuantity") int newQuantity, Model model) {
		// Update the setProductQuantity attribute in the model
		// You may also want to update the quantity in the database or cart
		model.addAttribute("setProductQuantity", newQuantity);
		// Return a response if needed
		return "Quantity updated successfully";
	}

//	Remove product from cart 
	@GetMapping("/removeItem/{index}")
	private String removeItem(@PathVariable int index) {
		GlobalData.cart.remove(index);
		return "redirect:/cart/cartHomePage";
	}

//	product item checkout 
	@GetMapping("/checkoutItem")
	private String checkoutItem(Model model) {
//		get side-bar drop-down all category from database
		List<Category> allCategory = this.categoryService.getAllCategory();
		model.addAttribute("category", allCategory);
		model.addAttribute("total", GlobalData.cart.stream().mapToDouble(
				product -> product.getProductPrice() - (product.getProductPrice() * (product.getDiscount() / 100)))
				.sum());
		model.addAttribute("Shipping", 30);
		model.addAttribute("otherValue", 40);
		return "/User/checkoutItem";

	}

//	product payment integration 
	@PostMapping("/createOrder")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {
		System.out.println("THIS IS CREATE ORDER FUNCTION ");
		System.out.println("DATA : " + data);
		int amt = Integer.parseInt(data.get("amount").toString());
		var client = new RazorpayClient("rzp_test_SjOHuxyf14nY18", "WZzyPendOl1c1UAdfsX1umha");
		JSONObject ob = new JSONObject();
		ob.put("amount", amt * 100);
		ob.put("currency", "INR");
		ob.put("receipt", "txn_12345");
//		 creating new order
		Order order = client.orders.create(ob);
		System.out.println(order);
		System.out.println("ORDER : " + order.toString());
		return order.toString();
	}
	
//	product Rating module 
	@GetMapping("/productRating")
	public String productRating() {
		return "404";
		
	}
	
	

}
