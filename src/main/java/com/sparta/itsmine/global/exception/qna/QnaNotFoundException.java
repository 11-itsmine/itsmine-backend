package com.sparta.itsmine.global.exception.qna;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class QnaNotFoundException extends QnaException {

    public QnaNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
