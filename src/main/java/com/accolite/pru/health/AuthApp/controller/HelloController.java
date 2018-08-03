package com.accolite.pru.health.AuthApp.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest")
public class HelloController {

	@Autowired
	private Logger logger;

	@GetMapping("hello")
	public ResponseEntity<?> sayHello(){
		logger.info("Inside secured resource");
		return ResponseEntity.ok("Hello. This is a secured resource");
	}

}
