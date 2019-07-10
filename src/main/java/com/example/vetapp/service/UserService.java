package com.example.vetapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.example.vetapp.exception.NotFoundException;
import com.example.vetapp.exception.PermissionDeniedException;
import com.example.vetapp.model.User;
import com.example.vetapp.repository.UserRepository;

@Service
public class UserService {
	
	UserRepository userRepository;
	
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public boolean existByUserId(Long id){
		return userRepository.existsById(id);
	}

	public User update(Authentication authentication, String username, String name, String phone, String address) {
		if(username.equals(authentication.getName()) || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			User user = userRepository.findByUsername(username);
			if(user == null)
				throw new NotFoundException("User not found!");
			user.setName(name);
			user.setPhone(phone);
			user.setAddress(address);
			return userRepository.save(user);
		}
		throw new PermissionDeniedException("Not allowed!");
	}

	public List<User> getAll() {
		return userRepository.findAll();
	}

}
