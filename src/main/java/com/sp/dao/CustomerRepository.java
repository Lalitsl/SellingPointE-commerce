package com.sp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sp.entities.customer;


public interface CustomerRepository extends JpaRepository<customer, Integer> {

}
