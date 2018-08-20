package com.accolite.pru.health.AuthApp.exception;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;

public class UserAuthenticationException extends AuthenticationException {

	private static final Logger logger = Logger.getLogger(UserAuthenticationException.class);

	public UserAuthenticationException(String msg, Throwable throwable) {
		super(msg, throwable);
		logger.error(msg);
		logger.error(throwable.getCause());
	}

	public UserAuthenticationException(String msg) {
		super(msg);
		logger.info(msg);
	}
}
