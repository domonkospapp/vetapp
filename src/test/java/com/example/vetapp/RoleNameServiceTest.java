package com.example.vetapp;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.vetapp.model.Role;
import com.example.vetapp.model.RoleName;
import com.example.vetapp.repository.RoleRepository;
import com.example.vetapp.service.RoleNameService;

@RunWith(SpringRunner.class)
public class RoleNameServiceTest {

	@TestConfiguration
	static class EmployeeServiceImplTestContextConfiguration {
		
		@Bean
		public RoleNameService roleNameService() {
			return new RoleNameService();
		}
	}
	
	@Autowired
	private RoleNameService roleNameService;
	
	@MockBean
	private RoleRepository roleRepository;
	
	@Before
	public void setUp() {
		Optional<Role> userRole = Optional.of(new Role(RoleName.ROLE_USER));
		Optional<Role> doctorRole = Optional.of(new Role(RoleName.ROLE_DOCTOR));
		Optional<Role> adminRole = Optional.of(new Role(RoleName.ROLE_ADMIN));

		Mockito.when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(userRole);
		Mockito.when(roleRepository.findByName(RoleName.ROLE_DOCTOR)).thenReturn(doctorRole);
		Mockito.when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(adminRole);
	}

	@Test
	public void getRoleByName_user() {
		Role role = roleNameService.getRoleByName("user");
		assertThat(role.getName()).isEqualTo(RoleName.ROLE_USER);
	}

	@Test
	public void getRoleByName_doctor() {
		Role role = roleNameService.getRoleByName("doctor");
		assertThat(role.getName()).isEqualTo(RoleName.ROLE_DOCTOR);
	}

	@Test
	public void getRoleByName_admin() {
		Role role = roleNameService.getRoleByName("admin");
		assertThat(role.getName()).isEqualTo(RoleName.ROLE_ADMIN);
	}

}
