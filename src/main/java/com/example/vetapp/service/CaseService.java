package com.example.vetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.vetapp.exception.NotFoundException;
import com.example.vetapp.model.Case;
import com.example.vetapp.model.Pet;
import com.example.vetapp.model.User;
import com.example.vetapp.repository.CaseRepository;

@Service
public class CaseService {
	
	UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	PetService petService;
	
	@Autowired
	public void setPetService(PetService petService) {
		this.petService = petService;
	}
	
	CaseRepository caseRepository;
	
	@Autowired
	public void setCaseRepository(CaseRepository caseRepository) {
		this.caseRepository = caseRepository;
	}

	public Case save(Authentication authentication, String username, Long petId, String name, String description,
			String price) {
		User owner = userService.findUserByUsername(username);
		User doctor = userService.findUserByUsername(authentication.getName());
		Pet pet = petService.getById(petId);
		
		if(!owner.getPets().contains(pet)) {
			throw new NotFoundException("Pet not found!");
		}
		return caseRepository.save(new Case(doctor, name, description, price, pet));
	}

}
