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

import com.example.vetapp.model.Role;
import com.example.vetapp.model.RoleName;
import com.example.vetapp.model.User;
import com.example.vetapp.repository.RoleRepository;
import com.example.vetapp.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AuthorizationIntegrationTest {
	
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
    
    private String userToken;
    private String doctorToken;
    private String adminToken;
    
    @Before
    public void setup() throws Exception {
    	Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
        Role doctorRole = roleRepository.findByName(RoleName.ROLE_DOCTOR).orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));                

        User user = new User("User User", "user", "user@gmail.com", encoder.encode(password));
        User doctor = new User("Doctor Doctor", "doctor", "doctor@gmail.com", encoder.encode(password));
        User admin = new User("Admin Admin", "admin", "admin@gmail.com", encoder.encode(password));
                
        user.setRoles(createRoleSet(userRole));
        doctor.setRoles(createRoleSet(doctorRole));
        admin.setRoles(createRoleSet(adminRole));

        userRepository.save(user);
        userRepository.save(doctor);
        userRepository.save(admin);

        userToken = obtainAccessToken(user.getUsername(), password);
        doctorToken = obtainAccessToken(doctor.getUsername(), password);
        adminToken = obtainAccessToken(admin.getUsername(), password);
    }
    
    @After
    public void tearDown() {
        userRepository.deleteAll();
    }
    
    @Test
    public void user_can_read_user() throws Exception {
    	HttpHeaders headers = creteHeader("Authorization", "Bearer " + userToken);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/test/user"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    public void user_cant_read_doctor() throws Exception {
    	HttpHeaders headers = creteHeader("Authorization", "Bearer " + userToken);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/test/doctor"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void user_cant_read_admin() throws Exception {
    	HttpHeaders headers = creteHeader("Authorization", "Bearer " + userToken);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/test/admin"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void doctor_can_read_user() throws Exception {
    	HttpHeaders headers = creteHeader("Authorization", "Bearer " + doctorToken);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/test/user"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void doctor_can_read_doctor() throws Exception {
    	HttpHeaders headers = creteHeader("Authorization", "Bearer " + doctorToken);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/test/doctor"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void doctor_cant_read_admin() throws Exception {
    	HttpHeaders headers = creteHeader("Authorization", "Bearer " + doctorToken);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/test/admin"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
    
    @Test
    public void admin_can_read_user() throws Exception {
    	HttpHeaders headers = creteHeader("Authorization", "Bearer " + adminToken);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/test/user"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    public void admin_can_read_doctor() throws Exception {
    	HttpHeaders headers = creteHeader("Authorization", "Bearer " + adminToken);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/test/doctor"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    public void admin_can_read_admin() throws Exception {
    	HttpHeaders headers = creteHeader("Authorization", "Bearer " + adminToken);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/test/admin"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
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
