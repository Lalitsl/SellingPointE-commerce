package com.sp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sp.entities.MyOrder;
import java.util.List;


public interface MyOrderRepository extends JpaRepository<MyOrder, Integer> {
	
	public MyOrder findByOrderId(String orderId);
	
}
