package com.example.vetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.vetapp.model.User;
import com.example.vetapp.service.RoleService;

@PreAuthorize("hasRole('ADMIN')")
@RestController
public class RoleController {
	
	@Autowired
	RoleService roleService;
	
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "users/{username}/roles", method = RequestMethod.POST)
    public User addRole(
    	@PathVariable("username") String username,
    	@RequestParam(value="roleName") String roleName
    ){
    	return roleService.save(username, roleName);
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "users/{username}/roles/{role}", method = RequestMethod.DELETE)
    public void deleteMember(
    		@PathVariable("username") String username,
    		@PathVariable("role") String role
    ) {
    	roleService.delete(username, role); 
    }

}
