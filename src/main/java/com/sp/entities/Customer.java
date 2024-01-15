package com.sp.entities;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int customerId;
	@Column(nullable = false)
	@NotEmpty
	private String customerName;
	@Column(nullable = false,unique = true)
	@NotEmpty
	@Email(message = "{errors.invalid_email}")
	private String email;
	private String address;
	private String mobile;
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
			name="user_role",
			joinColumns ={@JoinColumn(name="CUSTOMER_ID", referencedColumnName="customerId")},
        	inverseJoinColumns= {@JoinColumn(name="ROLE_ID", referencedColumnName="roleId")}
  
			)
	private List<Role> roles;
	
	private String password;

	public Customer(Customer customer) {
		this.customerId = customer.getCustomerId();
		this.customerName = customer.getCustomerName();
		this.email = customer.getEmail();
		this.address = customer.getAddress();
		this.mobile = customer.getMobile();
		this.roles = customer.getRoles();
		this.password = customer.getPassword();
	}

	public Customer() {
		super();
	}
	

	

	
	
	
	
	

}
