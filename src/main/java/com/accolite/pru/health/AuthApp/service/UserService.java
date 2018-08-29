package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.exception.AppException;
import com.accolite.pru.health.AuthApp.model.Role;
import com.accolite.pru.health.AuthApp.model.RoleName;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.payload.RegistrationRequest;
import com.accolite.pru.health.AuthApp.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	private static final Logger logger = Logger.getLogger(UserService.class);

	/**
	 * Finds a user in the database by username
	 */
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	/**
	 * Finds a user in the database by email
	 */
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * Finds current logged in user in the database by email
	 * Note: This will always return a concrete valid instance.
	 */
	public User getLoggedInUser(String email) {
		return findByEmail(email).get();
	}

	/**
	 * Save the user to the database
	 */
	public User save(User user) {
		return userRepository.save(user);
	}

	/**
	 * Check is the user exists given the email: naturalId
	 */
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	/**
	 * Check is the user exists given the username: naturalId
	 */
	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	/**
	 * Creates a new user from a request
	 */
	public User createUser(RegistrationRequest registerRequest) {
		User newUser = new User();
		Boolean isNewUserAsAdmin = registerRequest.getRegisterAsAdmin();
		newUser.setEmail(registerRequest.getEmail());
		newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		newUser.setUsername(registerRequest.getEmail());
		newUser.addRoles(getRolesForNewUser(isNewUserAsAdmin));
		newUser.setActive(true);
		newUser.setEmailVerified(false);
		return newUser;
	}

	/**
	 * Performs a quick check to see what roles the new user could benefit from
	 * @return list of roles for the new user
	 */
	private Set<Role> getRolesForNewUser(Boolean isAdmin) {
		Set<Role> newUserRoles = new HashSet<>();
		newUserRoles.add(roleService.findByRole(RoleName.ROLE_USER).orElseThrow(() -> new AppException("ROLE_USER " +
				" is not set in database.")));
		if (isAdmin) {
			newUserRoles.add(roleService.findByRole(RoleName.ROLE_ADMIN).orElseThrow(() -> new AppException(
					"ROLE_ADMIN" + "not set in database.")));
		}
		return newUserRoles;
	}
}
