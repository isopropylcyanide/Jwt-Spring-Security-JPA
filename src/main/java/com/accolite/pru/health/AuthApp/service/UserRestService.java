package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.exception.UserAuthenticationException;
import com.accolite.pru.health.AuthApp.model.LoginRequest;
import com.accolite.pru.health.AuthApp.model.RegisterUserRequest;
import com.accolite.pru.health.AuthApp.model.Role;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.UserRole;
import com.accolite.pru.health.AuthApp.repository.UserRepository;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserRestService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private Logger logger;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Registers a new user in the database by performing a series of quick checks.
	 * @param newRegisterUserRequest
	 * @return A user object if successfully created
	 */
	public Optional<User> registerUser(RegisterUserRequest newRegisterUserRequest) {
		User newUser = newRegisterUserRequest.getUser();
		Boolean isNewUserAsAdmin = newRegisterUserRequest.getRegisterAsAdmin();
		
		Boolean emailAlreadyExists = emailAlreadyExists(newUser.getEmail());
		if (emailAlreadyExists){
			logger.error("Email already exists: " + newUser.getEmail());
			throw new UserAuthenticationException("Email already exists: " + newUser.getEmail());
		}
		newUser.setActive(true);
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		newUser.setLastIssuedDate(new Date());
		newUser.addRoles(getRolesForNewUser(isNewUserAsAdmin));
		User registeredNewUser = userRepository.saveAndFlush(newUser);
		return Optional.ofNullable(registeredNewUser);
	}

	/**
	 * Performs a quick check to see what roles the new user could benefit from
	 * @param isAdmin
	 * @return list of roles for the new user
	 */
	private Set<Role> getRolesForNewUser(Boolean isAdmin) {
		Set<Role> newUserRoles = new HashSet<>();
		newUserRoles.add(roleService.getRoleByUserRole(UserRole.ROLE_USER));
		if (isAdmin){
			newUserRoles.add(roleService.getRoleByUserRole(UserRole.ROLE_ADMIN));
		}
		return newUserRoles;
	}

	/**
	 * Checks if the given email already exists in the database repository or not
	 * @param email
	 * @return true if the email exists else false
	 */
	private Boolean emailAlreadyExists(String email) {
		return userRepository.existsByEmail(email);
	}


	/**
	 * Tries to log the given user in from the incoming request and return the object if found
	 * @param loginRequest
	 * @return Optional<User> indicating that the user might or might not be present
	 */
	public Optional<User> loginUser(LoginRequest loginRequest) {
		return null;
	}
}
