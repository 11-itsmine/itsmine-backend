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

    // 상품
    SUCCESS_SAVE_PRODUCT(HttpStatus.OK, "성공적으로 상품을 등록했습니다."),
    SUCCESS_TO_SEARCH_PRODUCTS(HttpStatus.OK, "고객님의 상품 조회가 완료되었습니다."),
    SUCCESS_TO_UPDATE(HttpStatus.OK, "상품 수정 완료!"),
    SUCCESS_DELETE_PRODUCT(HttpStatus.OK, "상품 삭제 완료!"),
    SUCCESS_TO_LIKE(HttpStatus.OK, "좋아요가 완료 되었습니다"),
    SUCCESS_TO_REMOVE_LIKE(HttpStatus.OK, "좋아요가 취소 되었습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}