package com.sparta.itsmine.domain.refreshtoken;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenAdapter {

	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshToken findByUsername(String username) {
		return refreshTokenRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(
				String.format("This \"%s\" does not exist.", username)));
	}

	@Transactional
	public void deleteByUsername(String username) {
		refreshTokenRepository.deleteByUsername(username);

	}

	public RefreshToken findByUsernameOrNull(String username) {
		return refreshTokenRepository.findByUsername(username).orElse(null);
	}


}
