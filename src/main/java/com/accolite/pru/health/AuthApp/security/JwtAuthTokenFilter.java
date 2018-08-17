package com.accolite.pru.health.AuthApp.security;

import org.springframework.security.authentication.AuthenticationManager;

public class JwtAuthTokenFilter {

	private AuthenticationManager authenticationManager;
	private JwtSuccessHandler authenticationSuccessHandler;

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationSuccessHandler(JwtSuccessHandler authenticationSuccessHandler) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	public JwtSuccessHandler getAuthenticationSuccessHandler() {
		return authenticationSuccessHandler;
	}
}
