package com.accolite.pru.health.AuthApp.service;

import java.time.Instant;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.accolite.pru.health.AuthApp.exception.InvalidTokenRequestException;
import com.accolite.pru.health.AuthApp.exception.ResourceNotFoundException;
import com.accolite.pru.health.AuthApp.model.PasswordResetToken;
import com.accolite.pru.health.AuthApp.model.User;

@Service
public class PasswordResetService {
	@Autowired
	PasswordResetTokenService passwordResetTokenService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	private static final Logger logger = Logger.getLogger(PasswordResetService.class);

	/**
	 * Verifying if the email id exists.Generate a password reset token and return
	 * the token to the controller
	 */
	public Optional<PasswordResetToken> generatePasswordResetToken(String mailId) {
		Boolean emailExists = userService.existsByEmail(mailId);
		if (!(emailExists)) {
			throw new ResourceNotFoundException("Address", "email id", mailId);
		}
		PasswordResetToken passwordResetToken = passwordResetTokenService.getPasswordResetToken(mailId);
		return Optional.ofNullable(passwordResetToken);
	}

	/**
	 * Checking the current time with expiry time of that particular token.If the
	 * current time doesn't exceed the expiry time, encode and update the password.
	 */
	public Optional<User> resetPassword(String newPassword, String token) {
		String encodedPassword;
		String mailId;
		User user;
		Boolean tokenExists = passwordResetTokenService.existsByToken(token);
		if (!(tokenExists)) {
			throw new ResourceNotFoundException("Token", "Token Id", token);
		}
		Instant currentTime = Instant.now();
		Instant expiryTime = passwordResetTokenService.findExpiryTimeByToken(token);
		if (currentTime.isBefore(expiryTime)) {
			user = passwordResetTokenService.findUserByToken(token);
			mailId = user.getEmail();
			encodedPassword = passwordEncoder.encode(newPassword);
			logger.info("password:-------------------" + newPassword + " " + encodedPassword + "CurrentTime"
					+ currentTime + "ExpiryTS:" + expiryTime);
			userService.resetPassword(mailId, encodedPassword);
		} else {
			throw new InvalidTokenRequestException("Reset Link Token", token,
					"Expired Link. Please issue a new request");
		}
		return Optional.ofNullable(user);
	}
}
