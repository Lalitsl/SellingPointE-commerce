package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sp.dao.CustomerRepository;
import com.sp.entities.Customer;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private CustomerRepository customerRepository;

//	customer registration handler 
	@PostMapping("/submitCustomerSignup")
	public String submitCustomerSignup(Model model, @ModelAttribute("customer") Customer customer ) {

		customer.setCustomerName(customer.getCustomerName());
		customer.setEmail(customer.getEmail());
		customer.setMobile(customer.getMobile());
		customer.setPassword(customer.getPassword());
		customer.setAddress(customer.getAddress());
		customer.setType("user");
		Customer save = this.customerRepository.save(customer);
		System.out.println("data saved successfully "+save);
		return "signup";
	}

}
