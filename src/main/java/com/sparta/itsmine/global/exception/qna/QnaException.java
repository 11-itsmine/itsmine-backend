package com.sparta.itsmine.global.exception.qna;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class QnaException extends RuntimeException {

    private final ResponseExceptionEnum responseExceptionEnum;

    public QnaException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }

}
