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

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class);

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> dbUser = userRepository.findByEmail(email);
        logger.info("Fetched user : " + dbUser + " by " + email);
        return dbUser.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user email in the database for " + email));
    }

    public UserDetails loadUserById(Long id) {
        Optional<User> dbUser = userRepository.findById(id);
        logger.info("Fetched user : " + dbUser + " by " + id);
        return dbUser.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user id in the database for " + id));
    }
}
