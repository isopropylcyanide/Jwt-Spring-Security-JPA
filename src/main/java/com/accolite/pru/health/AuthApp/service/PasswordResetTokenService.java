package com.accolite.pru.health.AuthApp.service;

import java.time.Instant;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.accolite.pru.health.AuthApp.model.PasswordResetToken;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.repository.PasswordResetTokenRepository;
import com.accolite.pru.health.AuthApp.util.Util;

@Service
public class PasswordResetTokenService {

	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;

	private static final Logger logger = Logger.getLogger(PasswordResetTokenService.class);

	@Value("${app.token.password.reset.duration}")
	private Long expiration;

	@Autowired
	UserService userService;

	public void addToken(PasswordResetToken passwordResetToken) {
		passwordResetTokenRepository.save(passwordResetToken);
	}

	/**
	 * Gets the password reset token.
	 *
	 * 1) Set the metadata for token and return it
	 * 
	 * @param mailId
	 *            the mail id
	 * @return the password reset token
	 */
	public PasswordResetToken getPasswordResetToken(String mailId) {
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		Util util = new Util();
		String token = util.generateResetLinkToken();
		passwordResetToken.setToken(token);
		Optional<User> userOpt = userService.findByEmail(mailId);
		User user = userOpt.get();
		passwordResetToken.setUser(user);
		passwordResetToken.setExpiryTime(Instant.now().plusMillis(expiration));
		addToken(passwordResetToken);
		return passwordResetToken;
	}

	public Instant findExpiryTimeByToken(String token) {
		return passwordResetTokenRepository.findExpiryTimeByToken(token);
	}

	public User findUserByToken(String token) {
		Long id = passwordResetTokenRepository.findUserIdByToken(token);
		Optional<User> userOpt = userService.findById(id);
		User user = userOpt.get();
		return user;
	}

	public Boolean existsByToken(String token) {
		return passwordResetTokenRepository.existsByToken(token);
	}

}
