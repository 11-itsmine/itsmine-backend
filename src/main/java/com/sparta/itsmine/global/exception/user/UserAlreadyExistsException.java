package com.sparta.itsmine.global.exception.user;


import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }

}