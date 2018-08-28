package com.accolite.pru.health.AuthApp.controller;

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

import com.accolite.pru.health.AuthApp.event.OnGenerateResetLinkMailEvent;
import com.accolite.pru.health.AuthApp.model.PasswordResetToken;
import com.accolite.pru.health.AuthApp.model.payload.ForgotPasswordResetLinkRequest;
import com.accolite.pru.health.AuthApp.model.payload.ForgotPasswordResetPasswordRequest;
import com.accolite.pru.health.AuthApp.service.ForgotPasswordService;

@RestController
@RequestMapping(value = "/user/forgotpassword")
public class ForgotPasswordController {
	@Autowired
	ForgotPasswordService forgotPasswordService;

	PasswordResetToken passwordResetToken = new PasswordResetToken();

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	/**
	 * Reset link.
	 *
	 * @param forgotPasswordResetLinkRequest
	 *            the forgot password reset link request
	 * @return the response entity
	 */
	@PostMapping("/resetlink")
	public ResponseEntity<?> resetLink(
			@Valid @RequestBody ForgotPasswordResetLinkRequest forgotPasswordResetLinkRequest) {
		passwordResetToken = forgotPasswordService.generatePasswordResetToken(forgotPasswordResetLinkRequest.getEmail());
		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/user/forgotpassword" + "/resetlink/resetpassword");
		OnGenerateResetLinkMailEvent generateResetLinkMailEvent = new OnGenerateResetLinkMailEvent(passwordResetToken,
				urlBuilder, passwordResetToken.getToken());
		applicationEventPublisher.publishEvent(generateResetLinkMailEvent);
		return ResponseEntity.ok(true);
	}

	/**
	 * Reset password.
	 *
	 * @param token
	 *            the token
	 * @param forgotPasswordResetPasswordRequest
	 *            the forgot password reset password request
	 * @return the response entity
	 */
	@PostMapping("/resetlink/resetpassword")
	public ResponseEntity<?> resetPassword(@RequestParam(value = "token") String token,
			@Valid @RequestBody ForgotPasswordResetPasswordRequest forgotPasswordResetPasswordRequest) {
		forgotPasswordService.resetPassword(forgotPasswordResetPasswordRequest.getPassword(), token);
		return ResponseEntity.ok(true);
	}
}
