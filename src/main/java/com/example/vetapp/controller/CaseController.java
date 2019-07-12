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
import com.example.vetapp.service.CaseService;

@RestController
public class CaseController {
	
	@Autowired
	CaseService caseService;
	
	@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "users/{username}/pets/{petId}", method = RequestMethod.POST)
    public Pet addPet(
        Authentication authentication,
    	@PathVariable("username") String username,
        @PathVariable("petId") String petId,
    	@RequestParam(value="name") String name,
    	@RequestParam(value="description") String description,
    	@RequestParam(value="price") String price
    ){
    	return caseService.save(authentication, username, petId, name, description, price);
    }

}