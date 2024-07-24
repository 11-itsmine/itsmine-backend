package com.sparta.itsmine.global.exception.comment;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class CommentAlreadyExistsException extends CommentException{
    public CommentAlreadyExistsException(ResponseExceptionEnum responseCodeEnum) {super(responseCodeEnum);}
}
