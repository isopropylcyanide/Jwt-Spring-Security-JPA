package com.accolite.pru.health.AuthApp.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.accolite.pru.health.AuthApp.event.OnGenerateResetLinkEvent;
import com.accolite.pru.health.AuthApp.event.OnPasswordChangeEvent;
import com.accolite.pru.health.AuthApp.exception.ResourceNotFoundException;
import com.accolite.pru.health.AuthApp.model.PasswordResetToken;
import com.accolite.pru.health.AuthApp.model.payload.ApiResponse;
import com.accolite.pru.health.AuthApp.model.payload.PasswordResetLinkRequest;
import com.accolite.pru.health.AuthApp.model.payload.PasswordResetRequest;
import com.accolite.pru.health.AuthApp.service.PasswordResetService;

@RestController
@RequestMapping(value = "/user/password")
public class ForgotPasswordController {
	@Autowired
	PasswordResetService forgotPasswordService;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	/**
	 * Reset link.
	 *
	 * 1) Receives the mail id 2) Sends the mail id to service to check if it exists
	 * 3) Sends the link for resetting the password to that mailid
	 * 
	 * @param forgotPasswordResetLinkRequest
	 *            the forgot password reset link request
	 * @return the response entity
	 */
	@PostMapping("/resetlink")
	public ResponseEntity<?> resetLink(@Valid @RequestBody PasswordResetLinkRequest passwordResetLinkRequest) {
		Optional<PasswordResetToken> passwordResetTokenOpt = forgotPasswordService
				.generatePasswordResetToken(passwordResetLinkRequest.getEmail());
		passwordResetTokenOpt.orElseThrow(
				() -> new ResourceNotFoundException("Users", "email", passwordResetLinkRequest.getEmail()));
		PasswordResetToken passwordResetToken = passwordResetTokenOpt.get();
		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/user/password" + "/reset");
		OnGenerateResetLinkEvent generateResetLinkMailEvent = new OnGenerateResetLinkEvent(passwordResetToken,
				urlBuilder);
		applicationEventPublisher.publishEvent(generateResetLinkMailEvent);
		return ResponseEntity.ok(new ApiResponse("Password reset link sent successfully", true));
	}

	/**
	 * Reset password.
	 *
	 * 1) Receives the new password and confirm password 2) Sends the password to
	 * update 3) Sends the Acknowledgement after changing the password
	 * 
	 * @param token
	 *            the token
	 * @param forgotPasswordResetPasswordRequest
	 *            the forgot password reset password request
	 * @return the response entity
	 */
	@PostMapping("/reset")
	public ResponseEntity<?> resetPassword(@RequestParam(value = "token") String token,
			@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
		Optional<String> mailOpt = forgotPasswordService.resetPassword(passwordResetRequest.getPassword(), token);
		mailOpt.orElseThrow(() -> new ResourceNotFoundException("Token", "token id", token));
		String mail = mailOpt.get();
		OnPasswordChangeEvent onPasswordChangeEvent = new OnPasswordChangeEvent(mail, "Reset Password",
				"Changed Successfully");
		applicationEventPublisher.publishEvent(onPasswordChangeEvent);
		return ResponseEntity.ok(new ApiResponse("Password changed successfully", true));
	}
}
