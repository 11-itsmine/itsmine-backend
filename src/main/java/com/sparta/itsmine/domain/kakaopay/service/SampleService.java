package com.sparta.itsmine.domain.kakaopay.service;


import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.service.AuctionService;
import com.sparta.itsmine.domain.kakaopay.dto.ApproveRequest;
import com.sparta.itsmine.domain.kakaopay.dto.ReadyRequest;
import com.sparta.itsmine.domain.kakaopay.dto.ReadyResponse;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kakaopay
 */
@Service
@RequiredArgsConstructor
public class SampleService {

    @Value("${kakaopay.api.secret.key}")
    private String kakaopaySecretKey;

    @Value("${cid}")
    private String cid;

    @Value("${sample.host}")
    private String sampleHost;

    private String tid;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AuctionService auctionService;


    public ReadyResponse ready(Long productId, User user, AuctionRequestDto requestDto) {
        // Request header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Product product = productRepository.findById(productId).orElseThrow();

        // Request param
        ReadyRequest readyRequest = ReadyRequest.builder()
                .cid(cid)//가맹점 코드, 10자
                .partnerOrderId(product.getId())//가맹점 주문번호, 최대 100자
                .partnerUserId(user.getUsername())//가맹점 회원 id, 최대 100자
                .itemName(product.getProductName())//상품명, 최대 100자
                .quantity(1)//상품 수량
                .totalAmount(requestDto.getBidPrice())//상품 총액
                .taxFreeAmount(0)//상품 비과세 금액
                .vatAmount(0)//상품 부가세 금액
                .approvalUrl(sampleHost + "/approve/pc/layer/" + product.getId() + "/"
                        + user.getId())//결제 성공 시 redirect url, 최대 255자 ,
                // 여기다 유저정보를 덧붙여서 호출
                // "/approve" + "?productId="+productId+"&userId="+userId
                //여기에 경매ID 추가
                .cancelUrl(sampleHost + "/cancel/pc/layer")//결제 취소 시 redirect url, 최대 255자
                .failUrl(sampleHost + "/fail/pc/layer")//결제 실패 시 redirect url, 최대 255자
                .build();

        // Send reqeust
        HttpEntity<ReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);

        //헤더 jwt 토큰 추가

        ResponseEntity<ReadyResponse> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                entityMap,
                ReadyResponse.class
        );
        ReadyResponse readyResponse = response.getBody();

        // 주문번호와 TID를 매핑해서 저장해놓는다.
        // Mapping TID with partner_order_id then save it to use for approval request.
        this.tid = readyResponse.getTid();
        return readyResponse;
    }

    public String approve(String pgToken, Long productId, Long userId) {
        // ready할 때 저장해놓은 TID로 승인 요청
        // Call “Execute approved payment” API by pg_token, TID mapping to the current payment transaction and other parameters.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Product product = productRepository.findById(productId).orElseThrow();
        User user=userRepository.findById(userId).orElseThrow();

        // Request param
        ApproveRequest approveRequest = ApproveRequest.builder()
                .cid(cid)//가맹점 코드, 10자
                .tid(tid)//결제 고유번호, 결제 준비 API 응답에 포함
                .partnerOrderId(product.getId())//가맹점 주문번호, 결제 준비 API 요청과 일치해야 함
                .partnerUserId(user.getUsername())//가맹점 회원 id, 결제 준비 API 요청과 일치해야 함
                .pgToken(
                        pgToken)//결제승인 요청을 인증하는 토큰 사용자 결제 수단 선택 완료 시, approval_url로 redirection 해줄 때 pg_token을 query string으로 전달
                .build();

        // Send Request
        HttpEntity<ApproveRequest> entityMap = new HttpEntity<>(approveRequest, headers);
        try {
            ResponseEntity<String> response = new RestTemplate().postForEntity(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    entityMap,
                    String.class
            );

            // 승인 결과를 저장한다.
            // save the result of approval
//            String approveResponse = response.getBody();
//            return approveResponse;

            //입찰 생성 서비스를 여기서 호출하면 AuctionRequestDto는 어떻게 받아오지?
            String approveResponse = response.getBody();
            return approveResponse;
        } catch (HttpStatusCodeException ex) {
            return ex.getResponseBodyAsString();
        }
    }
}
