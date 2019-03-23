package com.accolite.pru.health.AuthApp.model.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Password reset link request", description = "The password reset link payload")
public class PasswordResetLinkRequest {

    @NotBlank(message = "Email cannot be blank")
    @ApiModelProperty(value = "User registered email", required = true, allowableValues = "NonEmpty String")
    private String email;

    public PasswordResetLinkRequest(String email) {
        this.email = email;
    }

    public PasswordResetLinkRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
