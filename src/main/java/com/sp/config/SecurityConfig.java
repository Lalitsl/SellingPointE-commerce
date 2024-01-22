package com.sp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	CustomAuthSuccessHandler authSuccessHandler;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService getDetailsService() {
		return new CustomUserDetailService();
	}
   
	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
		
	}
	

	 @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			http.csrf(csrf -> csrf.disable());
			http
		    .authorizeHttpRequests(
		        (requests) -> requests
		            .requestMatchers("/admin/**").hasRole("ADMIN")
	            	.requestMatchers("/user/**").hasRole("USER")
		            .requestMatchers("/**").permitAll()
		            .anyRequest().authenticated()
		    )
		    .formLogin(
		        (formLogin) -> formLogin
		        .loginPage("/signin").loginProcessingUrl("/userlogin")
	    		.successHandler(authSuccessHandler).permitAll()
	    		.failureUrl("/signin?error=true")
		        .defaultSuccessUrl("/user/showUser")
		    )
		    .logout(
		        (logout) -> logout
		            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		            .invalidateHttpSession(true)
		            .deleteCookies("JSESSIONID")
		    );
		    
			http.authenticationProvider(getAuthenticationProvider());
			return http.build();
		}
	 
	 

	


}
