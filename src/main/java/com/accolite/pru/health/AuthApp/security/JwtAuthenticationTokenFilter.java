package com.accolite.pru.health.AuthApp.security;

import com.accolite.pru.health.AuthApp.exception.JwtTokenMissingException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Value("${jwt.header.prefix}")
	private String tokenHeaderPrefix;

	public JwtAuthenticationTokenFilter(String defaultFilterProcessesUrl) {
		super("**/secured/**");
	}

	@Autowired
	private Logger log;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
		String header = httpServletRequest.getHeader(tokenHeader);
		if (null == header || !header.startsWith(tokenHeaderPrefix)) {
			throw new JwtTokenMissingException("Missing/Null JWT Token for the request header [" + header + "]");
		}
		String requestJwtTokenString = header.substring(tokenHeaderPrefix.length() + 1);
		log.info("Extracted jwt token: " + requestJwtTokenString);
		JwtAuthenticationToken requestJwtToken = new JwtAuthenticationToken(null, null, requestJwtTokenString);
		getAuthenticationManager().authenticate(requestJwtToken);
		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		log.info("Successfully authenticated: [Filter] " + request);
		chain.doFilter(request, response);
	}
}
