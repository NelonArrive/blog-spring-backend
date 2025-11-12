package com.nelon.blog.services;

import com.nelon.blog.services.interfaces.IAuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
	
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	@Value("${jwt.expiry-ms}")
	private Long jwtExpiryMs;
	
	@Override
	public UserDetails authenticate(String email, String password) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(email, password)
		);
		return userDetailsService.loadUserByUsername(email);
	}
	
	@Override
	public String generateToken(UserDetails userDetails) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpiryMs);
		
		return Jwts.builder()
			.subject(userDetails.getUsername())
			.issuedAt(now)
			.expiration(expiryDate)
			.signWith(getSigninKey())
			.compact();
	}
	
	@Override
	public UserDetails validateToken(String token) {
		String username = extractUsername(token);
		return userDetailsService.loadUserByUsername(username);
	}
	
	private String extractUsername(String token) {
		return Jwts.parser()
			.verifyWith(getSigninKey())
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getSubject();
	}
	
	private SecretKey getSigninKey() {
		byte[] keyBytes = secretKey.getBytes();
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
