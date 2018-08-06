package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.exception.CustomAuthenticationException;
import com.accolite.pru.health.AuthApp.model.CustomUserDetails;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
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
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<User> dbUser = userRepository.findByUserName(userName);
		System.out.println("Got user: " + dbUser + " for " + userName);
		return dbUser.map(CustomUserDetails::new)
				.orElseThrow(() -> new CustomAuthenticationException("Couldn't find a matching username in the " +
						"database for " + userName));
	}
}
