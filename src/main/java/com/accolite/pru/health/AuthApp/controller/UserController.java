package com.accolite.pru.health.AuthApp.controller;

import com.accolite.pru.health.AuthApp.model.LoginRequest;
import com.accolite.pru.health.AuthApp.model.RegisterUserRequest;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private Logger logger;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest newRegisterUserRequest){
		Optional<User> registeredUser = userService.registerUser(newRegisterUserRequest);
		logger.info("Registered User returned [API[: " + registeredUser);
		return ResponseEntity.ok(registeredUser.map(User::getId));
	}


	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
		Optional<User> loggedInUser = userService.loginUser(loginRequest);
		logger.info("Logged in User returned [API[: " + loggedInUser);
		return ResponseEntity.ok(loggedInUser.map(User::getId));
	}
}
