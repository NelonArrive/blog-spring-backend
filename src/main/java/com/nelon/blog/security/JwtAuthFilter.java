package com.nelon.blog.security;

// импорт твоего сервиса, который умеет работать с токенами

import com.nelon.blog.services.AuthService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

// из Spring Security — токен аутентификации и контекст безопасности
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

// базовый фильтр, который выполняется один раз на каждый запрос
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
// Lombok: создаёт конструктор с финальными полями (authService)
public class JwtAuthFilter extends OncePerRequestFilter {
	
	// 👇 этот сервис ты сам сделал — он умеет проверять токен и возвращать данные пользователя
	private final AuthService authService;
	
	// Этот метод — самое сердце фильтра
	// Он вызывается для КАЖДОГО HTTP-запроса
	@Override
	protected void doFilterInternal(
		HttpServletRequest request,  // входящий запрос
		HttpServletResponse response, // ответ
		FilterChain filterChain       // цепочка фильтров — нужно передать дальше
	) throws ServletException, IOException {
		
		try {
			// 👉 пытаемся достать токен из заголовка "Authorization"
			String token = extractToken(request);
			
			// если токен есть
			if (token != null) {
				// 👇 проверяем его (валиден ли он, не истёк ли)
				// и если всё ок, получаем данные пользователя
				UserDetails userDetails = authService.validateToken(token);
				
				// 👇 создаём объект, который говорит Spring Security:
				// "Вот юзер, он прошёл проверку, вот его роли"
				UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(
						userDetails,             // кто юзер
						null,                    // пароль не нужен, т.к. уже проверили токен
						userDetails.getAuthorities() // какие у него роли (ROLE_USER, ROLE_ADMIN)
					);
				
				// ❗️эта строка бесполезна (по сути ничего не делает)
				
				authentication.setDetails(authentication.getDetails());
				
				// 👇 кладём объект "authentication" в контекст Spring Security
				// теперь SecurityContextHolder знает, кто делает запрос
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				if (userDetails instanceof BlogUserDetails) {
					request.setAttribute("userId", ((BlogUserDetails) userDetails).getId());
				}
			}
		} catch (Exception e) {
			log.warn("Authentication failed.", e);
		}
		
		
		// ⚠️ ОБЯЗАТЕЛЬНО: передаём запрос дальше по цепочке фильтров
		// иначе остальные фильтры и контроллер просто не выполнятся!
		filterChain.doFilter(request, response);
	}
	
	// Этот метод достаёт токен из заголовка
	// Пример: "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		// Проверяем, что заголовок начинается с "Bearer "
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			// Возвращаем сам токен (обрезаем "Bearer ")
			return bearerToken.substring(7);
		}
		// если нет — возвращаем null
		return null;
	}
}
