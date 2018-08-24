package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.model.TokenStatus;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.token.EmailVerificationToken;
import com.accolite.pru.health.AuthApp.repository.EmailVerificationTokenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

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

	public Optional<EmailVerificationToken> findByToken(String token) {
		return emailVerificationTokenRepository.findByToken(token);
	}

	public List<EmailVerificationToken> findByUser(User user) {
		return emailVerificationTokenRepository.findByUser(user);
	}

	public void save(EmailVerificationToken emailVerificationToken) {
		emailVerificationTokenRepository.save(emailVerificationToken);
	}
}
