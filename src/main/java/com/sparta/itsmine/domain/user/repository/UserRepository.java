package com.sparta.itsmine.domain.user.repository;

import com.sparta.itsmine.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findUserByUsernameAndNameAndEmail(String username, String name, String email);

    boolean existsByUsername(String username);

    Optional<User> findByIdAndDeletedAtIsNull(Long id);

    List<User> findAllByBlockedAtIsNotNull();

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findById(Long userId);
}
