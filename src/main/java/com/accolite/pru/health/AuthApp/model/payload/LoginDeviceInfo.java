package com.accolite.pru.health.AuthApp.model.payload;

import com.accolite.pru.health.AuthApp.model.DeviceType;

import javax.validation.constraints.NotNull;

public class LoginDeviceInfo {

	@NotNull(message = "Device id cannot be null")
	private Long deviceId;

	@NotNull(message = "Device type cannot be null")
	private DeviceType deviceType;

	public LoginDeviceInfo() {
	}

	public LoginDeviceInfo(Long deviceId, DeviceType deviceType) {
		this.deviceId = deviceId;
		this.deviceType = deviceType;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}
}
