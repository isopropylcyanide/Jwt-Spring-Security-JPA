package com.accolite.pru.health.AuthApp.model.payload;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class LogOutRequest {

	@Valid
	@NotNull(message = "Device info cannot be null")
	private DeviceInfo deviceInfo;

	public LogOutRequest() {
	}

	public LogOutRequest(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
}
