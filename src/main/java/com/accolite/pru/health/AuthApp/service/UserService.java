package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.exception.UserLogoutException;
import com.accolite.pru.health.AuthApp.model.CustomUserDetails;
import com.accolite.pru.health.AuthApp.model.Role;
import com.accolite.pru.health.AuthApp.model.RoleName;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.UserDevice;
import com.accolite.pru.health.AuthApp.model.payload.LogOutRequest;
import com.accolite.pru.health.AuthApp.model.payload.RegistrationRequest;
import com.accolite.pru.health.AuthApp.model.token.RefreshToken;
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

	@Autowired
	private UserDeviceService userDeviceService;

	@Autowired
	private RefreshTokenService refreshTokenService;

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
	 * Finds current logged in user in the database by email Note: This will always
	 * return a concrete valid instance.
	 */
	public User getLoggedInUser(String email) {
		return findByEmail(email).get();
	}

	/**
	 * Find a user in db by id.
	 */
	public Optional<User> findById(Long Id) {
		return userRepository.findById(Id);
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
	 * Creates a new user from the registration request
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
		Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
		Role adminRole = new Role(RoleName.ROLE_ADMIN);
		if (!isAdmin) {
			newUserRoles.remove(adminRole);
		}
		logger.info("Setting user roles: " + newUserRoles);
		return newUserRoles;
	}

	/**
	 * Log the given user out and delete the refresh token associated with it. If no device
	 * is associated with it, fail silently.
	 */
	public void logoutUser(CustomUserDetails customUserDetails, LogOutRequest logOutRequest) {
		String deviceId = logOutRequest.getDeviceInfo().getDeviceId();
		Optional<UserDevice> userDeviceOpt = userDeviceService.findByDeviceId(deviceId);
		userDeviceOpt.orElseThrow(() -> new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(), "" +
				"Invalid device Id supplied. No matching user device found"));
		logger.info("Removing refresh token associated with device [" + userDeviceOpt + "]");
		userDeviceOpt.map(UserDevice::getRefreshToken)
				.map(RefreshToken::getId)
				.ifPresent(refreshTokenService::deleteById);
	}
}
