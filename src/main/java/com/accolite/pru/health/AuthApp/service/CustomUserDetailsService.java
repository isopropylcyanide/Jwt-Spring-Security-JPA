package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.model.CustomUserDetails;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Logger logger;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> dbUser = userRepository.findByEmail(email);
		logger.info("Got user: " + dbUser + " for " + email);
		return dbUser.map(CustomUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching email in the " +
						"database for " + email));
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		Optional<User> dbUser = userRepository.findById(id);
		logger.info("Got user: " + dbUser + " for " + id);
		return dbUser.map(CustomUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching id in the " +
						"database for " + id));
	}
}
