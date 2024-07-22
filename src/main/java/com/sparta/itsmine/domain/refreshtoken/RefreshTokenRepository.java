package com.sparta.itsmine.domain.refreshtoken;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUsername(String username);

    void deleteByUsername(String username);

}