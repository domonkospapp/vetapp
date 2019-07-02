package com.example.vetapp.message.request;

import javax.validation.constraints.*;
 
public class SignUpForm {
	
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
 
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;
 
    @NotBlank
    @Size(max = 60)
    @Email
    private String email;
        
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
 
    public SignUpForm(
    		@NotBlank @Size(min = 3, max = 50) String name,
			@NotBlank @Size(min = 3, max = 50) String username, 
			@NotBlank @Size(max = 60) @Email String email,
			@NotBlank @Size(min = 6, max = 40) String password
	) {
		super();
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
}
