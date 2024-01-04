package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sp.dao.CustomerRepository;
import com.sp.entities.customer;

@Controller
public class UserController {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	
	

//	customer registration handler 
	@PostMapping("/submitCustomerSignup")
	public String submitCustomerSignup(Model model, @ModelAttribute("customer") customer customer ) {

		customer.setCustomerName(customer.getCustomerName());
		customer.setEmail(customer.getEmail());
		customer.setMobile(customer.getMobile());
		customer.setPassword(customer.getPassword());
		customer.setAddress(customer.getAddress());
		customer save = this.customerRepository.save(customer);
		System.out.println("data saved successfully "+save);
		
		System.out.println("USER NAME : "+ customer.getCustomerName());
		System.out.println("USER EMAIL : "+ customer.getEmail());
		System.out.println("USER PASSWORD : "+ customer.getPassword());
		return "demo";
		
	}

}
