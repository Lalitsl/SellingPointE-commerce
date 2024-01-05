package com.sp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sp.entities.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
