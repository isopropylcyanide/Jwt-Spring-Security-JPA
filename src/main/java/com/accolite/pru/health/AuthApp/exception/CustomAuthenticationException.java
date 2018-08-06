package com.accolite.pru.health.AuthApp.exception;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class CustomAuthenticationException extends AuthenticationException {

	@Autowired
	private Logger logger;

	public CustomAuthenticationException(String msg) {
		super(msg);
		logger.info(msg);
	}
}
