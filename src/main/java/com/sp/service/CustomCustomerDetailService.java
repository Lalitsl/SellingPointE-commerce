package com.sp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sp.config.CustomCustomerDetail;
import com.sp.dao.CustomerRepository;
import com.sp.entities.Customer;

@Service
public class CustomCustomerDetailService implements UserDetailsService {
	
	@Autowired
	CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		fetching User-name from database 
		Optional<Customer> customer = customerRepository.findCustomerByEmail(username);
		customer.orElseThrow(()-> new UsernameNotFoundException("USER NOT FOUND !!! "));
		return customer.map(CustomCustomerDetail::new).get();
		
	}
	
	
}
