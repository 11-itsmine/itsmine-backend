package com.sparta.itsmine.domain.refreshtoken.service;

import java.util.concurrent.TimeUnit;

import com.sparta.itsmine.domain.refreshtoken.entity.RefreshToken;
import com.sparta.itsmine.domain.refreshtoken.repository.RefreshTokenAdapter;
import com.sparta.itsmine.domain.refreshtoken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenAdapter refreshTokenAdapter;
    private final RedisTemplate<String, String> redisTemplate;
    private String token;
    private final Long refreshTokenExpiration = 30 * 24 * 60 * 60 * 1000L; // 30일

    @Transactional
    public void save(String username, String refreshToken) {
            redisTemplate.opsForValue().set(username, refreshToken, refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }
}
