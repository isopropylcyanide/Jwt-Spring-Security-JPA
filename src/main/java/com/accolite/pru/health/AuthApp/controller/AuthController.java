package com.accolite.pru.health.AuthApp.controller;

import com.accolite.pru.health.AuthApp.annotation.CurrentUser;
import com.accolite.pru.health.AuthApp.event.OnRegenerateEmailVerificationEvent;
import com.accolite.pru.health.AuthApp.event.OnUserRegistrationCompleteEvent;
import com.accolite.pru.health.AuthApp.exception.InvalidTokenRequestException;
import com.accolite.pru.health.AuthApp.exception.TokenRefreshException;
import com.accolite.pru.health.AuthApp.exception.UserLoginException;
import com.accolite.pru.health.AuthApp.exception.UserRegistrationException;
import com.accolite.pru.health.AuthApp.model.CustomUserDetails;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.payload.ApiResponse;
import com.accolite.pru.health.AuthApp.model.payload.JwtAuthenticationResponse;
import com.accolite.pru.health.AuthApp.model.payload.LogOutRequest;
import com.accolite.pru.health.AuthApp.model.payload.LoginRequest;
import com.accolite.pru.health.AuthApp.model.payload.RegistrationRequest;
import com.accolite.pru.health.AuthApp.model.payload.TokenRefreshRequest;
import com.accolite.pru.health.AuthApp.model.token.EmailVerificationToken;
import com.accolite.pru.health.AuthApp.model.token.RefreshToken;
import com.accolite.pru.health.AuthApp.security.JwtTokenProvider;
import com.accolite.pru.health.AuthApp.service.AuthService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private static final Logger logger = Logger.getLogger(AuthController.class);

	/**
	 * Checks is a given email is in use or not.
	 */
	@GetMapping("/checkEmailInUse")
	public ResponseEntity<?> checkEmailInUse(@RequestParam("email") String email) {
		Boolean emailExists = authService.emailAlreadyExists(email);
		return ResponseEntity.ok(new ApiResponse(emailExists.toString(), true));
	}

	/**
	 * Checks is a given username is in use or not.
	 */
	@GetMapping("/checkUsernameInUse")
	public ResponseEntity<?> checkUsernameInUse(@RequestParam("username") String username) {
		Boolean usernameExists = authService.usernameAlreadyExists(username);
		return ResponseEntity.ok(new ApiResponse(usernameExists.toString(), true));
	}


	/**
	 * Entry point for the user log in. Return the jwt auth token and the refresh token
	 */
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Optional<Authentication> authenticationOpt = authService.authenticateUser(loginRequest);
		authenticationOpt.orElseThrow(() -> new UserLoginException("Couldn't login user [" + loginRequest + "]"));
		Authentication authentication = authenticationOpt.get();
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

		logger.info("Logged in User returned [API]: " + customUserDetails.getUsername());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		Optional<RefreshToken> refreshTokenOpt = authService.createAndPersistRefreshTokenForDevice(authentication,
				loginRequest);

		refreshTokenOpt.orElseThrow(() -> new UserLoginException("Couldn't create refresh token for: [" + loginRequest + "]"));
		String refreshToken = refreshTokenOpt.map(RefreshToken::getToken).get();
		String jwtToken = authService.generateToken(customUserDetails);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, refreshToken,
				tokenProvider.getExpiryDuration()));
	}

	/**
	 * Entry point for the user registration process. On successful registration, publish
	 * an event to generate email verification token
	 */
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest,
			WebRequest request) {
		Optional<User> registeredUserOpt = authService.registerUser(registrationRequest);
		registeredUserOpt.orElseThrow(() -> new UserRegistrationException("Couldn't register user [" + registrationRequest +
				"]"));
		User registeredUser = registeredUserOpt.get();
		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth" +
				"/registrationConfirmation");

		OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent =
				new OnUserRegistrationCompleteEvent(registeredUser, urlBuilder);
		applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);

		logger.info("Registered User returned [API[: " + registeredUser);
		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/api/user/me")
				.buildAndExpand(registeredUser.getEmail()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse("User registered successfully. Check your email" +
				" for verification", true));
	}


	/**
	 * Confirm the email verification token generated for the user during registration. If
	 * token is invalid or token is expired, report error.
	 */
	@GetMapping("/registrationConfirmation")
	public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token) {
		Optional<User> verifiedUserOpt = authService.confirmEmailRegistration(token);
		verifiedUserOpt.orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", token,
				"Failed to confirm. Please generate a new email verification request"));

		User verifiedUser = verifiedUserOpt.get();
		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/api/user/me")
				.buildAndExpand(verifiedUser.getEmail()).toUri();
		return ResponseEntity.created(location).body(new ApiResponse("User verified successfully", true));
	}

	/**
	 * Resend the email registration mail with an updated token expiry.
	 * Safe to assume that the user would always click on the last re-verification email
	 * and any attempts at generating new token from past (possibly archived/deleted) tokens
	 * should fail and report an exception.
	 */
	@GetMapping("/resendRegistrationToken")
	public ResponseEntity<?> resendRegistrationToken(@RequestParam("token") String existingToken) {
		Optional<EmailVerificationToken> newEmailTokenOpt = authService.recreateRegistrationToken(existingToken);
		newEmailTokenOpt.orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken,
				"User is already registered. No need to re-generate token"));

		User registeredUser = newEmailTokenOpt.map(EmailVerificationToken::getUser)
				.orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken,
						"No user associated with this request. Re-verification denied"));

		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth" +
				"/registrationConfirmation");
		OnRegenerateEmailVerificationEvent regenerateEmailVerificationEvent =
				new OnRegenerateEmailVerificationEvent(registeredUser, urlBuilder, newEmailTokenOpt.get());
		applicationEventPublisher.publishEvent(regenerateEmailVerificationEvent);

		return ResponseEntity.ok(new ApiResponse("Email verification resent successfully", true));
	}


	/**
	 * Refresh the expired jwt token using a refresh token for the specific device
	 * and return a new token to the caller
	 */
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshJwtToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
		Optional<String> updatedJwtToken = authService.refreshJwtToken(tokenRefreshRequest);
		updatedJwtToken.orElseThrow(() -> new TokenRefreshException(tokenRefreshRequest.getRefreshToken(),
				"Unexpected error during token refresh. Please logout and login again."));
		String refreshToken = tokenRefreshRequest.getRefreshToken();
		logger.info("Created new Jwt Auth token: " + updatedJwtToken);
		return ResponseEntity.ok(new JwtAuthenticationResponse(updatedJwtToken.get(), refreshToken,
				tokenProvider.getExpiryDuration()));
	}


	/**
	 * Log the user out from the app/device. Release the refresh token associated with the
	 * user device.
	 */
	@GetMapping("/logout")
	public ResponseEntity<?> logoutUser(@CurrentUser CustomUserDetails customUserDetails,
			@Valid @RequestBody LogOutRequest logOutRequest) {
		authService.logoutUser(customUserDetails, logOutRequest);
		return ResponseEntity.ok(new ApiResponse("Log out successful", true));
	}
}
