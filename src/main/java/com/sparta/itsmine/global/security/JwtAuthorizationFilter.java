package com.sparta.itsmine.global.security;

import static com.sparta.itsmine.global.security.JwtProvider.AUTHORIZATION_HEADER;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.itsmine.domain.user.utils.UserRole;
import com.sparta.itsmine.global.common.response.HttpResponseDto;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        if (req.getCookies() != null) {
            Arrays.stream(req.getCookies())
                .forEach(cookie -> System.out.println(cookie.getName() + ": " + cookie.getValue()));
        }
        String accessToken = resolveTokenFromCookies(req);
        String refreshToken = jwtProvider.getRefreshTokenFromRequest(req);

        if (StringUtils.hasText(accessToken)) {
            if (jwtProvider.validateAccessToken(accessToken)) {
                log.info("액세스 토큰 검증 성공");
                setAuthentication(jwtProvider.getUsernameFromToken(accessToken));

            } else if (StringUtils.hasText(refreshToken)) {
                log.info("액세스 토큰 만료");

                if (jwtProvider.validateRefreshToken(refreshToken)) {
                    log.info("리프레시 토큰 검증 성공 & 새로운 액세스 토큰 발급");
                    String username = jwtProvider.getUsernameFromToken(refreshToken);
                    UserRole role = jwtProvider.getRoleFromToken(refreshToken);

                    String newAccessToken = jwtProvider.createAccessToken(username, role);

                    jwtProvider.setHeaderAccessToken(res, newAccessToken);
                    setAuthentication(username);

                } else {
                    log.info("리프레시 토큰 검증 실패");
                    jwtExceptionHandler(res, HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh 토큰입니다.");
                    return;
                }
            }
        }
        filterChain.doFilter(req, res);
    }

    private void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(createAuthentication(username));
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private void jwtExceptionHandler(HttpServletResponse res, HttpStatus status, String msg) {
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

    private String resolveTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (AUTHORIZATION_HEADER.equals(cookie.getName())) {
                    String token = cookie.getValue();
                    // %20을 공백으로 변환
                    token = token.replace("%20", " ");
                    return jwtProvider.substringToken(token);
                }
            }
        }
        return null;
    }

}
