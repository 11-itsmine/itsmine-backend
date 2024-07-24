package com.sparta.itsmine.global.exception.category;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class CategoryNotFoundException extends CategoryException {

    public CategoryNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }

}
