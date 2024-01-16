package com.sp.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sp.entities.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	Optional<Customer> findCustomerByEmail(String email);
	

	
}
