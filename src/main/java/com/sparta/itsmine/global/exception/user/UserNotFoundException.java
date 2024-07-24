package com.sparta.itsmine.global.exception.user;


import com.sparta.itsmine.global.common.ResponseExceptionEnum;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }

}