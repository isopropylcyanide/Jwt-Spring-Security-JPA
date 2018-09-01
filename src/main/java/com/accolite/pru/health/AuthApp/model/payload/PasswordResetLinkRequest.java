package com.accolite.pru.health.AuthApp.model.payload;

import javax.validation.constraints.NotNull;

public class PasswordResetLinkRequest {

	@NotNull(message = "Email cannot be blank")
	String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
