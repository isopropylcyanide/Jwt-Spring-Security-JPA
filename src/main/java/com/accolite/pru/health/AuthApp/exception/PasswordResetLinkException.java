package com.accolite.pru.health.AuthApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class PasswordResetLinkException extends RuntimeException {
	public PasswordResetLinkException(String message) {
		super(message);
	}

	public PasswordResetLinkException(String message, Throwable cause) {
		super(message, cause);
	}
}