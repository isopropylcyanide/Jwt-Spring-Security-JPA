package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.exception.InvalidTokenRequestException;
import com.accolite.pru.health.AuthApp.model.TokenStatus;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.token.EmailVerificationToken;
import com.accolite.pru.health.AuthApp.repository.EmailVerificationTokenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailVerificationTokenService {

	@Autowired
	private EmailVerificationTokenRepository emailVerificationTokenRepository;

	@Value("${app.token.email.verification.duration}")
	private Long emailVerificationTokenExpiryDuration;

	private static final Logger logger = Logger.getLogger(EmailVerificationTokenService.class);

	/**
	 * Create an email verification token and persist it in the database which will be
	 * verified by the user
	 */
	public void createVerificationToken(User user, String token) {
		EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
		emailVerificationToken.setToken(token);
		emailVerificationToken.setTokenStatus(TokenStatus.STATUS_PENDING);
		emailVerificationToken.setUser(user);
		emailVerificationToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
		logger.info("Generated Email verification token [" + emailVerificationToken + "]");
		emailVerificationTokenRepository.save(emailVerificationToken);
	}

	/**
	 * Updates an existing token in the database with a new expiration
	 */
	public EmailVerificationToken updateExistingTokenWithNameAndExpiry(EmailVerificationToken existingToken) {
		existingToken.setTokenStatus(TokenStatus.STATUS_PENDING);
		existingToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
		logger.info("Updated Email verification token [" + existingToken + "]");
		return save(existingToken);
	}

	/**
	 * Finds an email verification token by the @NaturalId token
	 */
	public Optional<EmailVerificationToken> findByToken(String token) {
		return emailVerificationTokenRepository.findByToken(token);
	}

	/**
	 * Finds an email verification token by the user one-one mapping
	 */
	public EmailVerificationToken findByUser(User user) {
		return emailVerificationTokenRepository.findByUser(user);
	}

	/**
	 * Saves an email verification token in the repository
	 */
	public EmailVerificationToken save(EmailVerificationToken emailVerificationToken) {
		return emailVerificationTokenRepository.save(emailVerificationToken);
	}

	/**
	 * Generates a new random UUID to be used as the token for email verification
	 */
	public String generateNewToken() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Verify whether the token provided has expired or not on the basis of the current
	 * server time and/or throw error otherwise
	 */
	public void verifyExpiration(EmailVerificationToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			throw new InvalidTokenRequestException("Email Verification Token", token.getToken(),
					"Expired token. Please issue a new request");
		}
	}

}
