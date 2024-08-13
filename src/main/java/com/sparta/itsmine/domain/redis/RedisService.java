package com.sparta.itsmine.domain.redis;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisService {

	private final RedisTemplate<String, String> redisTemplate;
	private final Long refreshTokenExpiration = 14 * 24 * 60 * 60 * 1000L; // 14일
	private final Long kakaoTidExpiration = 3 * 60 * 1000L; // 3분

	@Transactional
	public void saveRefreshToken(String username, String refreshToken) {
		redisTemplate.opsForValue().set(username, refreshToken, refreshTokenExpiration, TimeUnit.MILLISECONDS);
	}

	@Transactional
	public void saveKakaoTid(String username, String tid) {
		redisTemplate.opsForValue().set(username+":tid", tid, kakaoTidExpiration, TimeUnit.MILLISECONDS);
	}
}
