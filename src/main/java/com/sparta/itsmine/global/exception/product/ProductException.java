package com.sparta.itsmine.global.exception.product;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class ProductException extends RuntimeException {

    private final ResponseExceptionEnum responseExceptionEnum;

    public ProductException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}