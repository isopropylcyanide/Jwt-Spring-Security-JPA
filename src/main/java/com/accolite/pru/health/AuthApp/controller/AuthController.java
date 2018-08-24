package com.accolite.pru.health.AuthApp.controller;

import com.accolite.pru.health.AuthApp.event.OnUserRegistrationCompleteEvent;
import com.accolite.pru.health.AuthApp.exception.UserLoginException;
import com.accolite.pru.health.AuthApp.exception.UserRegistrationException;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.payload.ApiResponse;
import com.accolite.pru.health.AuthApp.model.payload.JwtAuthenticationResponse;
import com.accolite.pru.health.AuthApp.model.payload.LoginRequest;
import com.accolite.pru.health.AuthApp.model.payload.RegistrationRequest;
import com.accolite.pru.health.AuthApp.security.JwtTokenProvider;
import com.accolite.pru.health.AuthApp.service.AuthService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	private static final Logger logger = Logger.getLogger(AuthController.class);

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Optional<Authentication> authenticationOpt = authService.authenticateUser(loginRequest);
		authenticationOpt.orElseThrow(() -> new UserLoginException("Couldn't login user [" + loginRequest + "]"));
		Authentication authentication = authenticationOpt.get();

		logger.info("Logged in User returned [API]: " + authentication.getName());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest,
			WebRequest request) {
		Optional<User> registeredUserOpt = authService.registerUser(registrationRequest);
		registeredUserOpt.orElseThrow(() -> new UserRegistrationException("Couldn't register user [" + registrationRequest +
				"]"));
		User registeredUser = registeredUserOpt.get();
		OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent =
				new OnUserRegistrationCompleteEvent(registeredUser, request.getContextPath());
		applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);

		logger.info("Registered User returned [API[: " + registeredUser);
		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/api/users/{email}")
				.buildAndExpand(registeredUser.getEmail()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse("User registered successfully", true));
	}
}
