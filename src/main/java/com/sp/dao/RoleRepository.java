package com.sp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sp.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

}