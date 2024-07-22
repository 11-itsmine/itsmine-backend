package com.sparta.itsmine.global.exception.qna;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;

public class QnaCheckUserException extends QnaException {

    public QnaCheckUserException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
