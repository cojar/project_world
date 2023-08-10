package com.example.world;

import com.example.world.user.SiteUser;
import com.example.world.user.UserRepository;
import com.example.world.user.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;

import com.example.world.user.SiteUser;
import com.example.world.user.UserRole;
import com.example.world.user.UserRepository;
import com.example.world.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext
public class WorldApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	public void testCreateUser() {
		String username = "testuser";
		String password = "testpassword";
		String nickname = "Test Nickname";
		LocalDate birthDate = LocalDate.of(1990, 1, 1);
		Integer mailKey = 12345;
		UserRole role = UserRole.USER;
		boolean mailAuth = false;

		SiteUser createdUser = userService.create(username, password, nickname, birthDate, mailKey, role, mailAuth);

		assertNotNull(createdUser.getId());
		assertEquals(username, createdUser.getUsername());
		assertEquals(nickname, createdUser.getNickname());
		assertEquals(role, createdUser.getRole());
		assertEquals(birthDate, createdUser.getBirthDate());
		assertEquals(mailKey.intValue(), createdUser.getMailKey());
		assertEquals(mailAuth, createdUser.isMailAuth());
	}

	@Test
	public void testIsNicknameDuplicate() {
		String nickname = "ExistingNickname";


		SiteUser existingUser = new SiteUser();
		existingUser.setNickname(nickname);
		userRepository.save(existingUser);


		boolean isDuplicate = userService.isNicknameDuplicate(nickname);
		assertTrue(isDuplicate);
	}

	@Test
	public void testIsNicknameNotDuplicate() {
		String nickname = "NonExistingNickname";


		boolean isDuplicate = userService.isNicknameDuplicate(nickname);
		assertFalse(isDuplicate);
	}
}
