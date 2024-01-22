package com.sp.entities;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userId;
	@Column(nullable = false)
	@NotEmpty
	private String userName;
	@Column(nullable = false,unique = true)
	@NotEmpty
	@Email(message = "{errors.invalid_email}")
	private String email;
	private String address;
	private String mobile;
	private String password;
	
	private String role;
	
//	@ManyToOne
//	private Role roles;
	
//	public User(User user) {
//		this.userId = user.getUserId();
//		this.userName=user.getUserName();
//		this.email = user.getEmail();
//		this.address = user.getAddress();
//		this.mobile = user.getMobile();
//		this.roles = user.getRoles();
//		this.password = user.getPassword();
//	}

	public User() {
		super();
	}
	

}


