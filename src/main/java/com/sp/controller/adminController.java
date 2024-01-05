package com.sp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class adminController {

//	admin home handler
	@GetMapping("/admin")
	public String admin() {
		return "admin/home";
	}
	
//	admin category handler
	@GetMapping("/category")
	public String categoryHome() {
		return "admin/categoryHome";
	}
	
}
