package com.sparta.itsmine.global.exception;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class DateDuplicatedException extends CommonException {

    public DateDuplicatedException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
