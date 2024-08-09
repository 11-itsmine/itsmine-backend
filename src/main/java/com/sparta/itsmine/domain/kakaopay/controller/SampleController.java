package com.sparta.itsmine.domain.kakaopay.controller;


import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.kakaopay.dto.ReadyResponse;
import com.sparta.itsmine.domain.kakaopay.service.SampleService;
import com.sparta.itsmine.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kakaopay
 */
@RestController
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @GetMapping("/ready/{productId}")//결재 요청
    public ReadyResponse ready(@PathVariable("productId") Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,@RequestBody AuctionRequestDto requestDto) {
        ReadyResponse readyResponse = sampleService.ready(productId, userDetails.getUser(),requestDto);
        //여기서 값을 받고? 필터처리
        return readyResponse;
    }

    @GetMapping("/approve/{agent}/{openType}/{productId}/{userId}/{auctionId}")//결재 승인,옥션 결재상태 확인
    public String approve(@PathVariable("agent") String agent,
            @PathVariable("openType") String openType, @RequestParam("pg_token") String pgToken,
            @PathVariable("productId") Long productId, @PathVariable("userId") Long userId) {
        String approveResponse = sampleService.approve(pgToken, productId, userId);
        return agent + "/" + openType + "/approve";
    }

    @GetMapping("/cancel/{agent}/{openType}")//결재 취소
    public String cancel(@PathVariable("agent") String agent,
            @PathVariable("openType") String openType) {
        // 주문건이 진짜 취소되었는지 확인 후 취소 처리
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is QUIT_PAYMENT before suspending the payment
        return agent + "/" + openType + "/cancel";
    }

    @GetMapping("/fail/{agent}/{openType}")//결재 실패
    public String fail(@PathVariable("agent") String agent,
            @PathVariable("openType") String openType) {
        // 주문건이 진짜 실패되었는지 확인 후 실패 처리
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is FAIL_PAYMENT before suspending the payment
        return agent + "/" + openType + "/fail";
    }
}
