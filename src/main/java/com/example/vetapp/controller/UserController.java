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

import com.example.vetapp.model.User;
import com.example.vetapp.service.UserService;

@RestController
@PreAuthorize("hasRole('USER')")
public class UserController {
	
	UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "users/{username}", method = RequestMethod.PUT)
    public User updateUser(
    	Authentication authentication,
    	@PathVariable("username") String username,
    	@RequestParam(value="name") String name,
    	@RequestParam(value="phone") String phone,
    	@RequestParam(value="address") String address
    ){
    	return userService.update(authentication, username, name, phone, address);
    }
	

}
