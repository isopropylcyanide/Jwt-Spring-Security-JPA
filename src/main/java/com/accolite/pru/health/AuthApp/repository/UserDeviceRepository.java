package com.accolite.pru.health.AuthApp.repository;

import com.accolite.pru.health.AuthApp.model.UserDevice;
import com.accolite.pru.health.AuthApp.model.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

	@Override
	Optional<UserDevice> findById(Long id);

	Optional<RefreshToken> findRefreshTokenById(Long id);

	Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken);

	Optional<UserDevice> findByDeviceId(String deviceId);
}
