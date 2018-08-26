package com.accolite.pru.health.AuthApp.event;

import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.token.EmailVerificationToken;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

public class OnRegenerateEmailVerificationEvent extends ApplicationEvent {

	private UriComponentsBuilder redirectUrl;
	private User user;
	private EmailVerificationToken token;

	public OnRegenerateEmailVerificationEvent(
			User user, UriComponentsBuilder redirectUrl, EmailVerificationToken token) {
		super(user);
		this.user = user;
		this.redirectUrl = redirectUrl;
		this.token = token;
	}

	public UriComponentsBuilder getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(UriComponentsBuilder redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public EmailVerificationToken getToken() {
		return token;
	}

	public void setToken(EmailVerificationToken token) {
		this.token = token;
	}
}
