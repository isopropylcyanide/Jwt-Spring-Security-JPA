package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.model.payload.social.FacebookRegistrationRequest;
import com.accolite.pru.health.AuthApp.model.social.FacebookUser;
import com.accolite.pru.health.AuthApp.repository.FacebookUserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacebookUserService {

	@Autowired
	private FacebookUserRepository facebookUserRepository;

	private static final Logger logger = Logger.getLogger(AuthService.class);


	public FacebookUser createUser(FacebookRegistrationRequest newFbRegistrationRequest) {
		FacebookUser facebookUser = new FacebookUser();
		facebookUser.setAccessToken(newFbRegistrationRequest.getToken());
		facebookUser.setAssociatedEmail(newFbRegistrationRequest.getEmail());
		facebookUser.setTokenExpiry(newFbRegistrationRequest.getTokenExpiry());
		return facebookUser;
	}
}
