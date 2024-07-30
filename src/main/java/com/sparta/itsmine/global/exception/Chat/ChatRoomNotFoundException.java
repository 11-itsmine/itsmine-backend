package com.sparta.itsmine.global.exception.Chat;


import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class ChatRoomNotFoundException extends ChatException {

    public ChatRoomNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
