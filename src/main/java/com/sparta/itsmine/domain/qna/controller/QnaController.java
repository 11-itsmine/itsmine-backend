package com.sparta.itsmine.domain.qna.controller;

import static com.sparta.itsmine.global.common.ResponseCodeEnum.NULL_QNA_LIST;
import static com.sparta.itsmine.global.common.ResponseCodeEnum.SUCCESS_CREATE_QNA;
import static com.sparta.itsmine.global.common.ResponseCodeEnum.SUCCESS_QNA_LIST;

import com.sparta.itsmine.domain.qna.dto.CreateQnaRequestDTO;
import com.sparta.itsmine.domain.qna.entity.Qna;
import com.sparta.itsmine.domain.qna.service.QnaService;
import com.sparta.itsmine.domain.security.UserDetailsImpl;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.HttpResponseDto;
import com.sparta.itsmine.global.common.ResponseUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
            @Valid @RequestBody CreateQnaRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        qnaService.createQna(productId, requestDTO, user);
        return ResponseUtils.of(SUCCESS_CREATE_QNA);
    }

    @GetMapping
    public ResponseEntity<HttpResponseDto> getQnaList(
            @PathVariable Long productId
    ) {

        List<Qna> qnaList = qnaService.getQnaList(productId);

        return qnaList == null ? ResponseUtils.of(NULL_QNA_LIST)
                : ResponseUtils.of(SUCCESS_QNA_LIST, qnaList);
    }

    @GetMapping("/{qnaId}")
    public ResponseEntity<HttpResponseDto> getQna(
            @PathVariable Long productId,
            @PathVariable Long qnaId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Qna qna = qnaService.getQna(productId, qnaId);
    }
}
