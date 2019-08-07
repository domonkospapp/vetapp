package com.example.vetapp;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

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
	
	@Before
	public void setUp() {
//		Optional<Role> userRole = Optional.of(new Role(RoleName.ROLE_USER));
//		Optional<Role> doctorRole = Optional.of(new Role(RoleName.ROLE_DOCTOR));
//		Optional<Role> adminRole = Optional.of(new Role(RoleName.ROLE_ADMIN));
//
//		Mockito.when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(userRole);
//		Mockito.when(roleRepository.findByName(RoleName.ROLE_DOCTOR)).thenReturn(doctorRole);
//		Mockito.when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(adminRole);
	}

	@Test
	public void save_role() {
		//Role role = new Role(RoleName.ROLE_USER);
		//assertThat(role.getName()).isEqualTo(RoleName.ROLE_USER);
	}

}
