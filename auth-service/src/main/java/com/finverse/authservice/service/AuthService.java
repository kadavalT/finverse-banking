package com.finverse.authservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finverse.authservice.dto.AuthRequest;
import com.finverse.authservice.dto.RegisterRequest;
import com.finverse.authservice.exception.AuthServiceException;
import com.finverse.authservice.model.User;
import com.finverse.authservice.repository.UserRepository;
import com.finverse.authservice.security.JwtUtil;

@Service
public class AuthService {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	public String register(RegisterRequest request) {

		logger.info("Starting registration for email: {}", request.getEmail());

		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			logger.warn("Registration failed - Email already exists: {}", request.getEmail());

			throw new AuthServiceException("Email is already registered.");
		}

		validateAadhaar(request.getAadhaar());

		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		logger.debug("Password encrypted for email: {}", request.getEmail());

		user.setRole(request.getRole());
		user.setPhone(request.getPhone());
		user.setPan(request.getPan());
		user.setAadhaar(request.getAadhaar());

		userRepository.save(user);

		logger.info("User saved successfully: {}", request.getEmail());

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

		String token = jwtUtil.generateToken(userDetails);
		logger.debug("JWT token generated for email: {}", request.getEmail());

		return token;
	}

	public String login(AuthRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

			throw new RuntimeException("Invalid credentials");
		}

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

		return jwtUtil.generateToken(userDetails);

	}

	private void validateAadhaar(String aadhaar) {
		if (aadhaar == null || !aadhaar.matches("\\d{12}")) {
			throw new IllegalArgumentException("Invalid Aadhaar number. Must be exactly 12 digits.");
		}
	}

}
