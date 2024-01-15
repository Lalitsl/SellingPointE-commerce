package com.sp.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sp.dao.CustomerRepository;
import com.sp.dao.RoleRepository;
import com.sp.entities.Customer;
import com.sp.entities.Role;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler{
	
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	RoleRepository roleRepository;
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
		String email = token.getPrincipal().getAttributes().get("email").toString();
		if(customerRepository.findCustomerByEmail(email).isPresent()) {
			Customer customer =new Customer();
			customer.setCustomerName(token.getPrincipal().getAttributes().get("given_name").toString());
			customer.setEmail(email);
			List<Role> roles=new ArrayList<>();
			roles.add(roleRepository.findById(2).get());
			customer.setRoles(roles);
			customerRepository.save(customer);
		}
		redirectStrategy.sendRedirect(request, response, "/");
		
	}
	
	

}
