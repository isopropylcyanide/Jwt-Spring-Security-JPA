package com.accolite.pru.health.AuthApp.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accolite.pru.health.AuthApp.model.PasswordResetToken;
import com.accolite.pru.health.AuthApp.repository.PasswordResetTokenRepository;

@Service
public class PasswordResetTokenService {

	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;

	public void addToken(PasswordResetToken passwordResetToken) {
		passwordResetTokenRepository.save(passwordResetToken);
	}

	public Timestamp findExpiryTimeByToken(String token) {
		return passwordResetTokenRepository.findExpiryTimeByToken(token);
	}

	public String findEmailByToken(String token) {
		return passwordResetTokenRepository.findEmailByToken(token);
	}

}
