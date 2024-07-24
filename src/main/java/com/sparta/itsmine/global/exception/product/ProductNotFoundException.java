package com.sparta.itsmine.global.exception.product;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;

public class ProductNotFoundException extends ProductException {

    public ProductNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
