package com.accolite.pru.health.AuthApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class PasswordResetException extends RuntimeException {
	public PasswordResetException(String message) {
		super(message);
	}

	public PasswordResetException(String message, Throwable cause) {
		super(message, cause);
	}
}
