package com.sparta.itsmine.domain.kakaopay.controller;


import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.AUCTION_BID_CANCEL;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.KAKAOPAY_APPROVE;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.KAKAOPAY_READY;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.KAKAOPAY_REFUND;

import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayApproveResponseDto;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayCancelResponseDto;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayReadyResponseDto;
import com.sparta.itsmine.domain.kakaopay.service.KakaoPayService;
import com.sparta.itsmine.global.common.response.HttpResponseDto;
import com.sparta.itsmine.global.common.response.ResponseUtils;
import com.sparta.itsmine.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kakaopay
 */
@RestController
@RequestMapping("/v1/kakaopay")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/ready/{productId}")//결제 요청
    public ResponseEntity<HttpResponseDto> ready(@PathVariable("productId") Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody AuctionRequestDto requestDto) {
        KakaoPayReadyResponseDto kakaoPayReadyResponseDto = kakaoPayService.ready(productId,
                userDetails.getUser(),
                requestDto);
        return ResponseUtils.of(KAKAOPAY_READY, kakaoPayReadyResponseDto);
    }

    @GetMapping("/approve/{agent}/{openType}/{productId}/{userId}/{auctionId}")//결제 승인,옥션 결제상태 확인
    public ResponseEntity<HttpResponseDto> approve(@PathVariable("agent") String agent,
            @PathVariable("openType") String openType, @RequestParam("pg_token") String pgToken,
            @PathVariable("productId") Long productId, @PathVariable("userId") Long userId,
            @PathVariable("auctionId") Long auctionId) {
        KakaoPayApproveResponseDto kakaoPayApproveResponseDto = kakaoPayService.approve(pgToken,
                productId, userId, auctionId);
        return ResponseUtils.of(KAKAOPAY_APPROVE, kakaoPayApproveResponseDto);
    }

    @GetMapping("/cancel/{agent}/{openType}")//결제 취소
    public String cancel(@PathVariable("agent") String agent,
            @PathVariable("openType") String openType) {
        // 주문건이 진짜 취소되었는지 확인 후 취소 처리
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is QUIT_PAYMENT before suspending the payment
        return "결제 취소 및 환불";
    }


    @GetMapping("/fail/{agent}/{openType}")//결제 실패
    public String fail(@PathVariable("agent") String agent,
            @PathVariable("openType") String openType) {
        // 주문건이 진짜 실패되었는지 확인 후 실패 처리
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is FAIL_PAYMENT before suspending the payment
        return "결제 실패 결제가 완료되지 않았습니다 다시 결제해주세요";
    }

    //결제 취소
    @PostMapping("/refund")
    public ResponseEntity<HttpResponseDto> refund(@RequestParam("tid") String tid) {

        KakaoPayCancelResponseDto kakaoPayCancelResponseDto = kakaoPayService.kakaoCancel(tid);

        return ResponseUtils.of(KAKAOPAY_REFUND, kakaoPayCancelResponseDto);
    }

    //환불 없는 입찰 취소
    @PostMapping("/bidCancel")
    public ResponseEntity<HttpResponseDto> bidCancel(@RequestParam("tid") String tid) {

        kakaoPayService.bidCancel(tid);

        return ResponseUtils.of(AUCTION_BID_CANCEL);
    }

}