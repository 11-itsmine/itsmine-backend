package com.sparta.itsmine.global.exception.productimages;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.comment.CommentException;

public class InvalidURLException extends CommentException {
    public InvalidURLException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
