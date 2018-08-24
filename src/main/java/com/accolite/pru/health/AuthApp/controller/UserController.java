package com.accolite.pru.health.AuthApp.controller;

import com.accolite.pru.health.AuthApp.annotation.CurrentUser;
import com.accolite.pru.health.AuthApp.exception.ResourceNotFoundException;
import com.accolite.pru.health.AuthApp.model.CustomUserDetails;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.payload.ApiResponse;
import com.accolite.pru.health.AuthApp.service.AuthService;
import com.accolite.pru.health.AuthApp.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private UserService userService;

	@GetMapping("/checkEmailInUse")
	public ResponseEntity<?> checkEmailInUse(@RequestParam("email") String email) {
		Boolean emailExists = authService.emailAlreadyExists(email);
		return ResponseEntity.ok(new ApiResponse(emailExists.toString(), true));
	}

	@GetMapping("/checkUsernameInUse")
	public ResponseEntity<?> checkUsernameInUse(@RequestParam("username") String username) {
		Boolean usernameExists = authService.usernameAlreadyExists(username);
		return ResponseEntity.ok(new ApiResponse(usernameExists.toString(), true));
	}

	@GetMapping("/me")
	public ResponseEntity<?> getUserProfile(@CurrentUser CustomUserDetails customUserDetails) {
		logger.info("Inside secured resource with user");
		logger.info(customUserDetails.getEmail() + " has role: " + customUserDetails.getRoles());
		return ResponseEntity.ok("Hello. This is about me");
	}

	@GetMapping("/admins")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllAdmins() {
		logger.info("Inside secured resource with admin");
		return ResponseEntity.ok("Hello. This is about admins");
	}

	@GetMapping()
	public ResponseEntity<?> getUser(@RequestParam("email") String email) {
		User user = userService.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
		return ResponseEntity.ok(user);
	}

}
