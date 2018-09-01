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
			throw new ResourceNotFoundException("Email", "Address", mailId);
		}
		PasswordResetToken passwordResetToken = passwordResetTokenService.getPasswordResetToken(mailId);
		return Optional.ofNullable(passwordResetToken);
	}

	/**
	 * Checking if the token is valid
	 */
	public void verifyTokenExpiration(Instant expiryTime, String token) {
		Instant currentTime = Instant.now();
		if (currentTime.isAfter(expiryTime)) {
			throw new InvalidTokenRequestException("Reset Link Token", token,
					"Expired Link. Please issue a new request");
		}
	}

	/**
	 * Checking the current time with expiry time of that particular token.If the
	 * current time doesn't exceed the expiry time, encode and update the password.
	 */
	public Optional<User> resetPassword(String newPassword, String token) {
		String encodedPassword;
		String mailId;
		Optional<User> userOpt;
		User user = null;
		Optional<PasswordResetToken> passwordResetTokenOpt;
		passwordResetTokenOpt = passwordResetTokenService.findByToken(token);
		passwordResetTokenOpt.orElseThrow(() -> new ResourceNotFoundException("Token", "Token Id", token));
		PasswordResetToken passwordResetToken;
		if (passwordResetTokenOpt.isPresent()) {
			passwordResetToken = passwordResetTokenOpt.get();
			Instant expiryTime = passwordResetToken.getExpiryTime();
			verifyTokenExpiration(expiryTime, token);
			userOpt = passwordResetTokenService.findUserByToken(token);
			if (userOpt.isPresent()) {
				user = userOpt.get();
				mailId = user.getEmail();
				encodedPassword = passwordEncoder.encode(newPassword);
				userService.resetPassword(mailId, encodedPassword);
			}
		}
		return Optional.ofNullable(user);
	}
}
