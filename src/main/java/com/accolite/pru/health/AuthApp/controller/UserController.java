package com.accolite.pru.health.AuthApp.controller;

import com.accolite.pru.health.AuthApp.model.payload.ApiResponse;
import com.accolite.pru.health.AuthApp.service.AuthService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

	@GetMapping("/checkEmailAvailability")
	public ResponseEntity<?> checkEmailAvailability(@RequestParam("email") String email) {
		Boolean emailExists = authService.emailAlreadyExists(email);
		return ResponseEntity.ok(new ApiResponse(emailExists.toString(), true));
	}

	@GetMapping("/checkUsernameAvailability")
	public ResponseEntity<?> checkUsernameAvailability(@RequestParam("username") String username) {
		Boolean usernameExists = authService.usernameAlreadyExists(username);
		return ResponseEntity.ok(new ApiResponse(usernameExists.toString(), true));
	}

	@GetMapping("/me")
	@Secured("ROLE_USER")
	public ResponseEntity<?> getUserProfile() {
		logger.info("Inside secured resource with user");
		return ResponseEntity.ok("Hello. This is about me");
	}

	@GetMapping("/admins")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<?> sayAlternateHello() {
		logger.info("Inside secured resource with admin");
		return ResponseEntity.ok("Hello. This is about admins");
	}

}
