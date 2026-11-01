
package com.schoolportal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.schoolportal.entity.User;
import com.schoolportal.entity.UserType;
import com.schoolportal.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AuthService authService;

	private User user;

	@BeforeEach
	void setUp() {

		user = new User();
		user.setFirstName("John");
		user.setLastName("Smith");
		user.setEmail("john@example.com");
		user.setPassword("password123");
	}

	// SIGNUP TESTS

	@Test
	void signup_validUser_shouldEncodePasswordAndSaveUser() throws Exception {

		when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

		when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

		assertTrue(authService.signup(user));

		assertEquals("john@example.com", user.getEmail());
		assertEquals("encodedPassword", user.getPassword());

		verify(passwordEncoder).encode("password123");
		verify(userRepository).save(user);
	}

	@Test
	void signup_shouldTrimEmailBeforeSaving() throws Exception {

		user.setEmail("  john@example.com  ");

		when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

		when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

		assertTrue(authService.signup(user));

		assertEquals("john@example.com", user.getEmail());
		verify(userRepository).findByEmail("john@example.com");
	}

	@Test
	void signup_nullFirstName_shouldThrowException() {

		user.setFirstName(null);

		Exception exception = assertThrows(Exception.class, () -> authService.signup(user));

		assertEquals("First name is required.", exception.getMessage());
		verifyNoInteractions(userRepository, passwordEncoder);
	}

	@Test
	void signup_blankFirstName_shouldThrowException() {

		user.setFirstName("   ");

		Exception exception = assertThrows(Exception.class, () -> authService.signup(user));

		assertEquals("First name is required.", exception.getMessage());
	}

	@Test
	void signup_nullLastName_shouldThrowException() {

		user.setLastName(null);

		Exception exception = assertThrows(Exception.class, () -> authService.signup(user));

		assertEquals("Last name is required.", exception.getMessage());
	}

	@Test
	void signup_blankLastName_shouldThrowException() {

		user.setLastName("   ");

		Exception exception = assertThrows(Exception.class, () -> authService.signup(user));

		assertEquals("Last name is required.", exception.getMessage());
	}

	@Test
	void signup_nullEmail_shouldThrowException() {

		user.setEmail(null);

		Exception exception = assertThrows(Exception.class, () -> authService.signup(user));

		assertEquals("Email is required.", exception.getMessage());
	}

	@Test
	void signup_blankEmail_shouldThrowException() {

		user.setEmail("   ");

		Exception exception = assertThrows(Exception.class, () -> authService.signup(user));

		assertEquals("Email is required.", exception.getMessage());
	}

	@Test
	void signup_nullPassword_shouldThrowException() {

		user.setPassword(null);

		Exception exception = assertThrows(Exception.class, () -> authService.signup(user));

		assertEquals("Password must be at least 6 characters.", exception.getMessage());
	}

	@Test
	void signup_shortPassword_shouldThrowException() {

		user.setPassword("12345");

		Exception exception = assertThrows(Exception.class, () -> authService.signup(user));

		assertEquals("Password must be at least 6 characters.", exception.getMessage());
	}

	@Test
	void signup_existingEmail_shouldThrowException() {

		when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

		Exception exception = assertThrows(Exception.class, () -> authService.signup(user));

		assertEquals("Email already in use.", exception.getMessage());

		verify(passwordEncoder, never()).encode(any());
		verify(userRepository, never()).save(any());
	}

	// LOGIN TESTS

	@Test
	void login_validCredentials_shouldReturnUser() throws Exception {

		UserType userType = new UserType();
		userType.setId(1);
		userType.setUserType("Student");

		user.setUserType(userType);

		when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

		when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

		user.setPassword("encodedPassword");

		User result = authService.login("john@example.com", "password123");

		assertNotNull(result);
		assertEquals("john@example.com", result.getEmail());
		assertEquals("encodedPassword", result.getPassword());
		assertEquals("Student", result.getUserType().getUserType());

		verify(passwordEncoder).matches("password123", "encodedPassword");
	}

	@Test
	void login_shouldTrimEmail() throws Exception {

		UserType userType = new UserType();
		userType.setUserType("Student");
		user.setUserType(userType);
		user.setPassword("encodedPassword");

		when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

		when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

		User result = authService.login("  john@example.com  ", "password123");

		assertNotNull(result);

		verify(userRepository).findByEmail("john@example.com");
	}

	@Test
	void login_nullEmail_shouldThrowException() {

		Exception exception = assertThrows(Exception.class, () -> authService.login(null, "password123"));

		assertEquals("Invalid email or password.", exception.getMessage());
		verifyNoInteractions(userRepository, passwordEncoder);
	}

	@Test
	void login_blankEmail_shouldThrowException() {

		Exception exception = assertThrows(Exception.class, () -> authService.login("   ", "password123"));

		assertEquals("Invalid email or password.", exception.getMessage());
	}

	@Test
	void login_nullPassword_shouldThrowException() {

		Exception exception = assertThrows(Exception.class, () -> authService.login("john@example.com", null));

		assertEquals("Invalid email or password.", exception.getMessage());
	}

	@Test
	void login_blankPassword_shouldThrowException() {

		Exception exception = assertThrows(Exception.class, () -> authService.login("john@example.com", ""));

		assertEquals("Invalid email or password.", exception.getMessage());
	}

	@Test
	void login_userNotFound_shouldThrowException() {

		when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

		Exception exception = assertThrows(Exception.class, () -> authService.login("john@example.com", "password123"));

		assertEquals("Invalid email or password.", exception.getMessage());
	}

	@Test
	void login_wrongPassword_shouldThrowException() {

		user.setPassword("encodedPassword");

		when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

		when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

		Exception exception = assertThrows(Exception.class,
				() -> authService.login("john@example.com", "wrongPassword"));

		assertEquals("Invalid email or password.", exception.getMessage());
	}
}
