package com.finverse.authservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finverse.authservice.dto.AuthRequest;
import com.finverse.authservice.dto.AuthResponse;
import com.finverse.authservice.dto.RegisterRequest;
import com.finverse.authservice.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		logger.info("Register attempt for email: {}", request.getEmail());
		String token = authService.register(request);
		logger.info("User registered successfully: {}", request.getEmail());
		return ResponseEntity.ok(new AuthResponse(token));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
		logger.info("Login attempt for email: {}", request.getEmail());
		String token = authService.login(request);
		logger.info("Login successful for email: {}", request.getEmail());
		return ResponseEntity.ok(new AuthResponse(token));
	}
}
