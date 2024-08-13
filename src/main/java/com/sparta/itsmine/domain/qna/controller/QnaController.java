package com.sparta.itsmine.domain.qna.controller;

import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.NULL_QNA_LIST;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_CREATE_QNA;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_DELETE_QNA;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_QNA_LIST;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_UPDATE_QNA;
import static com.sparta.itsmine.global.common.response.ResponseUtils.of;

import com.sparta.itsmine.domain.qna.dto.QnAChangeRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.itsmine.domain.qna.dto.GetQnaResponseDto;
import com.sparta.itsmine.domain.qna.dto.QnaRequestDto;
import com.sparta.itsmine.domain.qna.service.QnaService;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.response.HttpResponseDto;
import com.sparta.itsmine.global.common.response.ResponseUtils;
import com.sparta.itsmine.global.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products/{productId}/qnas")
public class QnaController {

    private final QnaService qnaService;

    /**
     * Qna 생성 합니다.
     *
     * @param requestDto  qna 생성을 위한 정보
     * @param userDetails 인가된 유저 정보
     */
    @PostMapping
    public ResponseEntity<HttpResponseDto> createQna(
            @PathVariable("productId") Long productId,
            @Valid @RequestBody QnaRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        return of(SUCCESS_CREATE_QNA, qnaService.createQna(productId, requestDto, user));
    }

    /**
     * 상품의 QNA 정보를 불러 옵니다.
     *
     * @param productId 상품 정보의 ID 입니다 해당 ID의 문의 내용을 불러옵니다.
     */
    @GetMapping
    public ResponseEntity<HttpResponseDto> getQnaList(
            @PathVariable Long productId,
            Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        Page<GetQnaResponseDto> qnaList = qnaService.getQnaList(productId, pageable,
                userDetails.getUser());

        return qnaList == null ? of(NULL_QNA_LIST)
                : of(SUCCESS_QNA_LIST, qnaList.getContent());
    }

    /**
     * 상품의 하나의 QNA 정보를 불러 옵니다.
     *
     * @param productId 상품 정보의 ID 입니다 해당 ID의 문의 내용을 불러옵니다.
     * @param qnaId     Qna 고유 ID
     */
    @GetMapping("/{qnaId}")
    public ResponseEntity<HttpResponseDto> getQna(
            @PathVariable Long productId,
            @PathVariable Long qnaId
    ) {
        return ResponseUtils.of(SUCCESS_QNA_LIST, qnaService.getQna(productId, qnaId));
    }

    /**
     * Qna 수정
     *
     * @param qnaId       QnA고유 ID
     * @param requestDto  qna 생성을 위한 정보
     * @param userDetails 인가된 유저 정보
     */

    @PutMapping("/{qnaId}")
    public ResponseEntity<HttpResponseDto> updateQna(
            @PathVariable("qnaId") Long qnaId,
            @PathVariable("productId") Long productId,
            @RequestBody QnaRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return of(SUCCESS_UPDATE_QNA, qnaService.updateQna(qnaId, productId, requestDto, user));
    }

    /**
     * Qna 삭제
     *
     * @param qnaId       QnA고유 ID
     * @param productId   상품 고유 ID
     * @param userDetails 인가된 유저 정보
     */
    @DeleteMapping("/{qnaId}")
    public ResponseEntity<HttpResponseDto> deleteQna(
            @PathVariable("productId") Long productId,
            @PathVariable("qnaId") Long qnaId,
            @RequestBody QnAChangeRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        qnaService.deleteQna(productId, qnaId, requestDto.getPassword() ,user);
        return of(SUCCESS_DELETE_QNA);
    }
}
