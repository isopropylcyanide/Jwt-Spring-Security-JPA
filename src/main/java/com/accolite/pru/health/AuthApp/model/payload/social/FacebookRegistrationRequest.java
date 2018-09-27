package com.accolite.pru.health.AuthApp.model.payload.social;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@ApiModel(value = "Facebook Registration Request", description = "The facebook registration request payload")
public class FacebookRegistrationRequest {

	@NotBlank(message = "Registration email can be null but not blank")
	@ApiModelProperty(value = "A valid email", required = true, allowableValues = "NonEmpty String")
	private String email;

	@NotNull(message = "Facebook Registration token cannot be null")
	@ApiModelProperty(value = "A valid token string", required = true, allowableValues = "NonEmpty String")
	private String token;

	@Column(name = "TOKEN_EXPIRY_DT", nullable = false)
	private Instant tokenExpiry;

	@NotNull(message = "Specify whether the user has to be registered as an admin or not")
	@ApiModelProperty(value = "Flag denoting whether the user is an admin or not", required = true,
			dataType = "boolean", allowableValues = "true, false")
	private Boolean registerAsAdmin;

	public FacebookRegistrationRequest() {
	}

	public FacebookRegistrationRequest(String token, Instant tokenExpiry, Boolean registerAsAdmin) {
		email = email;
		this.token = token;
		this.tokenExpiry = tokenExpiry;
		this.registerAsAdmin = registerAsAdmin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Instant getTokenExpiry() {
		return tokenExpiry;
	}

	public void setTokenExpiry(Instant tokenExpiry) {
		this.tokenExpiry = tokenExpiry;
	}

	public Boolean getRegisterAsAdmin() {
		return registerAsAdmin;
	}

	public void setRegisterAsAdmin(Boolean registerAsAdmin) {
		this.registerAsAdmin = registerAsAdmin;
	}
}
