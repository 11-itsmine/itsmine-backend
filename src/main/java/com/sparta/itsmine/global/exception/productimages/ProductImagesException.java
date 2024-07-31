package com.sparta.itsmine.global.exception.productimages;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class ProductImagesException extends RuntimeException{
    private final ResponseExceptionEnum responseExceptionEnum;
    public ProductImagesException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}
