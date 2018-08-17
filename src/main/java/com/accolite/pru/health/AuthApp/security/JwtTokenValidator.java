package com.accolite.pru.health.AuthApp.security;

import com.accolite.pru.health.AuthApp.model.Role;
import com.accolite.pru.health.AuthApp.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class JwtTokenValidator {

	@Value("${jwt.secret}")
	private String jwtSecret;


	@Autowired
	private Logger logger;

	public Optional<User> validate(String jwtToken) {
		logger.info("Trying to validate given token: " + jwtToken);
		try {
			User tokenEmbeddedUser = new User();
			Claims body = Jwts.parser()
							.setSigningKey(jwtSecret)
							.parseClaimsJws(jwtToken)
							.getBody();
			logger.info("Body subject/email: " + body.getSubject());
			logger.info("Body id : " + body.getId());
			logger.info("Body last issued: " + body.getIssuedAt());
			logger.info("Body role: " + body.get("role"));
			logger.info("Body active: " + body.get("active"));
			logger.info("Body username: " + body.get("username"));
			logger.info("Body firstName: " + body.get("firstName"));
			logger.info("Body lastName: " + body.get("lastName"));

			tokenEmbeddedUser.setId(Long.parseLong(body.getId()));
			tokenEmbeddedUser.setEmail(body.getSubject());
			tokenEmbeddedUser.setUserName(body.get("username").toString());
			tokenEmbeddedUser.setRoles((Set<Role>) body.get("role"));
			tokenEmbeddedUser.setActive(Boolean.getBoolean(body.get("active").toString()));
			tokenEmbeddedUser.setLastIssuedDate(body.getIssuedAt());
			tokenEmbeddedUser.setFirstName(body.get("firstName").toString());
			tokenEmbeddedUser.setLastName(body.get("lastName").toString());
			return Optional.ofNullable(tokenEmbeddedUser);
		} catch (Exception e){
			logger.error("Exception occured while validating user: " + e);
		}
		return Optional.empty();
	}
}
