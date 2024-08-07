package com.sparta.itsmine.domain.user.controller;


import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_LOGOUT;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.USER_DELETE_SUCCESS;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.USER_RESIGN_SUCCESS;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.USER_SUCCESS_GET;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.USER_SIGNUP_SUCCESS;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.USER_UPDATE_SUCCESS;
import static com.sparta.itsmine.global.common.response.ResponseUtils.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.itsmine.domain.social.kakao.service.KakaoService;
import com.sparta.itsmine.domain.user.dto.ProfileUpdateRequestDto;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.security.JwtProvider;
import com.sparta.itsmine.global.security.UserDetailsImpl;
import com.sparta.itsmine.domain.user.dto.SignupRequestDto;
import com.sparta.itsmine.domain.user.dto.UserResponseDto;
import com.sparta.itsmine.domain.user.service.UserService;
import com.sparta.itsmine.global.common.response.HttpResponseDto;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping
    public ResponseEntity<HttpResponseDto> signup(
            @RequestBody SignupRequestDto requestDto
    ) {
        String username = userService.signup(requestDto);
        return of(USER_SIGNUP_SUCCESS, username);
    }

    @GetMapping("/logout")
    public ResponseEntity<HttpResponseDto> logout(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        userService.logout(userDetails.getUsername());
        return of(SUCCESS_LOGOUT);
    }

    @GetMapping("/profile")
    public ResponseEntity<HttpResponseDto> getUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UserResponseDto response = userService.getUser(userDetails.getUser().getId());
        return of(USER_SUCCESS_GET, response);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<HttpResponseDto> withdraw(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        userService.withdraw(userDetails.getUser());
        return of(USER_DELETE_SUCCESS);
    }

    @PutMapping("/resign/{userId}")
    public ResponseEntity<HttpResponseDto> resign(
        @PathVariable Long userId
    ) {
        userService.resign(userId);
        return of(USER_RESIGN_SUCCESS);
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponseDto> update(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody ProfileUpdateRequestDto updateDto
    ) {
        userService.update(userDetails.getUser(), updateDto);
        return of(USER_UPDATE_SUCCESS);
    }

    @GetMapping("/user/kakao/callback")
    public String kakakoLogin(@RequestParam String code, HttpServletResponse response)
            throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code); // jwt 반환

        Cookie cookie = new Cookie(JwtProvider.AUTHORIZATION_HEADER, token.substring(7));

        cookie.setPath("/");
        response.addCookie(cookie); // 서블렛에 쿠리를 더해줍니다.

        return "redirect:/";

    }
}
