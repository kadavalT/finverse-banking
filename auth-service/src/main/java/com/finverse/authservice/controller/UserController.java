package com.finverse.authservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/welcome")
	public String userWelcome() {
		return "Welcome, USER!";
	}
}
