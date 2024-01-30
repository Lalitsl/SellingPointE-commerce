package com.sp.service;

import org.springframework.stereotype.Service;

import com.sp.model.EmailSender;

@Service
public class EmailService {
	
	  public void sendEmail(String toEmail, String subject, String body, String attachmentPath) {
	        // Logic to send email with attachment
	        EmailSender.sendEmail(toEmail, subject, body, attachmentPath);
	    }
}
