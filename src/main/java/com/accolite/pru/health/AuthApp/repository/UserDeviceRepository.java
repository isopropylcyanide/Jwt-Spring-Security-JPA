package com.accolite.pru.health.AuthApp.repository;

import com.accolite.pru.health.AuthApp.model.UserDevice;
import com.accolite.pru.health.AuthApp.model.token.JwtRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

	@Override
	Optional<UserDevice> findById(Long id);

	Optional<JwtRefreshToken> findRefreshTokenById(Long id);
}
