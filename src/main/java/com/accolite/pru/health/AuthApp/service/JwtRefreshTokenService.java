package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.model.UserDevice;
import com.accolite.pru.health.AuthApp.model.token.JwtRefreshToken;
import com.accolite.pru.health.AuthApp.repository.JwtRefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtRefreshTokenService {

	private JwtRefreshTokenRepository jwtRefreshTokenRepository;

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
	public Optional<JwtRefreshToken> findByNaturalId(String token) {
		return jwtRefreshTokenRepository.findByNaturalId(token);
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
	public Optional<UserDevice> findUserDeviceByNaturalId(String token) {
		return jwtRefreshTokenRepository.findUserDeviceByNaturalId(token);
	}

	/**
	 * Persist the updated jwtRefreshToken instance to database
	 */
	public JwtRefreshToken save(JwtRefreshToken jwtRefreshToken) {
		return jwtRefreshTokenRepository.save(jwtRefreshToken);
	}

}
