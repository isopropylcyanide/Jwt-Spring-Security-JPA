package com.accolite.pru.health.AuthApp.model.payload;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TokenRefreshRequest {

	@NotBlank(message = "Refresh token cannot be null")
	private String refreshToken;

	@Valid
	@NotNull(message = "Device info cannot be null")
	private DeviceInfo deviceInfo;

	public TokenRefreshRequest(String refreshToken, DeviceInfo deviceInfo) {
		this.refreshToken = refreshToken;
		this.deviceInfo = deviceInfo;
	}

	public TokenRefreshRequest() {
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
}
