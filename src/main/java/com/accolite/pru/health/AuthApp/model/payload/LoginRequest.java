package com.accolite.pru.health.AuthApp.model.payload;

import com.accolite.pru.health.AuthApp.validation.annotation.NullOrNotBlank;

import javax.validation.constraints.NotNull;

public class LoginRequest {

	@NullOrNotBlank(message = "Username can be null but not blank")
	private String userName;

	@NullOrNotBlank(message = "Email can be null but not blank")
	private String email;

	@NotNull(message = "Password cannot be blank")
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
