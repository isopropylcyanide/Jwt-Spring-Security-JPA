package com.accolite.pru.health.AuthApp.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/hello")
public class HelloController {

	@Autowired
	private Logger logger;

	@GetMapping("/secured/all")
	public ResponseEntity<?> saySecuredHello(){
		logger.info("Inside secured resource");
		return ResponseEntity.ok("Hello. This is a secured resource");
	}

	@GetMapping("/all")
	public ResponseEntity<?> sayHello(){
		logger.info("Inside unsecured resource");
		return ResponseEntity.ok("Hello. This is a not a secured resource");
	}

	@GetMapping("/secured/alternate")
	public ResponseEntity<?> sayAlternateHello(){
		logger.info("Inside unsecured resource");
		return ResponseEntity.ok("Hello. This is an alternate secured resource");
	}

}
