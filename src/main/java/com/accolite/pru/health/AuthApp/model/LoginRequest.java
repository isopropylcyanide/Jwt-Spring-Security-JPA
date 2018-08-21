package com.accolite.pru.health.AuthApp.model;

import com.accolite.pru.health.AuthApp.validation.annotation.NullOrNotBlank;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

	@NullOrNotBlank
	private String userName;

	@NullOrNotBlank
	private String email;

	@NotBlank
	private String password;

	public LoginRequest(String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
	}

	public LoginRequest() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
}
