package com.nelon.blog.services.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface IAuthService {
	UserDetails authenticate(String email, String password);
	String generateToken(UserDetails userDetails);
	UserDetails validateToken(String token);
}
