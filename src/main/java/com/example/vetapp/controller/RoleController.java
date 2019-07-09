package com.example.vetapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.vetapp.model.Role;
import com.example.vetapp.model.User;
import com.example.vetapp.service.RoleService;

@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
	
	@Autowired
	RoleService roleService;
	
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{username}/roles", method = RequestMethod.POST)
    public User addRole(
    	@PathVariable("username") String username,
    	@RequestParam(value="roleName") String roleName
    ){
    	return roleService.save(username, roleName);
    }
    
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{username}/roles", method = RequestMethod.GET)
    public List<Role> getRoles(
        	@PathVariable("username") String username
    ){
    	return roleService.get(username); 
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{userId}/roles/{roleId}", method = RequestMethod.DELETE)
    public void deleteMember(
    		@PathVariable("username") String username,
    		@PathVariable("roleId") String roleId
    ) {
    	roleService.delete(username, roleId); 
    }

}