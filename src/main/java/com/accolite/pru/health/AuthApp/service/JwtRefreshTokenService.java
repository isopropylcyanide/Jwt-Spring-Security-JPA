package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.model.TokenStatus;
import com.accolite.pru.health.AuthApp.model.UserDevice;
import com.accolite.pru.health.AuthApp.model.token.JwtRefreshToken;
import com.accolite.pru.health.AuthApp.repository.JwtRefreshTokenRepository;
import com.accolite.pru.health.AuthApp.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class JwtRefreshTokenService {

	private JwtRefreshTokenRepository jwtRefreshTokenRepository;

	@Value("${app.token.refresh.duration}")
	private Long refreshTokenDurationMs;

	/**
	 * Find a refresh token based on the id
	 */
	public Optional<JwtRefreshToken> findById(Long id) {
		return jwtRefreshTokenRepository.findById(id);
	}

	/**
	 * Find the refresh token string based on the id
	 */
	public Optional<String> findTokenById(Long id) {
		return jwtRefreshTokenRepository.findTokenById(id);
	}

	/**
	 * Find a refresh token based on the natural id i.e the token itself
	 */
	public Optional<JwtRefreshToken> findByToken(String token) {
		return jwtRefreshTokenRepository.findByToken(token);
	}

	/**
	 * Finds the user device the refreshed token is attached to using the id
	 */
	public Optional<UserDevice> findUserDeviceById(Long id) {
		return jwtRefreshTokenRepository.findUserDeviceById(id);
	}

	/**
	 * Finds the user device the refreshed token is attached to with the natural id
	 */
	public Optional<UserDevice> findUserDeviceByToken(String token) {
		return jwtRefreshTokenRepository.findUserDeviceByToken(token);
	}

	/**
	 * Persist the updated jwtRefreshToken instance to database
	 */
	public JwtRefreshToken save(JwtRefreshToken jwtRefreshToken) {
		return jwtRefreshTokenRepository.save(jwtRefreshToken);
	}


	/**
	 * Creates and returns a new refresh token
	 */
	public JwtRefreshToken createRefreshToken() {
		JwtRefreshToken jwtRefreshToken = new JwtRefreshToken();
		jwtRefreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		jwtRefreshToken.setToken(Util.generateRefreshToken());
		jwtRefreshToken.setTokenStatus(TokenStatus.STATUS_PENDING);
		return jwtRefreshToken;
	}
}
