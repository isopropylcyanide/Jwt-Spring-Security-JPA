package com.accolite.pru.health.AuthApp.security;

import com.accolite.pru.health.AuthApp.exception.UserAuthenticationException;
import com.accolite.pru.health.AuthApp.model.CustomUserDetails;
import com.accolite.pru.health.AuthApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	private JwtTokenValidator jwtTokenUtil;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
	}

	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken;
		String jwtToken = jwtAuthenticationToken.getToken();

		Optional<User> tokenEmbeddedUser = jwtTokenUtil.validate(jwtToken);

		tokenEmbeddedUser.orElseThrow(() -> new UserAuthenticationException("Error de-encapsulating the user object " +
				"present inside the token [" + jwtToken + "]"));

		CustomUserDetails customUserDetails = new CustomUserDetails(tokenEmbeddedUser.get());
		logger.info("Returning valid custom user detail: " + customUserDetails);
		return customUserDetails;
	}
}
