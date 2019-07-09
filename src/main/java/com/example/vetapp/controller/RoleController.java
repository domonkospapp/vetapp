package com.example.vetapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.vetapp.model.User;

@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
	
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{userId}/roles", method = RequestMethod.POST)
    public User addRole(
    	@PathVariable("userId") long userId,
    	@RequestParam(value="roleName") String roleName
    ){
    	return null;//roleService.save(userId, roleName);
    }

}
