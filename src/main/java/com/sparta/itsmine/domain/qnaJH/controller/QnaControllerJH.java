package com.sparta.itsmine.domain.qnaJH.controller;

import com.sparta.itsmine.domain.qnaJH.dto.QnaRequestDtoJH;
import com.sparta.itsmine.domain.qnaJH.service.QnaServiceJH;
import com.sparta.itsmine.global.common.HttpResponseDto;
import com.sparta.itsmine.global.common.ResponseCodeEnum;
import com.sparta.itsmine.global.common.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QnaControllerJH {

    private final QnaServiceJH qnaService;

    // qna 생성
    @PostMapping("/qna")
    public ResponseEntity<HttpResponseDto> addQna(
            @RequestBody QnaRequestDtoJH requestDto) {

        qnaService.addQna(requestDto);
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_CREATE);
    }
}
