package com.example.vetapp.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.vetapp.model.Pet;

@Service
public class PetService {

	public Pet save(Authentication authentication, String username, String name, Long yearOfBirth, String type) {
		// TODO Auto-generated method stub
		return null;
	}

}
