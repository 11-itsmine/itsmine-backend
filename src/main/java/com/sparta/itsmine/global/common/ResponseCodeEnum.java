package com.sparta.itsmine.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCodeEnum {
    // 유저
    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 완료했습니다."),
    USER_SUCCESS_SIGNUP(HttpStatus.OK, "님의 회원가입을 완료 했습니다."),
    REISSUE_ACCESS_TOKEN(HttpStatus.OK, "억세스 토큰 재발급을 완료했습니다."),
    //QnA
    SUCCESS_CREATE_QNA(HttpStatus.CREATED, "문의 내용이 등록 되었습니다."),
    SUCCESS_QNA_LIST(HttpStatus.OK, "문의 목록 조회"),
    NULL_QNA_LIST(HttpStatus.OK, "문의 내용은 없을수도 있습니다."),
    SUCCESS_GET_QNA(HttpStatus.OK, "문의 내용 입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}