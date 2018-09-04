package com.accolite.pru.health.AuthApp.controller;

import com.accolite.pru.health.AuthApp.event.OnGenerateResetLinkEvent;
import com.accolite.pru.health.AuthApp.event.OnRegenerateEmailVerificationEvent;
import com.accolite.pru.health.AuthApp.event.OnUserAccountChangeEvent;
import com.accolite.pru.health.AuthApp.event.OnUserRegistrationCompleteEvent;
import com.accolite.pru.health.AuthApp.exception.InvalidTokenRequestException;
import com.accolite.pru.health.AuthApp.exception.PasswordResetException;
import com.accolite.pru.health.AuthApp.exception.PasswordResetLinkException;
import com.accolite.pru.health.AuthApp.exception.TokenRefreshException;
import com.accolite.pru.health.AuthApp.exception.UserLoginException;
import com.accolite.pru.health.AuthApp.exception.UserRegistrationException;
import com.accolite.pru.health.AuthApp.model.CustomUserDetails;
import com.accolite.pru.health.AuthApp.model.PasswordResetToken;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.payload.ApiResponse;
import com.accolite.pru.health.AuthApp.model.payload.JwtAuthenticationResponse;
import com.accolite.pru.health.AuthApp.model.payload.LoginRequest;
import com.accolite.pru.health.AuthApp.model.payload.PasswordResetLinkRequest;
import com.accolite.pru.health.AuthApp.model.payload.PasswordResetRequest;
import com.accolite.pru.health.AuthApp.model.payload.RegistrationRequest;
import com.accolite.pru.health.AuthApp.model.payload.TokenRefreshRequest;
import com.accolite.pru.health.AuthApp.model.token.EmailVerificationToken;
import com.accolite.pru.health.AuthApp.model.token.RefreshToken;
import com.accolite.pru.health.AuthApp.security.JwtTokenProvider;
import com.accolite.pru.health.AuthApp.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Api(value = "Authorization Rest API", description = "Defines endpoints that " +
		"can be hit only when the user is not logged in. It's not secured by default.")

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
	@ApiOperation(value = "Checks if the given email is in use")
	@GetMapping("/checkEmailInUse")
	public ResponseEntity<?> checkEmailInUse(@ApiParam(value = "Email id to check against") @RequestParam("email") String email) {
		Boolean emailExists = authService.emailAlreadyExists(email);
		return ResponseEntity.ok(new ApiResponse(emailExists.toString(), true));
	}

	/**
	 * Checks is a given username is in use or not.
	 */
	@ApiOperation(value = "Checks if the given username is in use")
	@GetMapping("/checkUsernameInUse")
	public ResponseEntity<?> checkUsernameInUse(@ApiParam(value = "Username to check against") @RequestParam(
			"username") String username) {
		Boolean usernameExists = authService.usernameAlreadyExists(username);
		return ResponseEntity.ok(new ApiResponse(usernameExists.toString(), true));
	}


	/**
	 * Entry point for the user log in. Return the jwt auth token and the refresh token
	 */
	@PostMapping("/login")
	@ApiOperation(value = "Logs the user in to the system and return the auth tokens")
	public ResponseEntity<?> authenticateUser(@ApiParam(value = "The LoginRequest payload") @Valid @RequestBody LoginRequest loginRequest) {
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
	 * Entry point for the user registration process. On successful registration,
	 * publish an event to generate email verification token
	 */
	@PostMapping("/register")
	@ApiOperation(value = "Registers the user and publishes an event to generate the email verification")
	public ResponseEntity<?> registerUser(@ApiParam(value = "The RegistrationRequest payload") @Valid @RequestBody RegistrationRequest registrationRequest,
			WebRequest request) {
		Optional<User> registeredUserOpt = authService.registerUser(registrationRequest);
		registeredUserOpt.orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(),
				"Missing user object in database"));
		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth" +
				"/registrationConfirmation");

		OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent =
				new OnUserRegistrationCompleteEvent(registeredUserOpt.get(), urlBuilder);
		applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);

		registeredUserOpt.ifPresent(user -> logger.info("Registered User returned [API[: " + user));
		return ResponseEntity.ok(new ApiResponse("User registered successfully. Check your email" +
				" for verification", true));
	}

	/**
	 * Receives the reset link request and publishes an event to send email id containing
	 * the reset link if the request is valid. In future the deeplink should open within
	 * the app itself.
	 */
	@PostMapping("/password/resetlink")
	@ApiOperation(value = "Receive the reset link request and publish event to send mail containing the password " +
			"reset link")
	public ResponseEntity<?> resetLink(@ApiParam(value = "The PasswordResetLinkRequest payload") @Valid @RequestBody PasswordResetLinkRequest passwordResetLinkRequest) {
		Optional<PasswordResetToken> passwordResetTokenOpt = authService
				.generatePasswordResetToken(passwordResetLinkRequest);
		passwordResetTokenOpt.orElseThrow(() -> new PasswordResetLinkException(passwordResetLinkRequest.getEmail(),
				"Couldn't create a valid token"));
		PasswordResetToken passwordResetToken = passwordResetTokenOpt.get();
		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/password/reset");
		OnGenerateResetLinkEvent generateResetLinkMailEvent = new OnGenerateResetLinkEvent(passwordResetToken,
				urlBuilder);
		applicationEventPublisher.publishEvent(generateResetLinkMailEvent);
		return ResponseEntity.ok(new ApiResponse("Password reset link sent successfully", true));
	}

	/**
	 * Receives a new passwordResetRequest and sends the acknowledgement after
	 * changing the password to the user's mail through the event.
	 */

	@PostMapping("/password/reset")
	@ApiOperation(value = "Reset the password after verification and publish an event to send the acknowledgement " +
			"email")
	public ResponseEntity<?> resetPassword(@ApiParam(value = "The PasswordResetRequest payload") @Valid @RequestBody PasswordResetRequest passwordResetRequest) {
		Optional<User> userOpt = authService.resetPassword(passwordResetRequest);
		userOpt.orElseThrow(() -> new PasswordResetException(passwordResetRequest.getToken(), "Error in resetting " +
				"password"));
		User changedUser = userOpt.get();
		OnUserAccountChangeEvent onPasswordChangeEvent = new OnUserAccountChangeEvent(changedUser, "Reset Password",
				"Changed Successfully");
		applicationEventPublisher.publishEvent(onPasswordChangeEvent);
		return ResponseEntity.ok(new ApiResponse("Password changed successfully", true));
	}

	/**
	 * Confirm the email verification token generated for the user during
	 * registration. If token is invalid or token is expired, report error.
	 */
	@GetMapping("/registrationConfirmation")
	@ApiOperation(value = "Confirms the email verification token that has been generated for the user during " +
			"registration")
	public ResponseEntity<?> confirmRegistration(@ApiParam(value = "the token that was sent to the user email") @RequestParam("token") String token) {
		Optional<User> verifiedUserOpt = authService.confirmEmailRegistration(token);
		verifiedUserOpt.orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", token,
				"Failed to confirm. Please generate a new email verification request"));
		return ResponseEntity.ok(new ApiResponse("User verified successfully", true));
	}

	/**
	 * Resend the email registration mail with an updated token expiry. Safe to
	 * assume that the user would always click on the last re-verification email and
	 * any attempts at generating new token from past (possibly archived/deleted)
	 * tokens should fail and report an exception.
	 */
	@GetMapping("/resendRegistrationToken")
	@ApiOperation(value = "Resend the email registration with an updated token expiry. Safe to " +
			"assume that the user would always click on the last re-verification email and " +
			"any attempts at generating new token from past (possibly archived/deleted)" +
			"tokens should fail and report an exception. ")
	public ResponseEntity<?> resendRegistrationToken(@ApiParam(value = "the initial token that was sent to the user " +
			" email after registration") @RequestParam("token") String existingToken) {
		Optional<EmailVerificationToken> newEmailTokenOpt = authService.recreateRegistrationToken(existingToken);
		newEmailTokenOpt.orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken,
				"User is already registered. No need to re-generate token"));

		User registeredUser = newEmailTokenOpt.map(EmailVerificationToken::getUser)
				.orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken,
						"No user associated with this request. Re-verification denied"));

		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/auth" + "/registrationConfirmation");
		OnRegenerateEmailVerificationEvent regenerateEmailVerificationEvent = new OnRegenerateEmailVerificationEvent(
				registeredUser, urlBuilder, newEmailTokenOpt.get());
		applicationEventPublisher.publishEvent(regenerateEmailVerificationEvent);

		return ResponseEntity.ok(new ApiResponse("Email verification resent successfully", true));
	}

	/**
	 * Refresh the expired jwt token using a refresh token for the specific device
	 * and return a new token to the caller
	 */
	@PostMapping("/refresh")
	@ApiOperation(value = "Refresh the expired jwt authentication by issuing a token refresh request and returns the" +
			"updated response tokens")
	public ResponseEntity<?> refreshJwtToken(@ApiParam(value = "The TokenRefreshRequest payload") @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
		Optional<String> updatedJwtToken = authService.refreshJwtToken(tokenRefreshRequest);
		updatedJwtToken.orElseThrow(() -> new TokenRefreshException(tokenRefreshRequest.getRefreshToken(),
				"Unexpected error during token refresh. Please logout and login again."));
		String refreshToken = tokenRefreshRequest.getRefreshToken();
		logger.info("Created new Jwt Auth token: " + updatedJwtToken);
		return ResponseEntity.ok(new JwtAuthenticationResponse(updatedJwtToken.get(), refreshToken,
				tokenProvider.getExpiryDuration()));
	}
}
