package com.accolite.pru.health.AuthApp.model.payload;

import javax.validation.constraints.NotBlank;

public class UpdatePasswordRequest {

	@NotBlank(message = "Old password must not be blank")
	private String oldPassword;

	@NotBlank(message = "New password must not be blank")
	private String newPassword;

	public UpdatePasswordRequest(@NotBlank String oldPassword, @NotBlank String newPassword) {
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
