package com.accolite.pru.health.AuthApp.controller;

import com.accolite.pru.health.AuthApp.model.payload.ApiResponse;
import com.accolite.pru.health.AuthApp.service.AuthService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

	@GetMapping("/all")
	public ResponseEntity<?> sayHello() {
		logger.info("Inside unsecured resource");
		return ResponseEntity.ok("Hello. This is a not a secured resource");
	}

	@GetMapping("/secured/alternate")
	public ResponseEntity<?> sayAlternateHello() {
		logger.info("Inside unsecured resource which is open for any role");
		return ResponseEntity.ok("Hello. This is an alternate secured resource");
	}

}
