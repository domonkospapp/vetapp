package com.example.vetapp.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.vetapp.model.Role;
import com.example.vetapp.model.RoleName;
import com.example.vetapp.repository.RoleRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RoleRepositoryTest {

	@Autowired
	private RoleRepository roleRepository;
	
	@Test
	public void when_find_RoleUser() {
        Role foundRole = roleRepository.findByName(RoleName.ROLE_USER).orElse(null);
		assertThat(foundRole.getName()).isEqualTo(RoleName.ROLE_USER);
	}
	
	@Test
	public void when_find_RoleDoctor() {
        Role foundRole = roleRepository.findByName(RoleName.ROLE_DOCTOR).orElse(null);
		assertThat(foundRole.getName()).isEqualTo(RoleName.ROLE_DOCTOR);
	}
	
	@Test
	public void when_find_RoleAdmin() {
        Role foundRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElse(null);
		assertThat(foundRole.getName()).isEqualTo(RoleName.ROLE_ADMIN);
	}

}
