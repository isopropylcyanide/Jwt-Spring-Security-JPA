package com.accolite.pru.health.AuthApp.model.payload;

import com.accolite.pru.health.AuthApp.validation.annotation.NullOrNotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class LoginRequest {

	@NullOrNotBlank(message = "Login Username can be null but not blank")
	private String username;

	@NullOrNotBlank(message = "Login Email can be null but not blank")
	private String email;

	@NotNull(message = "Login password cannot be blank")
	private String password;

	@Valid
	@NotNull(message = "Device info cannot be null")
	private DeviceInfo deviceInfo;

	public LoginRequest(String username, String email, String password, DeviceInfo deviceInfo) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.deviceInfo = deviceInfo;
	}

	public LoginRequest() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
}
