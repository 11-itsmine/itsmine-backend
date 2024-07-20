package com.sparta.itsmine.global.exception.comment;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class QnaExceptionJH extends CommentException{
    public QnaExceptionJH(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
