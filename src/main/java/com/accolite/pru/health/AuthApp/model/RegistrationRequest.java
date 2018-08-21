package com.accolite.pru.health.AuthApp.model;

import org.springframework.lang.NonNull;

public class RegistrationRequest {

	private User user;

	@NonNull
	private Boolean registerAsAdmin;

	public RegistrationRequest(User user, Boolean registerAsAdmin) {
		this.user = user;
		this.registerAsAdmin = registerAsAdmin;
	}

	public RegistrationRequest() {
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getRegisterAsAdmin() {
		return registerAsAdmin;
	}

	public void setRegisterAsAdmin(Boolean registerAsAdmin) {
		this.registerAsAdmin = registerAsAdmin;
	}
}
