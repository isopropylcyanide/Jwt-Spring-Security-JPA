package com.accolite.pru.health.AuthApp.controller;

import com.accolite.pru.health.AuthApp.annotation.CurrentUser;
import com.accolite.pru.health.AuthApp.event.OnUserAccountChangeEvent;
import com.accolite.pru.health.AuthApp.exception.UpdatePasswordException;
import com.accolite.pru.health.AuthApp.model.CustomUserDetails;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.payload.ApiResponse;
import com.accolite.pru.health.AuthApp.model.payload.LogOutRequest;
import com.accolite.pru.health.AuthApp.model.payload.UpdatePasswordRequest;
import com.accolite.pru.health.AuthApp.service.AuthService;
import com.accolite.pru.health.AuthApp.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@Api(value = "User Rest API", description = "Defines endpoints for the logged in user. It's " +
		"secured by default ")


public class UserController {

	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	/**
	 * Gets the current user profile of the logged in user
	 */
	@GetMapping("/me")
	@PreAuthorize("hasRole('USER')")
	@ApiOperation(value = "Returns the current user profile")
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
	@ApiOperation(value = "Returns the list of configured admins. Requires ADMIN Access")
	public ResponseEntity<?> getAllAdmins() {
		logger.info("Inside secured resource with admin");
		return ResponseEntity.ok("Hello. This is about admins");
	}


	/**
	 * Updates the password of the current logged in user
	 */
	@PostMapping("/password/update")
	@PreAuthorize("hasRole('USER')")
	@ApiOperation(value = "Allows the user to change his password once logged in by supplying the correct current " +
			"password")
	public ResponseEntity<?> updateUserPassword(@CurrentUser CustomUserDetails customUserDetails,
			@ApiParam(value = "The UpdatePasswordRequest payload") @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
		User updatedUser = authService.updatePassword(customUserDetails, updatePasswordRequest)
				.orElseThrow(() -> new UpdatePasswordException("--Empty--", "No such user present."));

		OnUserAccountChangeEvent onUserPasswordChangeEvent =
				new OnUserAccountChangeEvent(updatedUser, "Update Password", "Change successful");
		applicationEventPublisher.publishEvent(onUserPasswordChangeEvent);

		return ResponseEntity.ok(new ApiResponse("Password changed successfully", true));
	}

	/**
	 * Log the user out from the app/device. Release the refresh token associated with the
	 * user device.
	 */
	@PostMapping("/logout")
	@ApiOperation(value = "Logs the specified user device and clears the refresh tokens associated with it")
	public ResponseEntity<?> logoutUser(@CurrentUser CustomUserDetails customUserDetails,
			@ApiParam(value = "The LogOutRequest payload") @Valid @RequestBody LogOutRequest logOutRequest) {
		userService.logoutUser(customUserDetails, logOutRequest);
		return ResponseEntity.ok(new ApiResponse("Log out successful", true));
	}
}
