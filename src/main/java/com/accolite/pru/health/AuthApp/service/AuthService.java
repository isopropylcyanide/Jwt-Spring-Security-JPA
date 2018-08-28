package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.exception.AppException;
import com.accolite.pru.health.AuthApp.exception.InvalidTokenRequestException;
import com.accolite.pru.health.AuthApp.exception.ResourceAlreadyInUseException;
import com.accolite.pru.health.AuthApp.exception.ResourceNotFoundException;
import com.accolite.pru.health.AuthApp.exception.UpdatePasswordException;
import com.accolite.pru.health.AuthApp.model.CustomUserDetails;
import com.accolite.pru.health.AuthApp.model.Role;
import com.accolite.pru.health.AuthApp.model.RoleName;
import com.accolite.pru.health.AuthApp.model.TokenStatus;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.UserDevice;
import com.accolite.pru.health.AuthApp.model.payload.LoginRequest;
import com.accolite.pru.health.AuthApp.model.payload.RegistrationRequest;
import com.accolite.pru.health.AuthApp.model.payload.UpdatePasswordRequest;
import com.accolite.pru.health.AuthApp.model.token.EmailVerificationToken;
import com.accolite.pru.health.AuthApp.model.token.JwtRefreshToken;
import com.accolite.pru.health.AuthApp.security.JwtTokenProvider;
import com.accolite.pru.health.AuthApp.util.Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	private static final Logger logger = Logger.getLogger(AuthService.class);

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private JwtRefreshTokenService jwtRefreshTokenService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private EmailVerificationTokenService emailVerificationTokenService;

	@Autowired
	private UserDeviceService userDeviceService;

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
		newUser.setEmailVerified(false);
		User registeredNewUser = userService.save(newUser);
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
		return userService.existsByEmail(email);
	}

	/**
	 * Checks if the given email already exists in the database repository or not
	 * @return true if the email exists else false
	 */
	public Boolean usernameAlreadyExists(String username) {
		return userService.existsByUsername(username);
	}


	/**
	 * Authenticate user and log them in given a loginRequest
	 */
	public Optional<Authentication> authenticateUser(LoginRequest loginRequest) {
		return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
				loginRequest.getPassword())));
	}

	/**
	 * Confirms the user verification based on the token expiry and mark the user as active.
	 * If user is already registered, save the unnecessary database calls.
	 */
	public Optional<User> confirmRegistration(String emailToken) {
		Optional<EmailVerificationToken> emailVerificationTokenOpt =
				emailVerificationTokenService.findByToken(emailToken);
		emailVerificationTokenOpt.orElseThrow(() ->
				new ResourceNotFoundException("Token", "Email verification", emailToken));

		Optional<User> registeredUser = emailVerificationTokenOpt.map(EmailVerificationToken::getUser);
		//if user is already verified
		Boolean userAlreadyVerified =
				emailVerificationTokenOpt.map(EmailVerificationToken::getUser)
						.map(User::getEmailVerified).filter(Util::isTrue).orElse(false);

		if (userAlreadyVerified) {
			logger.info("User [" + registeredUser + "] already registered.");
			return registeredUser;
		}
		Optional<Instant> validEmailTokenOpt =
				emailVerificationTokenOpt.map(EmailVerificationToken::getExpiryDate)
						.filter(dt -> dt.compareTo(Instant.now()) >= 0);

		validEmailTokenOpt.orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", emailToken,
				"Expired token. Please issue a new request"));

		emailVerificationTokenOpt.ifPresent(token -> {
			token.setTokenStatus(TokenStatus.STATUS_CONFIRMED);
			emailVerificationTokenService.save(token);
			User user = registeredUser.get();
			user.setEmailVerified(true);
			userService.save(user);
		});
		return registeredUser;
	}

	/**
	 * Attempt to regenerate a new email verification token given a valid
	 * previous expired token. If the previous token is valid, increase its expiry
	 * else update the token value and add a new expiration.
	 */
	public Optional<EmailVerificationToken> recreateRegistrationToken(String existingToken) {
		Optional<EmailVerificationToken> emailVerificationTokenOpt =
				emailVerificationTokenService.findByToken(existingToken);
		emailVerificationTokenOpt.orElseThrow(() ->
				new ResourceNotFoundException("Token", "Existing email verification", existingToken));
		Boolean userAlreadyVerified =
				emailVerificationTokenOpt.map(EmailVerificationToken::getUser)
						.map(User::getEmailVerified).filter(Util::isTrue).orElse(false);
		if (userAlreadyVerified) {
			return Optional.empty();
		}
		return emailVerificationTokenOpt.map(emailVerificationTokenService::updateExistingTokenWithNameAndExpiry);
	}

	/**
	 * Validates the password of the current logged in user with the given password
	 */
	public Boolean currentPasswordMatches(User currentUser, String password) {
		return passwordEncoder.matches(password, currentUser.getPassword());
	}

	/**
	 * Updates the password of the current logged in user
	 */
	public Optional<User> updatePassword(CustomUserDetails customUserDetails,
			UpdatePasswordRequest updatePasswordRequest) {
		User currentUser = userService.getLoggedInUser(customUserDetails.getEmail());

		if (!currentPasswordMatches(currentUser, updatePasswordRequest.getOldPassword())) {
			logger.info("Current password is invalid for [" + currentUser.getPassword() + "]");
			throw new UpdatePasswordException(currentUser.getEmail(), "Invalid current password");
		}
		String newPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
		currentUser.setPassword(newPassword);
		userService.save(currentUser);
		return Optional.ofNullable(currentUser);
	}

	/**
	 * Generates a JWT token for the validated client
	 */
	public String generateToken(Authentication authentication, JwtRefreshToken jwtRefreshToken) {
		return tokenProvider.generateToken(authentication, jwtRefreshToken);
	}

	/**
	 * Creates and persists the refresh token for the user device. If device exists
	 * already, we don't care. Unused devices with expired tokens should be cleaned
	 * with a cron job. The generated token would be encapsulated within the jwt.
	 */
	public Optional<JwtRefreshToken> createAndPersistRefreshTokenForDevice(Authentication authentication,
			LoginRequest loginRequest) {
		User currentUser = (User) authentication.getPrincipal();
		JwtRefreshToken jwtRefreshToken = jwtRefreshTokenService.createRefreshToken();
		UserDevice userDevice = userDeviceService.createUserDevice(loginRequest.getDeviceInfo());
		userDevice.setUser(currentUser);
		userDevice.setRefreshToken(jwtRefreshToken);
		jwtRefreshToken.setUserDevice(userDevice);
		userDeviceService.save(userDevice);
		jwtRefreshTokenService.save(jwtRefreshToken);
		return Optional.ofNullable(jwtRefreshToken);
	}
}
