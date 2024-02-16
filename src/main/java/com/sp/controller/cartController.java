package com.sp.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.sp.dao.MyOrderRepository;
import com.sp.dao.UserRepository;
import com.sp.entities.CartItem;
import com.sp.entities.Category;
import com.sp.entities.MyOrder;
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
	@Autowired
	MyOrderRepository myOrderRepository;

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

//		cart calculation
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
			// Add the product to the cart
			GlobalData.cart.add(product);
			return "redirect:/cart/cartHomePage";

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
	private String checkoutItem(Model model, @RequestParam String name, @RequestParam Integer postcode,
			@RequestParam Integer quantity, @RequestParam String country, @RequestParam String email,
			@RequestParam String phone, @RequestParam String address) {
//		get side-bar drop-down all category from database
		List<Category> allCategory = this.categoryService.getAllCategory();
		model.addAttribute("category", allCategory);
		model.addAttribute("total", GlobalData.cart.stream().mapToDouble(
				product -> product.getProductPrice() - (product.getProductPrice() * (product.getDiscount() / 100)))
				.sum());
		model.addAttribute("Shipping", 30);
		model.addAttribute("otherValue", 40);

		model.addAttribute("name", name);
		model.addAttribute("postcode", postcode);
		model.addAttribute("quantity", quantity);
		model.addAttribute("country", country);
		model.addAttribute("email", email);
		model.addAttribute("phone", phone);
		model.addAttribute("address", address);
		return "/User/checkoutItem";

	}

//	payment integration for product 
	@PostMapping("/createOrder")
	@ResponseBody
	public ResponseEntity<String> createOrder(@RequestBody Map<String, Object> data, Principal principal)
			throws RazorpayException {
		try {
			System.out.println("THIS IS CREATE ORDER FUNCTION ");
			System.out.println("DATA : " + data);
			int amt = Integer.parseInt(data.get("amount").toString());
			var client = new RazorpayClient("rzp_test_SjOHuxyf14nY18", "WZzyPendOl1c1UAdfsX1umha");
			JSONObject ob = new JSONObject();
			ob.put("amount", amt * 100);
			ob.put("currency", "INR");
			ob.put("receipt", "txn_12345");
			// Creating new order
			Order order = client.orders.create(ob);
			System.out.println(order);
			System.out.println("ORDER : " + order.toString());
			// Save the order in the database
			MyOrder myOrder = new MyOrder();
			Object amountObject = order.get("amount");
			if (amountObject != null) {
				String amountString = amountObject.toString();
				double originalAmount = Double.parseDouble(amountString);
				double adjustedAmount = originalAmount / 100.0;
				myOrder.setAmount(Double.toString(adjustedAmount));
			} else {
				System.out.println("Error encountered because amount is null.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error encountered because amount is null.");
			}
			myOrder.setOrderId(order.get("id"));
			myOrder.setPaymentId(null);
			myOrder.setStatus("created");
			myOrder.setPaymentDate(LocalDate.now());
			myOrder.setUser(this.userRepository.findUserByEmail(principal.getName()).get());
			System.out.println("User ID: " + this.userRepository.findUserByEmail(principal.getName()).get());
			myOrder.setReceipt(order.get("receipt"));
			MyOrder savedOrder = this.myOrderRepository.save(myOrder);
			System.out.println("Order saved: " + savedOrder);

			return ResponseEntity.ok(order.toString());
		} catch (Exception e) {
			System.out.println("Error creating order: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error creating order: " + e.getMessage());
		}
	}

	@PostMapping("/updateOrder")
	public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data) {
		try {
			MyOrder updateMyOrder = this.myOrderRepository.findByOrderId(data.get("order_id").toString());

			String paymentId = data.get("payment_id").toString();
			String orderId = data.get("order_id").toString();
			String status = data.get("status").toString();
			int quantity = Integer.parseInt(data.get("quantity").toString()); // Retrieve the quantity from the request
			System.out.println("PAYMENT Id : " + paymentId);
			System.out.println("ORDER ID : " + orderId);
			System.out.println("STATUS : " + status);
			System.out.println("====> QUANTITY from DATABASE: " + quantity);
// ====================================
			// Check if the product is in stock
//			if (product.getQuantity() > 0) {
//				// Update the product quantity and stock
//				int updatedStock = product.getQuantity() - 1;
//				product.setQuantity(updatedStock);
//				// Save the updated product to the database
//				productService.addProduct(product);
//
//				// Redirect to the cart page
//				return "redirect:/cart/cartHomePage";
//			} else {
//				
//				return "404"; // Adjust the URL accordingly
//			}
// ====================================
			updateMyOrder.setPaymentId(data.get("payment_id").toString());
			updateMyOrder.setStatus(data.get("status").toString());
			updateMyOrder.setPaymentDate(LocalDate.now());
			this.myOrderRepository.save(updateMyOrder);

			System.out.println("Data: " + data);
			return ResponseEntity.ok(Map.of("msg", "payment status updated ."));
		} catch (Exception e) {
			System.out.println("Error updating order: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating order: " + e.getMessage());
		}
	}

//	======= product Liked module ==============

	@GetMapping("/productLikePage")
	public String productLikeHomePage(Principal p, Model model) {
//		get logged in user
		String email = p.getName();
		User user = userRepository.findUserByEmail(email).get();
		model.addAttribute("user", user);
//		get side-bar drop-down all category from database
		List<Category> allCategory = this.categoryService.getAllCategory();
		model.addAttribute("category", allCategory);
		model.addAttribute("cartCount", GlobalData.liked.size());
		model.addAttribute("liked", GlobalData.liked);
		return "/User/productLikePage";
	}

	@GetMapping("/productLikePage/{id}")
	public String productLikePage(@PathVariable int id, Model model) {
		Optional<Product> productOptional = productService.getProductById(id);
		if (productOptional.isPresent()) {
			Product product = productOptional.get();
			GlobalData.liked.add(product);
			model.addAttribute("liked", GlobalData.liked);
			return "redirect:/product";
		} else {
			return "404";
		}
	}

//	Remove product from Liked
	@GetMapping("/removeLiked/{index}")
	private String removeLiked(@PathVariable int index) {
		GlobalData.liked.remove(index);
		return "redirect:/cart/productLikePage";
	}

}
