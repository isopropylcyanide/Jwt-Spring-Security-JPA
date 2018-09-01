package com.accolite.pru.health.AuthApp.repository;

import com.accolite.pru.health.AuthApp.model.UserDevice;
import com.accolite.pru.health.AuthApp.model.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	@Override
	Optional<RefreshToken> findById(Long id);

	Optional<String> findTokenById(Long id);

	Optional<RefreshToken> findByToken(String token);

	Optional<UserDevice> findUserDeviceById(Long id);

	Optional<UserDevice> findUserDeviceByToken(String token);
}
