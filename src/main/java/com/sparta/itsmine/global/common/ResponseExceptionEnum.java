package com.sparta.itsmine.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseExceptionEnum {
    // 유저
    USER_ERROR(HttpStatus.BAD_REQUEST, "유저 오류 발생"),
    REFRESH_TOKEN_UNAVAILABLE(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레쉬토큰 입니다."),
    USER_FAIL_SIGNUP(HttpStatus.BAD_REQUEST, "회원가입에 실패했습니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "중복된 아이디 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    USER_DELETED(HttpStatus.UNAUTHORIZED, "탈퇴한 사용자입니다"),
    USER_NOT_DELETED(HttpStatus.BAD_REQUEST, "탈퇴되지 않은 사용자입니다."),
    NOT_FOUND_AUTHENTICATION_INFO(HttpStatus.BAD_REQUEST, "사용자 정보가 일치하지 않습니다. 다시 시도해 주세요 :)"),
    INVALID_REFRESHTOKEN(HttpStatus.NOT_FOUND, "유효하지 않은 리프레쉬 토큰입니다."),
    FAIL_TO_CHANGE_ROLE(HttpStatus.BAD_REQUEST, "Role 변경을 실패했습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    //QNA
    QNA_ERROR(HttpStatus.BAD_REQUEST, "문의 오류 발생"),
    QNA_NOT_FOUND(HttpStatus.NOT_FOUND, "문의 내용이 존재 하지 않습니다."),
    QNA_USER_NOT_VALID(HttpStatus.BAD_REQUEST, "문의 내용 작성자가 아닙니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    USER_MISMATCH(HttpStatus.BAD_REQUEST, "유저가 일치하지 않습니다"),

    // 댓글
    COMMENT_EQUAL_SELLER(HttpStatus.BAD_REQUEST, "해당 상품 판매자만 접근이 가능합니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    QNAJH_NOT_FOUND(HttpStatus.NOT_FOUND, "문의 사항을 찾을 수 없습니다."),
    COMMENT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "해당 문의사항에는 이미 댓글이 작성 되어 있습니다."),

    //경매
    AUCTION_IMPOSSIBLE_BID(HttpStatus.BAD_REQUEST,"상품의 입찰가보다 낮거나 즉시구매가보다 높은 입찰가입니다 입찰 금액을 확인해주세요."),
    AUCTION_NOT_FOUND(HttpStatus.NOT_FOUND,"입찰 기록을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
