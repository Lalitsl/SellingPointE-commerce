package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sp.dao.CustomerRepository;
import com.sp.dao.RoleRepository;
import com.sp.entities.Customer;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@GetMapping("/cart")
	public String cart() {
		return "cart";
	}

}
