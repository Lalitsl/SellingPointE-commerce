package com.sp.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.sp.dao.UserRepository;
import com.sp.entities.User;

@Component
public class CustomUserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
//		1st way to fetch user from database 
//		User user = userRepository.findUserByEmail(username).get();
//		if(user==null) {
//			throw new UsernameNotFoundException("user not found ");
//		}else {
//			return new CustomUser(user);
//		}
		
//		2nd way to fetch data from database 
		Optional<User> user = userRepository.findUserByEmail(username);
		user.orElseThrow(()-> new UsernameNotFoundException("USER NOT FOUND !!! "));
		return user.map(CustomUser::new).get();
		
	}

}
