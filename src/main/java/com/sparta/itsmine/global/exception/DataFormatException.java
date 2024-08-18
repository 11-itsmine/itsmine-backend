package com.sparta.itsmine.global.exception;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class DataFormatException extends CommonException {

    public DataFormatException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
