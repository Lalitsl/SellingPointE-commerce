package com.sp.helper;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionCleanup {
	   private final HttpSession session;

	    @Autowired
	    public SessionCleanup(HttpSession session) {
	        this.session = session;
	    }

	    // @Scheduled(fixedDelay = 1000) // Check every second for expired messages
	    public void cleanUpSession() {
	        if (session.getAttribute("message") != null) {
	            Date messageTime = (Date) session.getAttribute("messageTime");
	            if (messageTime != null && messageTime.before(new Date())) {
	                session.removeAttribute("message");
	                session.removeAttribute("messageTime");
	            }
	        }
	    }
 
}

