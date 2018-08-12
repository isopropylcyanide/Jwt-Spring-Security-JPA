package com.accolite.pru.health.AuthApp.exception;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Scope("prototype")
@Component
public class UserAuthenticationException extends AuthenticationException{

	@Autowired
	private Logger logger;

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
