package com.sp.pdf;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



public class EmailSender {
	
	 public static void sendEmail(String toEmail, String subject, String body, String attachmentPath) {
	        Properties props = new Properties();
	        // Set mail server properties (configure based on your email provider)
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");

	        // Set sender's email and password
	        final String senderEmail = "lalitnarware2001@gmail.com";
	        final String password = "hxfbxywjhfjeczci";

	        // Create a session with authentication
	        Session session = Session.getInstance(props, new Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(senderEmail, password);
	            }
	        });

	        try {
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(senderEmail));
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
	            message.setSubject(subject);

	            // Create message body
	            MimeBodyPart messageBodyPart = new MimeBodyPart();
	            messageBodyPart.setText(body);

	            // Attach the PDF file
	            MimeBodyPart attachmentPart = new MimeBodyPart();
	            attachmentPart.attachFile(attachmentPath);

	            // Create Multipart
	            Multipart multipart = new MimeMultipart();
	            multipart.addBodyPart(messageBodyPart);
	            multipart.addBodyPart(attachmentPart);

	            // Set multipart content to the message
	            message.setContent(multipart);

	            // Send the message
	            Transport.send(message);

	            System.out.println("Email sent successfully...");

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	
}
