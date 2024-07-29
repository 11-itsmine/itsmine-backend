package com.sparta.itsmine.global.exception;

import static com.sparta.itsmine.global.common.response.ResponseUtils.of;

import com.sparta.itsmine.global.common.response.HttpResponseDto;
import com.sparta.itsmine.global.exception.category.CategoryException;
import com.sparta.itsmine.global.exception.product.ProductException;
import com.sparta.itsmine.global.exception.qna.QnaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    // 공통된 오류 처리 로직
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<HttpResponseDto> handleUserException(CommonException e) {
        log.error("에러 메세지: ", e);
        return of(e.getResponseExceptionEnum());
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<HttpResponseDto> handlerProductException(ProductException e) {
        log.error("에러 메세지: ", e);
        return of(e.getResponseExceptionEnum());
    }

    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<HttpResponseDto> handlerCategoryException(CategoryException e) {
        log.error("에러 메세지: ", e);
        return of(e.getResponseExceptionEnum());
    }

    @ExceptionHandler(QnaException.class)
    public ResponseEntity<HttpResponseDto> handleUserException(QnaException e) {
        log.error("에러 메세지: ", e);
        return of(e.getResponseExceptionEnum());
    }
}