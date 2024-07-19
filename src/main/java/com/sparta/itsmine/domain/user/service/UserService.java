package com.sparta.itsmine.domain.user.service;


import com.sparta.itsmine.domain.user.dto.SignupRequestDto;
import com.sparta.itsmine.domain.user.repository.UserAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private UserAdapter adapter;

    public String signup(SignupRequestDto requestDto) {
        return adapter.findUserByUsername(requestDto.getUsername()).getUsername();
    }

}
