package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.exception.TokenRefreshException;
import com.accolite.pru.health.AuthApp.model.UserDevice;
import com.accolite.pru.health.AuthApp.model.payload.DeviceInfo;
import com.accolite.pru.health.AuthApp.model.token.RefreshToken;
import com.accolite.pru.health.AuthApp.repository.UserDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;

    @Autowired
    public UserDeviceService(UserDeviceRepository userDeviceRepository) {
        this.userDeviceRepository = userDeviceRepository;
    }

    /**
     * Find the user device info by id
     */
    public Optional<UserDevice> findById(Long id) {
        return userDeviceRepository.findById(id);
    }

    /**
     * Find the user device info by id. Assuming only one deviceId per device
     * as logout clears the refresh token associated with the device.
     */
    public Optional<UserDevice> findByDeviceId(String deviceId) {
        return userDeviceRepository.findByDeviceId(deviceId);
    }

    /**
     * Find the user device info by user id
     */
    public Optional<UserDevice> findByUserId(Long userId) {
        return userDeviceRepository.findByUserId(userId);
    }

    /**
     * Find the user device info by refresh token
     */
    public Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken) {
        return userDeviceRepository.findByRefreshToken(refreshToken);
    }

    /**
     * Finds the refresh token associated with the user_device
     */
    public Optional<RefreshToken> findRefreshTokenById(Long id) {
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
    public UserDevice createUserDevice(DeviceInfo deviceInfo) {
        UserDevice userDevice = new UserDevice();
        userDevice.setDeviceId(deviceInfo.getDeviceId());
        userDevice.setDeviceType(deviceInfo.getDeviceType());
        userDevice.setNotificationToken(deviceInfo.getNotificationToken());
        userDevice.setRefreshActive(true);
        return userDevice;
    }

    /**
     * Check whether the user device corresponding to the token has refresh enabled and
     * throw appropriate errors to the client
     */
    public void verifyRefreshAvailability(RefreshToken refreshToken) {
        UserDevice userDevice = findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(refreshToken.getToken(), "No device found for the matching token. Please login again"));

        if (!userDevice.getRefreshActive()) {
            throw new TokenRefreshException(refreshToken.getToken(), "Refresh blocked for the device. Please login through a different device");
        }
    }
}
