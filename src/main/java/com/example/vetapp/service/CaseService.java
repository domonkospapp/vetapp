package com.example.vetapp.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.vetapp.model.Pet;

@Service
public class CaseService {

	public Pet save(Authentication authentication, String username, String petId, String name, String description,
			String price) {
		// TODO Auto-generated method stub
		return null;
	}

}
