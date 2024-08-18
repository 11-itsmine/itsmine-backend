package com.sparta.itsmine.domain.user.controller;

import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_CHANGE_PASSWORD;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_LOGIN;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_LOGOUT;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_TEMPORARY_PASSWORD;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.USER_DELETE_SUCCESS;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.USER_RESIGN_SUCCESS;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.USER_SIGNUP_SUCCESS;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.USER_SUCCESS_GET;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.USER_SUCCESS_LIST;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.USER_UPDATE_SUCCESS;
import static com.sparta.itsmine.global.common.response.ResponseUtils.of;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.itsmine.domain.social.kakao.service.KakaoService;
import com.sparta.itsmine.domain.user.dto.BlockResponseDto;
import com.sparta.itsmine.domain.user.dto.PasswordChangeRequest;
import com.sparta.itsmine.domain.user.dto.PasswordFindRequest;
import com.sparta.itsmine.domain.user.dto.ProfileUpdateRequestDto;
import com.sparta.itsmine.domain.user.dto.SignupRequestDto;
import com.sparta.itsmine.domain.user.dto.UserIdRequestDto;
import com.sparta.itsmine.domain.user.dto.UserResponseDto;
import com.sparta.itsmine.domain.user.dto.UserRoleDto;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.service.UserService;
import com.sparta.itsmine.global.common.response.HttpResponseDto;
import com.sparta.itsmine.global.common.response.ResponseUtils;
import com.sparta.itsmine.global.security.UserDetailsImpl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
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

    @PostMapping ("/find-password")
    public ResponseEntity<HttpResponseDto> findPassword(
            @RequestBody PasswordFindRequest passwordFindRequest
    ){
        String randomPassword = userService.findPassword(passwordFindRequest);
        return of(SUCCESS_TEMPORARY_PASSWORD, randomPassword);
    }

    @PostMapping("/change-password")
    public ResponseEntity<HttpResponseDto> changePassword(
            @RequestBody PasswordChangeRequest passwordChangeRequest) {

        userService.changePassword(passwordChangeRequest);

        return of(SUCCESS_CHANGE_PASSWORD);
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
            @PathVariable("userId") Long userId
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

    @GetMapping("/oauth/kakao")
    public ResponseEntity<HttpResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse res)
            throws JsonProcessingException {
        return of(SUCCESS_LOGIN, kakaoService.kakaoLogin(code, res));
    }

    @GetMapping("/list")
    private ResponseEntity<HttpResponseDto> getUserAllList(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<UserResponseDto> responseDtos = userService.getUserAllList(user);
        return ResponseUtils.of(USER_SUCCESS_LIST, responseDtos);
    }

    @GetMapping("/block-list")
    public ResponseEntity<HttpResponseDto> blockUserList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        User user = userDetails.getUser();
        List<BlockResponseDto> responseDtos = userService.blockUserList(user);
        return ResponseUtils.of(USER_SUCCESS_LIST, responseDtos);
    }

    @PutMapping("/unblock")
    public ResponseEntity<HttpResponseDto> unBlockUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserIdRequestDto requestDto
    ) {
        User user = userDetails.getUser();
        userService.unBlockUser(user, requestDto.getUserId());
        return ResponseUtils.of(USER_SUCCESS_LIST);
    }

    @GetMapping("/role")
    public UserRoleDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("user Role : {} ", userDetails.getUser().getUserRole());
        return new UserRoleDto(userDetails.getUsername(), userDetails.getUser().getUserRole());
    }

}
