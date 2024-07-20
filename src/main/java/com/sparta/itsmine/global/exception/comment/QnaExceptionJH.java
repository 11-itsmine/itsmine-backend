package com.sparta.itsmine.global.exception.comment;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class QnaExceptionJH extends RuntimeException{

    private final ResponseExceptionEnum responseExceptionEnum;

    public QnaExceptionJH(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}
