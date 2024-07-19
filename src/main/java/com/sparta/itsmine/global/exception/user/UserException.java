package com.sparta.itsmine.global.exception.user;


import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final ResponseExceptionEnum responseExceptionEnum;

    public UserException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}