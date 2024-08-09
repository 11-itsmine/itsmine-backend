package com.sparta.itsmine.domain.kakaopay.controller;


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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kakaopay
 */
@Controller
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @GetMapping("/ready/{agent}/{openType}/{productId}")//결재 요청
    public String ready(@PathVariable("agent") String agent,
            @PathVariable("openType") String openType, @PathVariable("productId") Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,//사용자를 식별할 수 있는 번호
            Model model) {
        ReadyResponse readyResponse = sampleService.ready(agent, openType, productId,
                userDetails.getUser());

        if (agent.equals("mobile")) {
            // 모바일은 결제대기 화면으로 redirect 한다.
            // In mobile, redirect to payment stand-by screen
            return "redirect:" + readyResponse.getNext_redirect_mobile_url();
        }

        if (agent.equals("app")) {
            // 앱에서 결제대기 화면을 올리는 webview 스킴
            // In app, webview app scheme for payment stand-by screen
            model.addAttribute("webviewUrl",
                    "app://webview?url=" + readyResponse.getNext_redirect_app_url());
            return "app/webview/ready";
        }

        // pc
        model.addAttribute("response", readyResponse);
        return agent + "/" + openType + "/ready";
    }

    @GetMapping("/approve/{agent}/{openType}/{productId}/{userId}")//결재 승인
    public String approve(@PathVariable("agent") String agent,
            @PathVariable("openType") String openType, @RequestParam("pg_token") String pgToken,
            @PathVariable("productId") Long productId, @PathVariable("userId") Long userId, Model model) {
        String approveResponse = sampleService.approve(pgToken, productId, userId);
        model.addAttribute("response", approveResponse);
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
