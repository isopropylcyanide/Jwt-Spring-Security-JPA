package com.accolite.pru.health.AuthApp.model.payload;

import javax.validation.constraints.NotBlank;

public class TokenRefreshRequest {

	@NotBlank(message = "Refresh token cannot be blank")
	private String refreshToken;

	public TokenRefreshRequest(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public TokenRefreshRequest() {
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
