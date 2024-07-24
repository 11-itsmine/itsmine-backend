package com.sparta.itsmine.global.exception.comment;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class CommentException extends RuntimeException{
    private final ResponseExceptionEnum responseExceptionEnum;
    public CommentException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}
