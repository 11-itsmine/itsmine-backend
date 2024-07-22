package com.sparta.itsmine.global.exception;

import com.sparta.itsmine.global.common.HttpResponseDto;
import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import com.sparta.itsmine.global.common.ResponseUtils;
import com.sparta.itsmine.global.exception.qna.QnaException;
import jdk.jshell.spi.ExecutionControl.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<HttpResponseDto> handleUserException(UserException e) {
        log.error("에러 메세지: ", e);
        return ResponseUtils.of(ResponseExceptionEnum.USER_ERROR);
    }

    @ExceptionHandler(QnaException.class)
    public ResponseEntity<HttpResponseDto> handleUserException(QnaException e) {
        log.error("에러 메세지: ", e);
        return ResponseUtils.of(ResponseExceptionEnum.USER_ERROR);
    }
}