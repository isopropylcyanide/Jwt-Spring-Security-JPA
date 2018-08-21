package com.accolite.pru.health.AuthApp.security;

import com.accolite.pru.health.AuthApp.service.CustomUserDetailsService;
import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class);

	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AuthenticationException e) throws IOException, ServletException {
		logger.error("User is unauthorised. Routing from the entry point");
		httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sorry, you don't have the authorization " +
				"to access this resource");

	}
}