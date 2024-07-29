package com.sparta.itsmine.domain.refreshtoken.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.itsmine.domain.refreshtoken.entity.RefreshToken;
import com.sparta.itsmine.domain.refreshtoken.repository.RefreshTokenAdapter;
import com.sparta.itsmine.domain.refreshtoken.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenAdapter refreshTokenAdapter;

    @Transactional
    public void save(String username, String refreshToken) {
        RefreshToken existToken = refreshTokenAdapter.findByUsernameOrNull(username);
        if (existToken == null) {
            refreshTokenRepository.save(new RefreshToken(username, refreshToken));
        } else {
            existToken.update(refreshToken);
        }
//        refreshTokenRepository.save(new RefreshToken(username, refreshToken));
    }

}
