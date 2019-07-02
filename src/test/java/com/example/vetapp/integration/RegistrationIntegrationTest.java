package com.example.vetapp.integration;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.vetapp.message.request.SignUpForm;
import com.example.vetapp.model.Role;
import com.example.vetapp.model.RoleName;
import com.example.vetapp.model.User;
import com.example.vetapp.repository.RoleRepository;
import com.example.vetapp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RegistrationIntegrationTest {
    
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
    @Autowired
    PasswordEncoder encoder;
	
    @LocalServerPort
    private int port;
    
	ObjectMapper mapper = new ObjectMapper();

    TestRestTemplate restTemplate = new TestRestTemplate();

    private String password = "password";

    @Before
    public void tearDown() {
        userRepository.deleteAll();
    }
    
    @Test
    public void username_taken() throws Exception {
    	Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
        User user = new User("User User", "dpapp", "user@gmail.com", encoder.encode(password));
        user.setRoles(createRoleSet(userRole));
        userRepository.save(user);
    	
    	HttpHeaders headers = creteHeader("Content-Type", "application/json");
		String body = mapper.writeValueAsString(new SignUpForm("Domonkos Papp", "dpapp", "dpapp@gmail.com", password));
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/auth/signup"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Fail -> Username is already taken!", response.getBody());
    }
    
    @Test
    public void email_taken() throws Exception {
    	Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
        User user = new User("User User", "user", "user@gmail.com", encoder.encode(password));
        user.setRoles(createRoleSet(userRole));
        userRepository.save(user);
    	
    	HttpHeaders headers = creteHeader("Content-Type", "application/json");
    	SignUpForm form = new SignUpForm("Domonkos Papp", "dpapp", "user@gmail.com", password);
		String body = mapper.writeValueAsString(form);
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
                
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/auth/signup"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Fail -> Email is already in use!", response.getBody());
    }
    
    @Test
    public void user_can_signup() throws Exception {
    	HttpHeaders headers = creteHeader("Content-Type", "application/json");
    	
    	SignUpForm form = new SignUpForm("Domonkos Papp", "dpapp", "dpapp@gmail.com", password);
		ObjectMapper mapper = new ObjectMapper();
		String body = mapper.writeValueAsString(form);
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/auth/signup"), HttpMethod.POST, entity, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        User savedUser = userRepository.findByUsername(form.getUsername());
        
        assertEquals(form.getName(), savedUser.getName());
        assertEquals(form.getUsername(), savedUser.getUsername());
        assertEquals(form.getEmail(), savedUser.getEmail());
    }
    
    @Test
    public void user_can_login() throws Exception {
    	Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
        User user = new User("User User", "user", "user@gmail.com", encoder.encode(password));
        user.setRoles(createRoleSet(userRole));
        userRepository.save(user);
    	
    	HttpHeaders headers = creteHeader("Content-Type", "application/json");
    	String body = "{\"username\":\"" + user.getUsername() + "\", \"password\": \"" + password + "\"}";
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/auth/signin"), HttpMethod.POST, entity, String.class);
        
        String resultString = response.getBody();
    	JacksonJsonParser jsonParser = new JacksonJsonParser();
    	System.out.println(jsonParser.parseMap(resultString).get("accessToken").toString());
        
    	assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
	
	private HttpHeaders creteHeader(String headerName, String headerValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(headerName, headerValue);
		return headers;
	}
	
	private Set<Role> createRoleSet(Role role){
        Set<Role> roles = new HashSet<>();
        roles.add(role);
		return roles;
	}

}
