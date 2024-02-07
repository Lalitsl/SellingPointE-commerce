package com.sp.controller;


import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.sp.helper.Message;
import com.sp.dao.UserRepository;
import com.sp.entities.Category;
import com.sp.entities.User;
import com.sp.service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	CategoryService categoryService;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	

	
//	Handler for redirect to base after user login
	@GetMapping("/showUser")
	public String showUser(Principal p , Model m) {
		String email = p.getName();
		User user = userRepository.findUserByEmail(email).get();
		m.addAttribute("user", user);
		return "redirect:/product";
	}
	
//	Handler for redirect to user password change form  
	@GetMapping("/changeUserPassword")
	public String changeUserPassword(Principal p , Model m) {
		try {
//			get side-bar drop-down all category from database
			List<Category> allCategory = this.categoryService.getAllCategory();
			m.addAttribute("category", allCategory);
			String email = p.getName();
			User user = userRepository.findUserByEmail(email).get();
			m.addAttribute("user", user);
			return "/User/changeUserPassword";
		} catch (Exception e) {
			return "404";
		}
	}
	
//	user password change process 
	@PostMapping("/changeUserPass")
	public String changeUserPass(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {

		try {
			String email = principal.getName();
			User currentUser = this.userRepository.findUserByEmail(email).get();
			if(this.bCryptPasswordEncoder.matches(oldPassword,currentUser.getPassword())) {
				if(newPassword.equals(oldPassword)) {
					session.setAttribute("message", new Message("You can't keep old Password and new password are same", "danger"));
					return "redirect:/user/changeUserPassword";
				}else {
					currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
					this.userRepository.save(currentUser);
					session.setAttribute("message", new Message("Password changed successfully", "success"));
					return "redirect:/logout";
				}
				
			} else {
				session.setAttribute("message", new Message("Your Old Password Invalid !!! ", "danger"));
				return "redirect:/user/changeUserPassword";
			}
		} catch (Exception e) {
			return "404";
		}
	}
	


}
