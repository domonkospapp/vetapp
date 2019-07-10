package com.example.vetapp.integration;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
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

import com.example.vetapp.model.Role;
import com.example.vetapp.model.RoleName;
import com.example.vetapp.model.User;
import com.example.vetapp.repository.RoleRepository;
import com.example.vetapp.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RoleControllerIntegrationTest {
	
    @LocalServerPort
    private int port;
    
    TestRestTemplate restTemplate = new TestRestTemplate();
    
    @Autowired
    PasswordEncoder encoder;
    
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;

    private String password = "password";
    
    private String adminToken;
    
    @Before
    public void setup() throws Exception {
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));                
        User admin = new User("Admin Admin", "admin", "admin@gmail.com", encoder.encode(password));         
        admin.setRoles(createRoleSet(adminRole));
        userRepository.save(admin);
        adminToken = obtainAccessToken(admin.getUsername(), password);
    }
    
    @After
    public void tearDown() {
        userRepository.deleteAll();
    }
    
    @Test
    public void admin_can_add_role() throws Exception {
    	String username = "user_roleless";
        User user = new User("User Without Roles", username, "user2@gmail.com", encoder.encode(password));
        userRepository.save(user);
        
        HttpHeaders headers = creteHeader("Content-Type", "application/json");
    	headers.add("Authorization", "Bearer " + adminToken); 
        HttpEntity<?> entity = new HttpEntity<Object>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("users/" + username + "/roles?roleName=user"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
                
        Set<Role> roles = userRepository.findByUsername(username).getRoles();
        roles.forEach(role -> {
        	assertEquals("ROLE_USER" , role.getName().toString());
        });
    }
    
    @Test
    public void admin_can_add_all_roles() throws Exception {
    	String username = "user_roleless";
        User user = new User("User Without Roles", username, "user2@gmail.com", encoder.encode(password));
        userRepository.save(user);
        
        HttpHeaders headers = creteHeader("Content-Type", "application/json");
    	headers.add("Authorization", "Bearer " + adminToken); 
        HttpEntity<?> entity = new HttpEntity<Object>(headers);

        restTemplate.exchange(createURLWithPort("users/" + username + "/roles?roleName=user"), HttpMethod.POST, entity, String.class);
        restTemplate.exchange(createURLWithPort("users/" + username + "/roles?roleName=doctor"), HttpMethod.POST, entity, String.class);
        restTemplate.exchange(createURLWithPort("users/" + username + "/roles?roleName=admin"), HttpMethod.POST, entity, String.class);
                
        Set<Role> roles = userRepository.findByUsername(username).getRoles();
        assertEquals(3 , roles.size());  
    }
    
    @Test
    public void admin_cant_add_the_same_role_twice() throws Exception {
    	String username = "user_roleless";
        User user = new User("User Without Roles", username, "user2@gmail.com", encoder.encode(password));
        userRepository.save(user);
        
        HttpHeaders headers = creteHeader("Content-Type", "application/json");
    	headers.add("Authorization", "Bearer " + adminToken); 
        HttpEntity<?> entity = new HttpEntity<Object>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("users/" + username + "/roles?roleName=user"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("users/" + username + "/roles?roleName=user"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
                
        Set<Role> roles = userRepository.findByUsername(username).getRoles();
        assertEquals(1, roles.size());
    }
    
    @Test
    public void admin_cant_add_role_with_wrong_username() throws Exception {        
        HttpHeaders headers = creteHeader("Content-Type", "application/json");
    	headers.add("Authorization", "Bearer " + adminToken); 
        HttpEntity<?> entity = new HttpEntity<Object>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("users/not_user/roles?roleName=user"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    public void admin_can_delete_role() throws Exception {
    	//create test user
    	String username = "user_with_roles";
        User user = new User("User With Roles", username, "user2@gmail.com", encoder.encode(password));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));                
        Role doctorRole = roleRepository.findByName(RoleName.ROLE_DOCTOR).orElseThrow(() -> new RuntimeException("Fail! -> Cause: Doctor Role not find."));                
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        roles.add(doctorRole);
        user.setRoles(roles);
        userRepository.save(user);
  
        HttpHeaders headers = creteHeader("Content-Type", "application/json");
    	headers.add("Authorization", "Bearer " + adminToken);
        HttpEntity<?> entity = new HttpEntity<Object>(headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("users/" + user.getUsername() + "/roles/" + "doctor"), HttpMethod.DELETE, entity, String.class);
        Set<Role> rolesFromBD = userRepository.findByUsername(username).getRoles();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(1 , rolesFromBD.size());
        assertEquals(RoleName.ROLE_USER, rolesFromBD.stream().findFirst().get().getName());
    }
    
    private String obtainAccessToken(String username, String password) throws Exception {
    	HttpHeaders headers = creteHeader("Content-Type", "application/json");
    	String body = "{\"username\":\"" + username + "\", \"password\": \"" + password + "\"}";
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/auth/signin"), HttpMethod.POST, entity, String.class);
        String resultString = response.getBody();
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(resultString).get("accessToken").toString();
	}
    
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
	
	private Set<Role> createRoleSet(Role role){
        Set<Role> roles = new HashSet<>();
        roles.add(role);
		return roles;
	}
	
	private HttpHeaders creteHeader(String headerName, String headerValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(headerName, headerValue);
		return headers;
	}

}
