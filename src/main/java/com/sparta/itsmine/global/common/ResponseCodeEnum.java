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


    //댓글
    COMMENT_SUCCESS_CREATE(HttpStatus.OK, "댓글 작성이 완료 되었습니다."),
    COMMENT_SUCCESS_GET(HttpStatus.OK, "댓글 조회가 완료 되었습니다."),
    COMMENT_SUCCESS_UPDATE(HttpStatus.OK,"댓글 수정이 완료 되었습니다."),
    COMMENT_SUCCESS_DELETE(HttpStatus.OK,"댓글 삭제가 완료 되었습니다."),

    //경매
    AUCTION_SUCCESS_CREATE(HttpStatus.CREATED, "경매 생성이 완료 되었습니다."),
    AUCTION_SUCCESS_GET(HttpStatus.OK, "경매 조회가 완료 되었습니다."),
    AUCTION_SUCCESS_DELETE_SUCCESSFULAUCTION(HttpStatus.OK,"낙찰 되었습니다."),
    AUCTION_SUCCESS_DELETE_AVOIDEDAUCTION(HttpStatus.OK,"유찰 되었습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}