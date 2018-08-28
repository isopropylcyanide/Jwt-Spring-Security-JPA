package com.accolite.pru.health.AuthApp.repository;

import com.accolite.pru.health.AuthApp.model.UserDevice;
import com.accolite.pru.health.AuthApp.model.token.JwtRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Long> {

	@Override
	Optional<JwtRefreshToken> findById(Long id);

	Optional<String> findTokenById(Long id);

	Optional<JwtRefreshToken> findByNaturalId(String token);

	Optional<UserDevice> findUserDeviceById(Long id);

	Optional<UserDevice> findUserDeviceByNaturalId(String token);
}
