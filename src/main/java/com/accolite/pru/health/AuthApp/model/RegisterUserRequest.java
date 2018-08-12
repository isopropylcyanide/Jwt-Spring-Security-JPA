package com.accolite.pru.health.AuthApp.model;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class RegisterUserRequest {

	private User user;
	private Boolean registerAsAdmin;

	public RegisterUserRequest(User user, Boolean registerAsAdmin) {
		this.user = user;
		this.registerAsAdmin = registerAsAdmin;
	}

	public RegisterUserRequest() {
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
