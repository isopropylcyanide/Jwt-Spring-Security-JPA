package com.accolite.pru.health.AuthApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class WebController {

	@GetMapping("/password/reset")
	public String resetPassword(@RequestParam(value = "token") String token, ModelMap model) {
		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/auth/password/reset");
		String url = urlBuilder.queryParam("token", token).toUriString();
		model.addAttribute("url", url);
		return "reset-password";
	}

}
