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
import com.accolite.pru.health.AuthApp.repository.ForgotPasswordRepository;

@Service
public class PasswordResetService {
	@Autowired
	ForgotPasswordRepository forgotPasswordRepository;

	@Autowired
	PasswordResetTokenService passwordResetTokenService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Logger logger = Logger.getLogger(PasswordResetService.class);

	/**
	 * Generate Password Reset Token.
	 *
	 * 1) Verifying if the email id exists 2) Generate a password reset token and
	 * return
	 * 
	 * @param mailId
	 *            the mail id
	 * @return the password reset token
	 */
	public Optional<PasswordResetToken> generatePasswordResetToken(String mailId) {
		Boolean emailExists = forgotPasswordRepository.existsByEmail(mailId);
		if (!(emailExists)) {
			throw new ResourceNotFoundException("User", "email", mailId);
		}
		PasswordResetToken passwordResetToken = passwordResetTokenService.getPasswordResetToken(mailId);
		return Optional.ofNullable(passwordResetToken);
	}

	/**
	 * Reset password.
	 *
	 * 1) Checking the current time with expiry time of that particular token. 2) If
	 * the current time doesn't exceed the expiry time, encode and update the
	 * password.
	 * 
	 * @param newPassword
	 *            the new password
	 * @param token
	 *            the token
	 */
	public Optional<String> resetPassword(String newPassword, String token) {
		String encodedPassword;
		String mailId;
		Boolean tokenExists = passwordResetTokenService.existsByToken(token);
		if (!(tokenExists)) {
			throw new ResourceNotFoundException("Token", "Token Id", token);
		}
		Instant currentTime = Instant.now();
		Instant expiryTime = passwordResetTokenService.findExpiryTimeByToken(token);
		if (currentTime.isBefore(expiryTime)) {
			User user = passwordResetTokenService.findUserByToken(token);
			mailId = user.getEmail();
			encodedPassword = passwordEncoder.encode(newPassword);
			logger.info("password:-------------------" + newPassword + " " + encodedPassword + "CurrentTime"
					+ currentTime + "ExpiryTS:" + expiryTime);
			forgotPasswordRepository.resetPassword(mailId, encodedPassword);
		} else {
			throw new InvalidTokenRequestException("Reset Link Token", token,
					"Expired Link. Please issue a new request");
		}
		return Optional.ofNullable(mailId);
	}
}
