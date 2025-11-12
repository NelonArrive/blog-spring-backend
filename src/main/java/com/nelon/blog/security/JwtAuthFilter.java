package com.nelon.blog.security;

import com.nelon.blog.services.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final AuthService authService;
	
	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String token = extractToken(request);
		if (token != null) {
			UserDetails user = authService.validateToken(token);
			
			UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(user, null,
					user.getAuthorities()
				);
			
			authentication.setDetails(authentication.getDetails());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
	}
	
	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
