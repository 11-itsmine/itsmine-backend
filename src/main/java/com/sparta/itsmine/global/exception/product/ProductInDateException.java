package com.sparta.itsmine.global.exception.product;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;

public class ProductInDateException extends ProductException{

    public ProductInDateException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }

}