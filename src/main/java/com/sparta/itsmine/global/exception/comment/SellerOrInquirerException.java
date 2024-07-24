package com.sparta.itsmine.global.exception.comment;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;

public class SellerOrInquirerException extends CommentException{
    public SellerOrInquirerException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
