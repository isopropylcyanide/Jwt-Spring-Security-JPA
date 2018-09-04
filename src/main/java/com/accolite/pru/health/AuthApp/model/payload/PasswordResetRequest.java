package com.accolite.pru.health.AuthApp.model.payload;

import com.accolite.pru.health.AuthApp.validation.annotation.MatchPassword;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@MatchPassword
@ApiModel(value = "Password reset Request", description = "The password reset request payload")
public class PasswordResetRequest {

	@NotBlank(message = "Password cannot be blank")
	@ApiModelProperty(value = "New user password", required = true, allowableValues = "NonEmpty String")
	private String password;

	@NotBlank(message = "Confirm Password cannot be blank")
	@ApiModelProperty(value = "Must match the new user password. Else exception will be thrown", required = true,
			allowableValues = "NonEmpty String matching the password")
	private String confirmPassword;

	@NotBlank(message = "Token has to be supplied along with a password reset request")
	@ApiModelProperty(value = "Reset token received in mail", required = true, allowableValues = "NonEmpty String")
	private String token;

	public PasswordResetRequest() {
	}

	public PasswordResetRequest(String password, String confirmPassword, String token) {
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.token = token;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
