package com.accolite.pru.health.AuthApp.service;

import java.sql.Timestamp;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.accolite.pru.health.AuthApp.exception.ResourceNotFoundException;
import com.accolite.pru.health.AuthApp.model.PasswordResetToken;
import com.accolite.pru.health.AuthApp.repository.ForgotPasswordRepository;

@Service
public class ForgotPasswordService {
	@Autowired
	ForgotPasswordRepository forgotPasswordDAO;

	@Autowired
	PasswordResetTokenService passwordResetTokenService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Logger logger = Logger.getLogger(ForgotPasswordService.class);

	PasswordResetToken passwordResetToken;

	@Value("${app.token.email.verification.duration}")
	private long expiration;

	private long getCurrentTime() {
		return System.currentTimeMillis();
	}

	private String generateToken() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Generate Password Reset Token.
	 *
	 * 1) Verifying if the email id exists 2) Generating a token and its expiry time
	 * 3) Set the metadata required in the token and return
	 * 
	 * @param mailId
	 *            the mail id
	 * @return the password reset token
	 */
	public PasswordResetToken generatePasswordResetToken(String mailId) {
		if (!(forgotPasswordDAO.existsByEmail(mailId))) {
			throw new ResourceNotFoundException("User", "email", mailId);
		}
		passwordResetToken = new PasswordResetToken();
		String token = generateToken();
		passwordResetToken.setExpiration(expiration);
		long currentTime = getCurrentTime();
		long expiryTime = currentTime + passwordResetToken.getExpiration();
		Timestamp expiryTimeStamp = new Timestamp(expiryTime);
		logger.info("-----------------time-----" + expiryTimeStamp + " expiry:" + passwordResetToken.getExpiration());
		passwordResetToken.setToken(token);
		passwordResetToken.setEmail(mailId);
		passwordResetToken.setExpiryTime(expiryTimeStamp);
		passwordResetTokenService.addToken(passwordResetToken);
		return passwordResetToken;
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
	public void resetPassword(String newPassword, String token) {
		String encodedPassword;
		long currentTime = getCurrentTime();
		Timestamp currentTimeStamp = new Timestamp(currentTime);
		Timestamp expiryTime = passwordResetTokenService.findExpiryTimeByToken(token);
		if (currentTimeStamp.before(expiryTime)) {
			String mailId = passwordResetTokenService.findEmailByToken(token);
			encodedPassword = passwordEncoder.encode(newPassword);
			logger.info("password:-------------------" + newPassword + " " + encodedPassword + "CurrentTime"
					+ currentTimeStamp + "ExpiryTS:" + expiryTime);
			forgotPasswordDAO.resetPassword(mailId, encodedPassword);
		}
		// Check if we need to throw an exception in the else
	}
}
