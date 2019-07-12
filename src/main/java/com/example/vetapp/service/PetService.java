package com.example.vetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vetapp.exception.NotFoundException;
import com.example.vetapp.model.Pet;
import com.example.vetapp.model.User;
import com.example.vetapp.repository.PetRepository;

@Service
public class PetService {
	
	UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	PetRepository petRepository;
	
	@Autowired
	public void setPetRepository(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	public Pet save(String username, String name, Long yearOfBirth, String type) {
		User owner = userService.findUserByUsername(username);
		return petRepository.save(new Pet(name, yearOfBirth, type, owner));
	}

	public Pet getById(Long id) {
		Pet pet = petRepository.findById(id).orElse(null);
		if(pet == null) {
			throw new NotFoundException("Pet not found!");
		}
		return pet;
	}

}