package com.sp.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.sp.service.CustomCustomerDetailService;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;
	@Autowired
	CustomCustomerDetailService customCustomerDetailService;

	@Bean
	public UserDetailsService getUserDetailsService() {
		return new CustomCustomerDetailService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeRequests()
	        	.requestMatchers("/admin/**").hasRole("ADMIN")
            	.requestMatchers("/user/**").hasRole("USER")
	            .requestMatchers("/**").permitAll()
	            .anyRequest().authenticated()
	            .and()
	        .formLogin()
	            .loginPage("/signin")
	            .permitAll()
	            .failureUrl("/signin?error=true")
	            .defaultSuccessUrl("/")
	            .usernameParameter("email")
	            .passwordParameter("password")
	            .and()
	        .oauth2Login()
	            .loginPage("/signin")
	            .successHandler(googleOAuth2SuccessHandler)
	            .and()
	        .logout()
	            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	            .logoutSuccessUrl("/signin")
	            .invalidateHttpSession(true)
	            .deleteCookies("JSESSIONID")
	            .and()
	        .exceptionHandling()
	            .and()
	        .csrf()
	            .disable();

	    http.authenticationProvider(authenticationProvider());

	    return http.build();
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    auth.userDetailsService(customCustomerDetailService)
	        .passwordEncoder(passwordEncoder());
//		auth.authenticationProvider(authenticationProvider());
	}
	
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().requestMatchers("/resources/**", "/static/**", "/images/**", "/images/product/**", "/css/**", "/js/**", "/fonts/**");
	}
	

  
    
   


}
