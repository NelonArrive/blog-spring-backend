package com.nelon.blog.controllers;

import com.nelon.blog.domain.dto.AuthResponse;
import com.nelon.blog.domain.dto.LoginRequest;
import com.nelon.blog.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping
	public ResponseEntity<AuthResponse> login(
		@RequestBody LoginRequest loginRequest
	) {
		UserDetails userDetails = authService.authenticate(
			loginRequest.getEmail(),
			loginRequest.getPassword()
		);
		
		String tokenValue = authService.generateToken(userDetails);
		AuthResponse authResponse = AuthResponse.builder()
			.token(tokenValue)
			.expiresIn(86400)
			.build();
		return ResponseEntity.ok(authResponse);
	}
	
}
