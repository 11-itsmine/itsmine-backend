package com.sparta.itsmine.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCodeEnum {
    // 유저
    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 완료했습니다."),
    USER_SIGNUP_SUCCESS(HttpStatus.OK, "님의 회원가입을 완료 했습니다."),
    REISSUE_ACCESS_TOKEN(HttpStatus.OK, "액세스 토큰 재발급을 완료했습니다."),
    SUCCESS_LOGOUT(HttpStatus.OK, "로그아웃을 완료했습니다."),
    USER_SUCCESS_GET(HttpStatus.OK, "유저 조회를 완료 했습니다."),
    USER_DELETE_SUCCESS(HttpStatus.OK, "회원 탈퇴를 완료했습니다."),
    USER_RESIGN_SUCCESS(HttpStatus.OK, "회원 복구를 완료했습니다."),
    USER_UPDATE_SUCCESS(HttpStatus.OK, "유저 정보 수정을 완료했습니다."),
    USER_SUCCESS_SIGNUP(HttpStatus.OK, "님의 회원가입을 완료 했습니다."),

    // 상품
    SUCCESS_SAVE_PRODUCT(HttpStatus.OK, "성공적으로 상품을 등록했습니다."),
    SUCCESS_TO_SEARCH_PRODUCTS(HttpStatus.OK, "고객님의 상품 조회가 완료되었습니다."),
    SUCCESS_TO_UPDATE(HttpStatus.OK, "상품 수정 완료!"),
    SUCCESS_DELETE_PRODUCT(HttpStatus.OK, "상품 삭제 완료!"),
    SUCCESS_TO_LIKE(HttpStatus.OK, "좋아요가 완료 되었습니다"),
    SUCCESS_TO_REMOVE_LIKE(HttpStatus.OK, "좋아요가 취소 되었습니다."),

    // 카테고리
    SUCCESS_TO_MAKE_NEW_CATEGORY(HttpStatus.OK, "새로운 카테고리를 만드셨습니다."),

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