package com.example.vetapp.integration;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
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

import com.example.vetapp.model.Case;
import com.example.vetapp.model.Pet;
import com.example.vetapp.model.Role;
import com.example.vetapp.model.RoleName;
import com.example.vetapp.model.User;
import com.example.vetapp.repository.PetRepository;
import com.example.vetapp.repository.RoleRepository;
import com.example.vetapp.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class CaseControllerIntegrationTest {
	
    @LocalServerPort
    private int port;
    
    TestRestTemplate restTemplate = new TestRestTemplate();
    
    @Autowired
    PasswordEncoder encoder;
    
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PetRepository petRepository;

    private String password = "password";
    
    //store auth token
    private HttpEntity<?> entity;
    
    User doctor;
    private User owner;
    private Pet pet;
        
    @Before
    public void setup() throws Exception {
        Role doctorRole = roleRepository.findByName(RoleName.ROLE_DOCTOR).orElseThrow(() -> new RuntimeException("Fail! -> Cause: Doctor Role not find."));                
        doctor = new User("Doctor Doctor", "doctor", "doctor@gmail.com", encoder.encode(password));
        doctor.setRoles(createRoleSet(doctorRole));
        userRepository.save(doctor);
        String doctorToken = obtainAccessToken(doctor.getUsername(), password);
        
        HttpHeaders headers = creteHeader("Content-Type", "application/json");
    	headers.add("Authorization", "Bearer " + doctorToken); 
        entity = new HttpEntity<Object>(headers);
        
        //create test owner
        owner = new User("User Owner", "owner", "owner@gmail.com", encoder.encode(password));
        userRepository.save(owner);
        
        //create test pet
    	pet = new Pet("Pet Name", new Long(2019), "Doggo", owner);
        petRepository.save(pet);
    }
    
    @After
    public void tearDown() {
        userRepository.deleteAll();
    }
    
    @Test
    public void doctor_can_add_case() throws Exception {
    	Case petCase = new Case(null, "case name", "case description", "10$", null);
    	ResponseEntity<String> response = restTemplate.exchange(
    			createURLWithPort(
    					"users/" + owner.getUsername() + "/pets/" + pet.getId() +
    					"?name=" + petCase.getName() +
    					"&description=" + petCase.getDescription() + 
    					"&price=" + petCase.getPrice()
    			), HttpMethod.POST, entity, String.class);
    	Pet petFromDB = userRepository.findByUsername(owner.getUsername()).getPets().get(0);
    	
    	assertEquals(HttpStatus.CREATED, response.getStatusCode());
    	assertEquals(1, petFromDB.getCases().size());

    	assertEquals(petCase.getName(), petFromDB.getCases().get(0).getName());
    	assertEquals(petCase.getDescription(), petFromDB.getCases().get(0).getDescription());
    	assertEquals(petCase.getPrice(), petFromDB.getCases().get(0).getPrice());
    	assertEquals(doctor.getId(), petFromDB.getCases().get(0).getDoctor().getId());
    	assertEquals(pet.getId(), petFromDB.getCases().get(0).getPet().getId());
    	assertEquals(pet.getOwner().getId(), petFromDB.getCases().get(0).getPet().getOwner().getId());
    }
    
    @Test
    public void doctor_cant_add_case_to_not_exist_user() throws Exception {
    	Case petCase = new Case(null, "case name", "case description", "10$", null);
    	ResponseEntity<String> response = restTemplate.exchange(
    			createURLWithPort(
    					"users/" + "not_a_user" + "/pets/" + pet.getId() +
    					"?name=" + petCase.getName() +
    					"&description=" + petCase.getDescription() + 
    					"&price=" + petCase.getPrice()
    			), HttpMethod.POST, entity, String.class);
    	
    	assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    public void doctor_cant_add_case_to_oter_users_pet() throws Exception {
    	User otherOwner = new User("Other User Owner", "otherowner", "otherowner@gmail.com", encoder.encode(password));
        userRepository.save(otherOwner);
    	
    	Case petCase = new Case(null, "case name", "case description", "10$", null);
    	ResponseEntity<String> response = restTemplate.exchange(
    			createURLWithPort(
    					"users/" + otherOwner.getUsername() + "/pets/" + pet.getId() +
    					"?name=" + petCase.getName() +
    					"&description=" + petCase.getDescription() + 
    					"&price=" + petCase.getPrice()
    			), HttpMethod.POST, entity, String.class);
    	
    	assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    public void doctor_cant_add_case_to_not_exist_pet() throws Exception {
    	Case petCase = new Case(null, "case name", "case description", "10$", null);
    	ResponseEntity<String> response = restTemplate.exchange(
    			createURLWithPort(
    					"users/" + owner.getUsername() + "/pets/" + new Long(404) +
    					"?name=" + petCase.getName() +
    					"&description=" + petCase.getDescription() + 
    					"&price=" + petCase.getPrice()
    			), HttpMethod.POST, entity, String.class);
    	
    	assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
