package com.accolite.pru.health.AuthApp.security;

import com.accolite.pru.health.AuthApp.service.CustomUserDetailsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Value("${app.jwt.header}")
	private String tokenRequestHeader;

	@Value("${app.jwt.header.prefix}")
	private String tokenRequestHeaderPrefix;

	private static final Logger log = Logger.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	/**
	 * Filter the incoming request for a valid token in the request header
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		try {
			String jwt = getJwtFromRequest(request);

			if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
				Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);

				UserDetails userDetails = customUserDetailsService.loadUserById(userId);
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception ex) {
			log.error("Failed to set user authentication in security context: ", ex);
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Extract the token from the Authorization request header
	 */
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(tokenRequestHeader);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenRequestHeaderPrefix)) {
			log.info("Extracted Token: " + bearerToken);
			return bearerToken.replace(tokenRequestHeaderPrefix, "");
		}
		return null;
	}
}
