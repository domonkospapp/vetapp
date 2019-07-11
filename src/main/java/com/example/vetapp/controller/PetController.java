package com.example.vetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.vetapp.model.Pet;
import com.example.vetapp.service.PetService;

@RestController
public class PetController {
	
	@Autowired
	PetService petService;
	
	@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "users/{username}/pets", method = RequestMethod.POST)
    public Pet addPet(
        Authentication authentication,
    	@PathVariable("username") String username,
    	@RequestParam(value="name") String name,
    	@RequestParam(value="yearOfBirth") Long yearOfBirth,
    	@RequestParam(value="type") String type
    ){
    	return petService.save(authentication, username, name, yearOfBirth, type);
    }

}
