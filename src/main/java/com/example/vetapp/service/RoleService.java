package com.example.vetapp.service;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vetapp.exception.ConflictException;
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
		Role role = roleNameService.getRoleByName(roleName);
		user.getRoles().forEach(r -> {
			if(r.getName().equals(role.getName())) {
				throw new ConflictException("Role already added!");
			}
		});
		user.getRoles().add(role);
		return userRepository.save(user);
	}

	public void delete(String username, String role) {
		User user = findUserByUsername(username);
		for (Iterator<Role> iterator = user.getRoles().iterator(); iterator.hasNext();) {
		    if (iterator.next().getName().equals(roleNameService.getRoleByName(role).getName())) {
		        iterator.remove();
		        userRepository.save(user);
		        return;
		    }
		}
		throw new NotFoundException("Role not found!");
	}
	
	private User findUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if(user == null)
			throw new NotFoundException("User not found!");
		return user;
	}

}
