package com.sparta.itsmine.domain.qna.controller;

import static com.sparta.itsmine.global.common.ResponseCodeEnum.SUCCESS_CREATE_QNA;

import com.sparta.itsmine.domain.qna.dto.CreateQnaRequestDTO;
import com.sparta.itsmine.domain.qna.service.QnaService;
import com.sparta.itsmine.global.common.HttpResponseDto;
import com.sparta.itsmine.global.common.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/{productId}/qnas")
public class QnaController {

    private final QnaService qnaService;

    @PostMapping
    public ResponseEntity<HttpResponseDto> createQna(
            @PathVariable Long productId,
            @Valid @RequestBody CreateQnaRequestDTO requestDTO
    ) {
        qnaService.createQna(productId, requestDTO);
        return ResponseUtils.of(SUCCESS_CREATE_QNA);
    }

}
