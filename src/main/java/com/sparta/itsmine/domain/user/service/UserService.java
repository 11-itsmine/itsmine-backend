package com.sparta.itsmine.domain.user.service;


import com.sparta.itsmine.domain.user.dto.SignupRequestDto;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.repository.UserAdapter;
import com.sparta.itsmine.domain.user.repository.UserRepository;
import com.sparta.itsmine.domain.user.utils.UserRole;
import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.user.UserAlreadyExistsException;
import com.sparta.itsmine.global.exception.user.UserException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAdapter adapter;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // @Value("${admin.token}")
    // private String adminToken;

    public String signup(SignupRequestDto requestDto) {
        if (adapter.existsByUsername(requestDto.getUsername())) {
            throw new UserAlreadyExistsException(ResponseExceptionEnum.USER_ALREADY_EXIST);
        }

        UserRole role = UserRole.USER;
        // 필요시 어드민 토큰 사용
        // if (requestDto.isAdmin()) {
        //     if (!requestDto.getAdminToken().equals(adminToken)) {
        //         throw new AdminTokenNotValidException(ResponseExceptionEnum.)
        //     }
        // }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getUsername(), encodedPassword, requestDto.getName(), requestDto.getNickname(), requestDto.getEmail(), role);
        userRepository.save(user);
        return requestDto.getName();
    }



}
