package com.sp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	
	@GetMapping("/product")
	public String products() {
		return "product";
	}
	
	@GetMapping("/blog")
	public String blog() {
		return "blogList";
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}
	
	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}
	
	@GetMapping("/signin")
	public String signin() {
		return "signin";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin/home";
	}
	
	
	
	

}
