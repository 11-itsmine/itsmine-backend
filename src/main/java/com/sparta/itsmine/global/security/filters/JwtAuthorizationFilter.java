package com.sparta.itsmine.global.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.itsmine.domain.user.utils.UserRole;
import com.sparta.itsmine.global.common.response.HttpResponseDto;
import com.sparta.itsmine.global.security.JwtProvider;
import com.sparta.itsmine.global.security.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final UserDetailsServiceImpl userDetailsService;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
		FilterChain filterChain) throws ServletException, IOException {

		// 로그인 URL은 건너뛰기
		if (req.getRequestURI().equals("/users/login")) {
			filterChain.doFilter(req, res);
			return;
		}

		String accessToken = jwtProvider.getAccessTokenFromRequest(req);
		String username = jwtProvider.getUsernameFromToken(accessToken);

		if (StringUtils.hasText(accessToken)) {
			if (jwtProvider.validateAccessToken(accessToken)) {
				log.info("액세스 토큰 검증 성공");
				UserRole role = jwtProvider.getRoleFromToken(accessToken);
				String newAccessToken = jwtProvider.createAccessToken(username, role);
				jwtProvider.addJwtToCookie(newAccessToken, res);
				setAuthentication(jwtProvider.getUsernameFromToken(accessToken));

			} else if (Boolean.TRUE.equals(redisTemplate.hasKey(username))) {
				log.info("액세스 토큰 만료, 리프레시 토큰 검증 시도");
				String refreshToken = jwtProvider.substringToken(redisTemplate.opsForValue().get(username));
				if (jwtProvider.hasRefreshToken(username)) {
					log.info("리프레시 토큰 검증 성공 & 새로운 액세스 토큰 발급");
					UserRole role = jwtProvider.getRoleFromToken(refreshToken);
					String newAccessToken = jwtProvider.createAccessToken(username, role);
					jwtProvider.addJwtToCookie(newAccessToken, res);
					setAuthentication(username);
				} else {
					log.error("리프레시 토큰 검증 실패");
					jwtExceptionHandler(res, HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh 토큰입니다.");
					return;
				}
			} else {
				jwtExceptionHandler(res, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
				return;
			}
		} else {
			log.error("토큰 없음");
		}
		filterChain.doFilter(req, res);
	}

	/**
	 * 인증 처리
	 */
	public void setAuthentication(String username) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(createAuthentication(username));
		SecurityContextHolder.setContext(context);
	}

	/**
	 * 인증 객체 생성
	 */
	private Authentication createAuthentication(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	/**
	 * JWT 예외 처리
	 */
	public void jwtExceptionHandler(HttpServletResponse res, HttpStatus status, String msg) {
		int statusCode = status.value();
		res.setStatus(statusCode);
		res.setContentType("application/json");
		try {
			String json = new ObjectMapper().writeValueAsString(new HttpResponseDto(statusCode, msg));
			res.getWriter().write(json);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}
