package com.accolite.pru.health.AuthApp.event;

import com.accolite.pru.health.AuthApp.model.User;
import org.springframework.context.ApplicationEvent;

public class OnUserRegistrationCompleteEvent extends ApplicationEvent {

	private String redirectUrl;
	private User user;

	public OnUserRegistrationCompleteEvent(
			User user, String redirectUrl) {
		super(user);
		this.user = user;
		this.redirectUrl = redirectUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
