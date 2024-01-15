package com.sp.entities;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer roleId;
	
	@Column(nullable = false, unique = true)
	@NotEmpty
	private String roleName;
	
	@ManyToMany(mappedBy = "roles")
	private List<Customer> customers;
	

}
