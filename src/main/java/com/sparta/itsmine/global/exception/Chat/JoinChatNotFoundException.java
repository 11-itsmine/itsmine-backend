package com.sparta.itsmine.global.exception.Chat;


import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class JoinChatNotFoundException extends ChatException {

    public JoinChatNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }

}
