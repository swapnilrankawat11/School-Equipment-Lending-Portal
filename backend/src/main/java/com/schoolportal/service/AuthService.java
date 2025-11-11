package com.schoolportal.service;

import com.schoolportal.dao.UserDAO;
import com.schoolportal.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
	private UserDAO userDao = new UserDAO();

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

		if (userDao.getUserByEmail(user.getEmail()) != null) {
			throw new Exception("Email already in use.");
		}

		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashed);

		boolean isAccountCreated = userDao.createUser(user);
		if (!isAccountCreated)
			throw new Exception("Failed to create user.");
		return isAccountCreated;
	}

	public User login(String email, String plainPassword) throws Exception {
		User user = userDao.getUserByEmail(email);
		if (user == null)
			throw new Exception("Invalid email or password.");

		boolean match = BCrypt.checkpw(plainPassword, user.getPassword());
		if (!match)
			throw new Exception("Invalid email or password.");

		user.setPassword(null);
		return user;
	}
}
