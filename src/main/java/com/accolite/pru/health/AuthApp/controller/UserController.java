package com.accolite.pru.health.AuthApp.controller;

import com.accolite.pru.health.AuthApp.annotation.CurrentUser;
import com.accolite.pru.health.AuthApp.event.OnUserAccountChangeEvent;
import com.accolite.pru.health.AuthApp.exception.UpdatePasswordException;
import com.accolite.pru.health.AuthApp.model.CustomUserDetails;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.payload.ApiResponse;
import com.accolite.pru.health.AuthApp.model.payload.UpdatePasswordRequest;
import com.accolite.pru.health.AuthApp.service.AuthService;
import com.accolite.pru.health.AuthApp.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

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
	 * Gets the current user profile of the logged in user
	 */
	@GetMapping("/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserProfile(@CurrentUser CustomUserDetails currentUser) {
		logger.info("Inside secured resource with user");
		logger.info(currentUser.getEmail() + " has role: " + currentUser.getRoles());
		return ResponseEntity.ok("Hello. This is about me");
	}

	/**
	 * Returns all admins in the system. Requires Admin access
	 */
	@GetMapping("/admins")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllAdmins() {
		logger.info("Inside secured resource with admin");
		return ResponseEntity.ok("Hello. This is about admins");
	}


	/**
	 * Updates the password of the current logged in user
	 */
	@PostMapping("/password/update")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> updateUserPassword(@CurrentUser CustomUserDetails customUserDetails,
			@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
		User updatedUser = authService.updatePassword(customUserDetails, updatePasswordRequest)
				.orElseThrow(() -> new UpdatePasswordException("--Empty--", "No such user present."));

		OnUserAccountChangeEvent onUserPasswordChangeEvent =
				new OnUserAccountChangeEvent(updatedUser, "Update Password", "Changed successful");
		applicationEventPublisher.publishEvent(onUserPasswordChangeEvent);

		return ResponseEntity.ok(new ApiResponse("Password changed successfully", true));
	}
}
