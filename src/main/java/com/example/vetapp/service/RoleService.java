package com.example.vetapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vetapp.model.Role;
import com.example.vetapp.model.User;
import com.example.vetapp.repository.RoleRepository;

@Service
public class RoleService {
	
	RoleRepository roleRepository;
	
	@Autowired
	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public User save(Long userId, String roleName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Role> get(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(Long userId, String roleId) {
		// TODO Auto-generated method stub
		
	}

}
