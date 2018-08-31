package com.accolite.pru.health.AuthApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

	@GetMapping("/password/reset")
	public String resetPassword(@RequestParam(value = "token") String token, ModelMap model) {
		String url = "/api/auth/password/reset?token=" + token;
		model.addAttribute("url", url);
		return "reset-password";
	}

}
