package com.example.vetapp.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vetapp.exception.NotFoundException;
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
	
	RoleNameService roleNameService;
	
	@Autowired
	public void setRoleNameService(RoleNameService roleNameService) {
		this.roleNameService = roleNameService;
	}

	public User save(String username, String roleName) {
		User user = findUserByUsername(username);
		Set<Role> roles = user.getRoles();
        roles.add(roleNameService.getRoleByName(roleName));
        user.setRoles(roles);
		return userRepository.save(user);
	}

	public List<Role> get(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(String username, String roleId) {
		// TODO Auto-generated method stub
		
	}
	
	private User findUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if(user == null)
			throw new NotFoundException("User not found!");
		return user;
	}

}
