package com.accolite.pru.health.AuthApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidTokenRequestException extends RuntimeException {

	private String tokenType;
	private String token;
	private String message;

	public InvalidTokenRequestException(String tokenType, String token, String message) {
		super(String.format("Invalid [%s] token [%s] : %s", token, tokenType, message));
		this.tokenType = tokenType;
		this.token = token;
		this.message = message;
	}
}
