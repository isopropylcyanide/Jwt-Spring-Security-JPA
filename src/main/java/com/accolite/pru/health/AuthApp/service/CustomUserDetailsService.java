package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.exception.UserAuthenticationException;
import com.accolite.pru.health.AuthApp.model.CustomUserDetails;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> dbUser = userRepository.findByEmail(email);
		System.out.println("Got user: " + dbUser + " for " + email);
		return dbUser.map(CustomUserDetails::new)
				.orElseThrow(() -> new UserAuthenticationException("Couldn't find a matching email in the " +
						"database for " + email));
	}
}
