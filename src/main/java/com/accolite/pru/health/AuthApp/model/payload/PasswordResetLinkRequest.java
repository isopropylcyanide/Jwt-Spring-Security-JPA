package com.accolite.pru.health.AuthApp.model.payload;

import javax.validation.constraints.NotBlank;

public class PasswordResetLinkRequest {

	@NotBlank(message = "Email cannot be blank")
	private String email;

	public PasswordResetLinkRequest(String email) {
		this.email = email;
	}

	public PasswordResetLinkRequest() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
