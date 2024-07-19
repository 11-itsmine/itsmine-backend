package com.sparta.itsmine.domain.usertest.repository;

import com.sparta.itsmine.domain.usertest.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
}
