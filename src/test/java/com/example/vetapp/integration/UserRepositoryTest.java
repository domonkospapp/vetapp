package com.example.vetapp.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.vetapp.model.User;
import com.example.vetapp.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class UserRepositoryTest {
	
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void when_findByUsername_return_user() {
		User user = new User("Test Test", "test", "mail@mail.com", "password");
		entityManager.persist(user);
		entityManager.flush();

		User foundUser = userRepository.findByUsername(user.getUsername());
		
		assertThat(foundUser.getName()).isEqualTo(user.getName());
	}
	
	@Test
	public void when_findByUsername_not_found() {
		User foundUser = userRepository.findByUsername("username");
		
		assertThat(foundUser).isEqualTo(null);
	}
	
	@Test
	public void when_existsByUsername_return_true() {
		User user = new User("Test Test", "test", "mail@mail.com", "password");
		entityManager.persist(user);
		entityManager.flush();

		boolean found = userRepository.existsByUsername(user.getUsername());
		
		assertThat(found).isEqualTo(true);
	}
	
	@Test
	public void when_existsByUsername_return_false() {
		boolean found = userRepository.existsByUsername("username");
		
		assertThat(found).isEqualTo(false);
	}

}