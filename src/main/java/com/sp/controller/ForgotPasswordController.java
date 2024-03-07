package com.sp.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sp.dao.UserRepository;
import com.sp.entities.User;
import com.sp.helper.Message;
import com.sp.model.MailStructure;
import com.sp.service.MailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotPasswordController {

	Random random = new Random(1000);

	@Autowired
	private MailService mailService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping("/forgot")
	public String openEmailForm() {
		return "forgotEmailForm";
	}

//	OTP send handler
	@PostMapping("/otpSend")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {
		System.out.println("Email : " + email);
//		generate OTP of 4 digit 
		int otp = random.nextInt(9999);
		System.out.println("otp : " + otp);
		// Capture the current time when the OTP is generated
		long currentTime = System.currentTimeMillis();
		// Check if the user with the given email exists
		User userCheck = userRepository.findUserByEmail(email).orElse(null);
		if (userCheck != null) {
			try {
				MailStructure mailStructure = new MailStructure();
				mailStructure.setSubject("Verify OTP for Password Reset of selling point project ");
				mailStructure.setMessage(
						" Please check your email and verify OTP to continue forgot password process . Your OTP is: "
								+ otp + " Thank-you Team Selling Point E-commerce project.");
				this.mailService.sendMail(email, mailStructure);
				session.setAttribute("myotp", otp);
				session.setAttribute("email", email);
				session.setAttribute("otpTime", currentTime);
				return "verifyOTP";
			} catch (Exception e) {
				System.out.println("Something went wrong: " + e.getMessage());
				return "redirect:/forgot";
			}
		} else {
			session.setAttribute("message", "Email doesn't exist. Email not matched in the database");
			return "redirect:/forgot";
		}

	}

	
	@PostMapping("/verifyOtp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session) {
		int myOtp = (int) session.getAttribute("myotp");
		String Email = (String) session.getAttribute("email");
		long otpTime = (long) session.getAttribute("otpTime");
		System.out.println("MYOTP : " + myOtp);
		System.out.println("User OTP : " + otp);
		System.out.println("OTP TIMES : " + otpTime);

		// Check if OTP has expired (more than 1 minute)
		if (System.currentTimeMillis() - otpTime > 30 * 1000) {
			session.setAttribute("message", "OTP has expired. Please resend OTP. ");
			return "forgotEmailForm"; // Or any other appropriate view
		}

		System.out.println("MYOTP : " + myOtp);
		System.out.println("User OTP : " + otp);

		if (myOtp == otp) {
//			change password code
			User user = userRepository.findUserByEmail(Email).get();
			System.out.println("USER VALUE IS : " + user);
			if (user == null) {
//				error message 
				session.setAttribute("message", "Email does't exist . Email not matched in database ");
				return "forgotEmailForm";
			} else {
//				send change password form 
			}
			System.out.println("OTP MATCHED ........");
			return "changePasswordForm";

		} else {
			session.setAttribute("message", new Message("Invalid OTP. Please Try Again...", "danger"));
			System.out.println("OTP INVALID ........");
			return "verifyOTP";
		}

	}

//	password change controller 

	@PostMapping("/changePassword")
	public String changePass(@RequestParam("newPassword") String newPassword, HttpSession session) {
		String email = (String) session.getAttribute("email");
		User user = this.userRepository.findUserByEmail(email).get();
		user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
		this.userRepository.save(user);
		return "redirect:/signin?change=password changged successfully .....";
	}

}
