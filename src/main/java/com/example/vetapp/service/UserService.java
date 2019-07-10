package com.example.vetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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

	public User update(Authentication authentication, String username, String name, String email, String phone, String address) {
		// TODO Auto-generated method stub
		return null;
	}

}
