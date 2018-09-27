package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.repository.FacebookLoginRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacebookLoginService {

	@Autowired
	private FacebookLoginRepository facebookLoginRepository;

	private static final Logger logger = Logger.getLogger(AuthService.class);


}
