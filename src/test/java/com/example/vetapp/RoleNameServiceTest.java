package com.example.vetapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

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
	
	@Test
	public void getRoleByName() { }

}
