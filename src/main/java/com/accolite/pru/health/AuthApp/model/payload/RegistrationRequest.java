package com.accolite.pru.health.AuthApp.model.payload;

import com.accolite.pru.health.AuthApp.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RegistrationRequest {

	@NotNull(message = "User details cannot be null")
	@Valid
	private User user;

	@NotNull(message = "Specify whether the user has to be registered as an admin or not")
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
