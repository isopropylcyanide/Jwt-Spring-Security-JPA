package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.model.PasswordResetToken;
import com.accolite.pru.health.AuthApp.model.User;
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

	public void saveToken(PasswordResetToken passwordResetToken) {
		passwordResetTokenRepository.save(passwordResetToken);
	}

	/**
	 * Set the metadata for password reset token and return it to password reset
	 * service
	 */
	public PasswordResetToken getPasswordResetToken(String mailId) {
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		String token = Util.generateRefreshToken();
		passwordResetToken.setToken(token);
		Optional<User> userOpt = userService.findByEmail(mailId);
		User user = userOpt.get();
		passwordResetToken.setUser(user);
		logger.info(user);
		passwordResetToken.setExpiryTime(Instant.now().plusMillis(expiration));
		saveToken(passwordResetToken);
		return passwordResetToken;
	}

	public Instant findExpiryTimeByToken(String token) {
		return passwordResetTokenRepository.findExpiryTimeByToken(token);
	}

	public Optional<User> findUserByToken(String token) {
		Optional<User> userOpt = userService.findByToken(token);
		User user = userOpt.get();
		return userOpt;
	}

	public Boolean existsByToken(String token) {
		return passwordResetTokenRepository.existsByToken(token);
	}

	public Optional<PasswordResetToken> findByToken(String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

}
