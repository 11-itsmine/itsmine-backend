package com.sparta.itsmine.global.exception.comment;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class CommentNotFoundException extends CommentException{
    public CommentNotFoundException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
