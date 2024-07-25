package com.sparta.itsmine.global.exception;


import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class DataNotFoundException extends CommonException {

    public DataNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
