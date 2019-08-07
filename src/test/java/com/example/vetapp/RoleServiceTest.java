package com.example.vetapp;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.vetapp.exception.ConflictException;
import com.example.vetapp.model.Role;
import com.example.vetapp.model.RoleName;
import com.example.vetapp.model.User;
import com.example.vetapp.repository.UserRepository;
import com.example.vetapp.service.RoleNameService;
import com.example.vetapp.service.RoleService;

@RunWith(SpringRunner.class)
public class RoleServiceTest {

	@TestConfiguration
	static class EmployeeServiceImplTestContextConfiguration {
		
		@Bean
		public RoleService roleService() {
			return new RoleService();
		}
	}
	
	@Autowired
	private RoleService roleService;
	
	@MockBean
	private RoleNameService roleNameService;
	
	@MockBean
	private UserRepository userRepository;

	private Role userRole;
	private Role doctorRole;
	private Role adminRole;
	
	@Before
	public void setUp() {
		userRole = new Role(RoleName.ROLE_USER);
		doctorRole = new Role(RoleName.ROLE_DOCTOR);
		adminRole = new Role(RoleName.ROLE_ADMIN);

		Mockito.when(roleNameService.getRoleByName("user")).thenReturn(userRole);
		Mockito.when(roleNameService.getRoleByName("doctor")).thenReturn(doctorRole);
		Mockito.when(roleNameService.getRoleByName("admin")).thenReturn(adminRole);
	}

	@Test
	public void save_role() {
		User user = new User("Test User", "test_user", "test@test.com", "password");

		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		
		User userWithRole = roleService.save(user.getUsername(), "user");

		assertThat(userWithRole.getRoles().size()).isEqualTo(1);
		assertThat(userWithRole.getRoles().contains(userRole)).isEqualTo(true);
	}

	@Test(expected = ConflictException.class)
	public void save_exists_role() {
		User user = new User("Test User", "test_user", "test@test.com", "password");
		user.getRoles().add(userRole);

		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);

		roleService.save(user.getUsername(), "user");
	}

}
