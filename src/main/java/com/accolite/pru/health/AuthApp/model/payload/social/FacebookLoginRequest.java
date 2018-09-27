package com.accolite.pru.health.AuthApp.model.payload.social;

import com.accolite.pru.health.AuthApp.model.payload.DeviceInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "Facebook Login Request", description = "The facebook login request payload")
public class FacebookLoginRequest {

	@NotBlank(message = "Facebook associated email can not be blank")
	@ApiModelProperty(value = "Facebook associated email", required = true, allowableValues = "NonEmpty String")
	private String email;

	@NotNull(message = "Login token cannot be blank")
	@ApiModelProperty(value = "Valid user token generated at UI through the facebook SDK", required = true,
			allowableValues = "NonEmpty String")
	private String token;

	@Valid
	@NotNull(message = "Device info cannot be null")
	@ApiModelProperty(value = "Device info", required = true, dataType = "object", allowableValues = "A valid " +
			"deviceInfo object")
	private DeviceInfo deviceInfo;

	public FacebookLoginRequest() {
	}

	public FacebookLoginRequest(String email, String token, DeviceInfo deviceInfo) {
		this.email = email;
		this.token = token;
		this.deviceInfo = deviceInfo;
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

	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
}
