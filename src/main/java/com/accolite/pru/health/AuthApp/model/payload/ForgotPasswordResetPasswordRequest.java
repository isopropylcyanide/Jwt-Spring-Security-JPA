package com.accolite.pru.health.AuthApp.model.payload;

import javax.validation.constraints.NotNull;

public class ForgotPasswordResetPasswordRequest {
	@NotNull(message = "Password cannot be blank")
	String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
