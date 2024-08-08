package com.sparta.itsmine.domain.user.repository;

import com.sparta.itsmine.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByIdAndDeletedAtIsNull(Long id);

    Optional<User> findByKakaoId(Long kakaoId);
}
