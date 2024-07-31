package com.sparta.itsmine.global.exception.product;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.CommonException;

public class ProductInDateException extends CommonException {

    public ProductInDateException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
