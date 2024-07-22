package com.sparta.itsmine.global.exception.category;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class CategoryException extends RuntimeException {

    private final ResponseExceptionEnum responseExceptionEnum;

    public CategoryException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}