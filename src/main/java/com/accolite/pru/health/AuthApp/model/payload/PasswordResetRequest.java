package com.accolite.pru.health.AuthApp.model.payload;

import javax.validation.constraints.NotNull;

public class PasswordResetRequest {
	@NotNull(message = "Password cannot be blank")
	String password;

	@NotNull(message = "Confirm Password cannot be blank")
	String confirmPassword;

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
