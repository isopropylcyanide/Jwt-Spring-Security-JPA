package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.exception.InvalidTokenRequestException;
import com.accolite.pru.health.AuthApp.model.PasswordResetToken;
import com.accolite.pru.health.AuthApp.repository.PasswordResetTokenRepository;
import com.accolite.pru.health.AuthApp.util.Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class PasswordResetTokenService {

	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;

	private static final Logger logger = Logger.getLogger(PasswordResetTokenService.class);

	@Value("${app.token.password.reset.duration}")
	private Long expiration;

	@Autowired
	UserService userService;

	/**
	 * Saves the given password reset token
	 */
	public PasswordResetToken save(PasswordResetToken passwordResetToken) {
		return passwordResetTokenRepository.save(passwordResetToken);
	}

	/**
	 * Finds a token in the database given its naturalId
	 */
	public Optional<PasswordResetToken> findByToken(String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

	/**
	 * Creates and returns a new password token to which a user must be associated
	 */
	public PasswordResetToken createToken() {
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		String token = Util.generateRandomUuid();
		passwordResetToken.setToken(token);
		passwordResetToken.setExpiryDate(Instant.now().plusMillis(expiration));
		return passwordResetToken;
	}

	/**
	 * Verify whether the token provided has expired or not on the basis of the current
	 * server time and/or throw error otherwise
	 */
	public void verifyExpiration(PasswordResetToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			throw new InvalidTokenRequestException("Password Reset Token", token.getToken(),
					"Expired token. Please issue a new request");
		}
	}
}
