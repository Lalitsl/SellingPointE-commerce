package com.sp.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sp.entities.User;


public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findUserByEmail(String email);
	User findUserByUserName(String userName);
	
}
