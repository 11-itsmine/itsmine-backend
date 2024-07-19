package com.sparta.itsmine.global.exception.user;


import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import jdk.jshell.spi.ExecutionControl.UserException;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}