package com.accolite.pru.health.AuthApp.model.payload;

import com.accolite.pru.health.AuthApp.model.DeviceType;
import com.accolite.pru.health.AuthApp.validation.annotation.NullOrNotBlank;

import javax.validation.constraints.NotNull;

public class LoginDeviceInfo {

	@NotNull(message = "Device id cannot be null")
	private String deviceId;

	@NotNull(message = "Device type cannot be null")
	private DeviceType deviceType;

	@NullOrNotBlank(message = "Device notification token can be null but not blank")
	private String notificationToken;

	public LoginDeviceInfo() {
	}

	public LoginDeviceInfo(String deviceId, DeviceType deviceTypem, String notificationToken) {
		this.deviceId = deviceId;
		deviceType = deviceType;
		notificationToken = notificationToken;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getNotificationToken() {
		return notificationToken;
	}

	public void setNotificationToken(String notificationToken) {
		this.notificationToken = notificationToken;
	}
}
