
package com.schoolportal.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolportal.entity.User;
import com.schoolportal.repository.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// SIGNUP
	@Transactional
	public boolean signup(User user) throws Exception {

		if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
			throw new Exception("First name is required.");
		}

		if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
			throw new Exception("Last name is required.");
		}

		if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
			throw new Exception("Email is required.");
		}

		if (user.getPassword() == null || user.getPassword().length() < 6) {
			throw new Exception("Password must be at least 6 characters.");
		}

		String email = user.getEmail().trim();

		if (userRepository.findByEmail(email).isPresent()) {
			throw new Exception("Email already in use.");
		}

		String hashedPassword = passwordEncoder.encode(user.getPassword());

		user.setEmail(email);
		user.setPassword(hashedPassword);

		userRepository.save(user);

		return true;
	}

	// LOGIN
	@Transactional(readOnly = true)
	public User login(String email, String plainPassword) throws Exception {

		if (email == null || email.trim().isEmpty() || plainPassword == null || plainPassword.isEmpty()) {
			throw new Exception("Invalid email or password.");
		}

		User user = userRepository.findByEmail(email.trim())
				.orElseThrow(() -> new Exception("Invalid email or password."));

		boolean match = passwordEncoder.matches(plainPassword, user.getPassword());

		if (!match) {
			throw new Exception("Invalid email or password.");
		}

		// Initialize the lazy-loaded user type relationship
		user.getUserType().getUserType();

		// Do not modify the password stored in the entity.
		// The controller will construct a safe response without the password.

		return user;
	}
}
