package com.example.vetapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vetapp.model.Role;
import com.example.vetapp.model.User;
import com.example.vetapp.repository.UserRepository;

@Service
public class RoleService {
	
	UserRepository userRepository;
	
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User save(String username, String roleName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Role> get(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(String username, String roleId) {
		// TODO Auto-generated method stub
		
	}

}
