package com.example.vetapp.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.vetapp.controller.AuthController;
import com.example.vetapp.controller.TestController;
import com.example.vetapp.model.Role;
import com.example.vetapp.model.RoleName;
import com.example.vetapp.model.User;
import com.example.vetapp.repository.RoleRepository;
import com.example.vetapp.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AuthorizationIntegrationTest {
	
    MockMvc mockMvc;

	@Autowired
	TestController testController;
	
	@Autowired
	AuthController authController;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    private String password = "password";
    
    private String userToken;
    private String doctorToken;
    private String adminToken;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(this.authController, this.testController).build();// Standalone context
        
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

        this.userToken = obtainAccessToken(user.getUsername(), password);
        this.userToken = obtainAccessToken(doctor.getUsername(), password);
        this.userToken = obtainAccessToken(admin.getUsername(), password);
    }
    
	@Test
	public void first() throws Exception {
        mockMvc.perform(get("/api/test/user")
        	.header("Authorization", "Bearer " + userToken))
        	.andExpect(status().isOk());
	}
	
	private String obtainAccessToken(String username, String password) throws Exception {
		String body = "{\"username\":\"" + username + "\", \"password\": \"" + password + "\"}";
		ResultActions result = mockMvc.perform(post("/api/auth/signin")
		        .contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isOk());		
		String resultString = result.andReturn().getResponse().getContentAsString();
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(resultString).get("accessToken").toString();
	}
	
	private Set<Role> createRoleSet(Role role){
        Set<Role> roles = new HashSet<>();
        roles.add(role);
		return roles;
	}
}
