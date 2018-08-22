package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.exception.AppException;
import com.accolite.pru.health.AuthApp.exception.ResourceAlreadyInUseException;
import com.accolite.pru.health.AuthApp.model.Role;
import com.accolite.pru.health.AuthApp.model.RoleName;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.payload.LoginRequest;
import com.accolite.pru.health.AuthApp.model.payload.RegistrationRequest;
import com.accolite.pru.health.AuthApp.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	private static final Logger logger = Logger.getLogger(AuthService.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;


	/**
	 * Registers a new user in the database by performing a series of quick checks.
	 * @return A user object if successfully created
	 */
	public Optional<User> registerUser(RegistrationRequest newRegistrationRequest) {
		String newRegistrationRequestEmail = newRegistrationRequest.getEmail();
		Boolean emailAlreadyExists = emailAlreadyExists(newRegistrationRequestEmail);
		if (emailAlreadyExists) {
			logger.error("Email already exists: " + newRegistrationRequestEmail);
			throw new ResourceAlreadyInUseException("Email", "Address", newRegistrationRequestEmail);
		}
		logger.info("Trying to register new user [" + newRegistrationRequestEmail + "]");
		User newUser = new User();
		Boolean isNewUserAsAdmin = newRegistrationRequest.getRegisterAsAdmin();
		newUser.setEmail(newRegistrationRequestEmail);
		newUser.setPassword(passwordEncoder.encode(newRegistrationRequest.getPassword()));
		newUser.setUsername(newRegistrationRequestEmail);
		newUser.addRoles(getRolesForNewUser(isNewUserAsAdmin));
		newUser.setActive(true);
		User registeredNewUser = userRepository.save(newUser);
		return Optional.ofNullable(registeredNewUser);
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

	/**
	 * Checks if the given email already exists in the database repository or not
	 * @return true if the email exists else false
	 */
	public Boolean emailAlreadyExists(String email) {
		return userRepository.existsByEmail(email);
	}

	/**
	 * Checks if the given email already exists in the database repository or not
	 * @return true if the email exists else false
	 */
	public Boolean usernameAlreadyExists(String username) {
		return userRepository.existsByUsername(username);
	}


	/**
	 * Authenticate user and log them in given a loginRequest
	 */
	public Optional<Authentication> authenticateUser(LoginRequest loginRequest) {
		return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
				loginRequest.getPassword())));
	}
}
