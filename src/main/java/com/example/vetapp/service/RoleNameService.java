package com.example.vetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vetapp.exception.NotFoundException;
import com.example.vetapp.model.Role;
import com.example.vetapp.model.RoleName;
import com.example.vetapp.repository.RoleRepository;

@Service
public class RoleNameService {
	
	RoleRepository roleRepository;
	
	@Autowired
	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	public Role getRoleByName(String roleName) {
		switch(roleName) {
	        case "admin":
	          return roleRepository.findByName(RoleName.ROLE_ADMIN)
	                .orElseThrow(() -> new NotFoundException("Role admin not found!"));
	        case "doctor":
	              return roleRepository.findByName(RoleName.ROLE_DOCTOR)
	                .orElseThrow(() -> new NotFoundException("Role doctor not found!"));
	        case "user":
	              return roleRepository.findByName(RoleName.ROLE_USER)
	                .orElseThrow(() -> new NotFoundException("Role user not found!"));
	        default:
	            throw new NotFoundException("Role not found!");            
		}
	}
}
