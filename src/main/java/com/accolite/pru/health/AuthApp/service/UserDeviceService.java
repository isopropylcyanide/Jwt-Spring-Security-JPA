package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.model.UserDevice;
import com.accolite.pru.health.AuthApp.model.payload.LoginDeviceInfo;
import com.accolite.pru.health.AuthApp.model.token.JwtRefreshToken;
import com.accolite.pru.health.AuthApp.repository.UserDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDeviceService {

	@Autowired
	private UserDeviceRepository userDeviceRepository;


	/**
	 * Find the user device info by id
	 */
	public Optional<UserDevice> findById(Long id) {
		return userDeviceRepository.findById(id);
	}

	/**
	 * Finds the refresh token associated with the user_device
	 */
	public Optional<JwtRefreshToken> findRefreshTokenById(Long id) {
		return userDeviceRepository.findRefreshTokenById(id);
	}

	/**
	 * Save the userDevice instance to the repository
	 */
	public UserDevice save(UserDevice userDevice) {
		return userDeviceRepository.save(userDevice);
	}


	/**
	 * Creates a new user device and set the user to the current device
	 */
	public UserDevice createUserDevice(LoginDeviceInfo deviceInfo) {
		UserDevice userDevice = new UserDevice();
		userDevice.setDeviceId(deviceInfo.getDeviceId());
		userDevice.setDeviceType(deviceInfo.getDeviceType());
		userDevice.setNotificationToken(deviceInfo.getNotificationToken());
		userDevice.setRefreshActive(true);
		return userDevice;
	}
}
