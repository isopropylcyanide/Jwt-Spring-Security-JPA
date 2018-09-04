package com.accolite.pru.health.AuthApp.model.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Update password Request", description = "The update password request payload")
public class UpdatePasswordRequest {

	@NotBlank(message = "Old password must not be blank")
	@ApiModelProperty(value = "Valid current user password", required = true, allowableValues = "NonEmpty String")
	private String oldPassword;

	@NotBlank(message = "New password must not be blank")
	@ApiModelProperty(value = "Valid new password string", required = true, allowableValues = "NonEmpty String")
	private String newPassword;

	public UpdatePasswordRequest(String oldPassword, String newPassword) {
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	public UpdatePasswordRequest() {
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
