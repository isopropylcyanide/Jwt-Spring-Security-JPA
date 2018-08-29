package com.accolite.pru.health.AuthApp.service;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	private static final Logger logger = Logger.getLogger(UserService.class);

	/**
	 * Finds a user in the database by username
	 */
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	/**
	 * Finds a user in the database by email
	 */
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * Finds current logged in user in the database by email Note: This will always
	 * return a concrete valid instance.
	 */
	public User getLoggedInUser(String email) {
		return findByEmail(email).get();
	}

	/**
	 * Find a user in db by id.
	 *
	 */
	public Optional<User> findById(Long Id) {
		return userRepository.findById(Id);
	}

	/**
	 * Save the user to the database
	 */
	public User save(User user) {
		return userRepository.save(user);
	}

	/**
	 * Check is the user exists given the email: naturalId
	 */
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	/**
	 * Check is the user exists given the username: naturalId
	 */
	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

}
