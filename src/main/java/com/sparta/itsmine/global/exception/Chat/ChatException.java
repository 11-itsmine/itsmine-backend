package com.sparta.itsmine.global.exception.Chat;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class ChatException extends RuntimeException {

    private final ResponseExceptionEnum responseExceptionEnum;

    public ChatException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}
