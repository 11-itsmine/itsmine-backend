package com.sparta.itsmine.global.exception;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class DataDuplicatedException extends CommonException {

    public DataDuplicatedException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
