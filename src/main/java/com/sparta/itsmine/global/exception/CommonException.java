package com.sparta.itsmine.global.exception;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final ResponseExceptionEnum responseExceptionEnum;

    public CommonException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }

}
