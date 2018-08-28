package com.accolite.pru.health.AuthApp.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

import com.accolite.pru.health.AuthApp.model.PasswordResetToken;

public class OnGenerateResetLinkMailEvent extends ApplicationEvent {
	private UriComponentsBuilder redirectUrl;
	private PasswordResetToken passwordResetToken;
	private String token;

	public OnGenerateResetLinkMailEvent(PasswordResetToken passwordResetToken, UriComponentsBuilder redirectUrl,
			String token) {
		super(passwordResetToken);
		this.passwordResetToken = passwordResetToken;
		this.redirectUrl = redirectUrl;
		this.token = token;
	}

	public PasswordResetToken getPasswordResetToken() {
		return passwordResetToken;
	}

	public void setPasswordResetToken(PasswordResetToken passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UriComponentsBuilder getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(UriComponentsBuilder redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
