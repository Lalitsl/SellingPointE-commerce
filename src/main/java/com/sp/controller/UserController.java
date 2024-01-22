package com.sp.controller;


import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sp.dao.UserRepository;
import com.sp.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	
	@GetMapping("/cart")
	public String cart() {
		return "cart";
	}
	
	@GetMapping("/showUser")
	public String showUser(Principal p , Model m) {
		String email = p.getName();
		User user = userRepository.findUserByEmail(email).get();
		m.addAttribute("user", user);
		System.out.println("EMAIL : "+user.getEmail());
		System.out.println("USERNAME : "+user.getUserName());	
		return "product";
		
	}
	


}
