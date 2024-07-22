package com.sparta.itsmine.global.exception.comment;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;

public class QnaNotFoundExceptionJH extends CommentException {
    public QnaNotFoundExceptionJH(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
