package com.accolite.pru.health.AuthApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

	@GetMapping("/api/auth/password/reset")
	public String resetPassword(@RequestParam(value = "token") String token) {
		return "reset-password";
	}

	@GetMapping("/api/auth/password/reset/re")
	public String resetPassword() {
		return "reset-password1";
	}
}
