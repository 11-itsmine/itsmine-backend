package com.sparta.itsmine.global.exception.comment;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class CommentEqualSellerException extends CommentException{
    public CommentEqualSellerException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
