package com.sparta.itsmine.domain.kakaopay.service;


import static com.sparta.itsmine.domain.product.utils.ProductStatus.BID;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.SUCCESS_BID;

import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.auction.service.AuctionService;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayApproveRequestDto;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayApproveResponseDto;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayCancelRequestDto;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayCancelResponseDto;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayReadyRequestDtd;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayReadyResponseDto;
import com.sparta.itsmine.domain.kakaopay.entity.KakaoPayTid;
import com.sparta.itsmine.domain.kakaopay.repository.KakaoPayRepository;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductAdapter;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kakaopay
 */
@Service
@RequiredArgsConstructor
public class KakaoPayService {

    @Value("${kakaopay.api.secret.key}")
    private String kakaopaySecretKey;

    @Value("${cid}")
    private String cid;

    @Value("${sample.host}")
    private String kakaopayHost;

    private String tid;

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final KakaoPayRepository kakaoPayRepository;
    private final AuctionService auctionService;
    private final ProductAdapter productAdapter;


    public KakaoPayReadyResponseDto ready(Long productId, User user, AuctionRequestDto requestDto) {
        // Request header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Product product = productAdapter.getProduct(productId);
        Integer bidPrice = requestDto.getBidPrice();
        //즉시구매가와 같이 않으면 1/10만 보증금으로 결재
        if (!bidPrice.equals(product.getAuctionNowPrice())) {
            bidPrice = requestDto.getBidPrice() / 10;
        }

        AuctionResponseDto createAuction = auctionService.createAuction(user, productId,
                requestDto);
        // Request param
        KakaoPayReadyRequestDtd kakaoPayReadyRequestDtd = KakaoPayReadyRequestDtd.builder()
                .cid(cid)//가맹점 코드, 10자
                .partnerOrderId(product.getId())//가맹점 주문번호, 최대 100자
                .partnerUserId(user.getUsername())//가맹점 회원 id, 최대 100자
                .itemName(product.getProductName())//상품명, 최대 100자
                .quantity(1)//상품 수량
                .totalAmount(bidPrice)//상품 총액
                .taxFreeAmount(0)//상품 비과세 금액
                .vatAmount(0)//상품 부가세 금액
                .approvalUrl(kakaopayHost + "/approve/pc/popup/" + product.getId() + "/"
                        + user.getId() + "/"
                        + createAuction.getId())//결제 성공 시 redirect url, 최대 255자 ,
                .cancelUrl(kakaopayHost + "/cancel/pc/popup")//결제 취소 시 redirect url, 최대 255자
                .failUrl(kakaopayHost + "/fail/pc/popup")//결제 실패 시 redirect url, 최대 255자
                .build();

        // Send reqeust
        HttpEntity<KakaoPayReadyRequestDtd> entityMap = new HttpEntity<>(kakaoPayReadyRequestDtd,
                headers);

        ResponseEntity<KakaoPayReadyResponseDto> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                entityMap,
                KakaoPayReadyResponseDto.class
        );
        KakaoPayReadyResponseDto kakaoPayReadyResponseDto = response.getBody();

        // 주문번호와 TID를 매핑해서 저장해놓는다.
        // Mapping TID with partner_order_id then save it to use for approval request.
        this.tid = kakaoPayReadyResponseDto.getTid();
        return kakaoPayReadyResponseDto;
    }

    public ResponseEntity<KakaoPayApproveResponseDto> approve(String pgToken, Long productId,
            Long userId, Long auctionId) {
        // ready할 때 저장해놓은 TID로 승인 요청
        // Call “Execute approved payment” API by pg_token, TID mapping to the current payment transaction and other parameters.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Auction auction = auctionRepository.findById(auctionId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        // Request param
        KakaoPayApproveRequestDto kakaoPayApproveRequestDto = KakaoPayApproveRequestDto.builder()
                .cid(cid)//가맹점 코드, 10자
                .tid(tid)//결제 고유번호, 결제 준비 API 응답에 포함
                .partnerOrderId(product.getId())//가맹점 주문번호, 결제 준비 API 요청과 일치해야 함
                .partnerUserId(user.getUsername())//가맹점 회원 id, 결제 준비 API 요청과 일치해야 함
                .pgToken(
                        pgToken)//결제승인 요청을 인증하는 토큰 사용자 결제 수단 선택 완료 시, approval_url로 redirection 해줄 때 pg_token을 query string으로 전달
                .build();

        KakaoPayTid KakaoPayTid = new KakaoPayTid(cid, kakaoPayApproveRequestDto.getTid(),
                product.getId(), user.getUsername(), pgToken, auction);
        kakaoPayRepository.save(KakaoPayTid);

        if (auction.getBidPrice().equals(product.getAuctionNowPrice())) {
            auction.updateStatus(SUCCESS_BID);
            auctionRepository.save(auction);
            auctionService.currentPriceUpdate(auction.getBidPrice(), product);
            auctionService.successfulAuction(productId);
        } else {
            auction.updateStatus(BID);
            auctionRepository.save(auction);
            auctionService.currentPriceUpdate(auction.getBidPrice(), product);
            auctionService.scheduleMessage(productId, product.getDueDate());
        }

        // Send Request
        HttpEntity<KakaoPayApproveRequestDto> entityMap = new HttpEntity<>(
                kakaoPayApproveRequestDto, headers);

        ResponseEntity<KakaoPayApproveResponseDto> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                entityMap,
                KakaoPayApproveResponseDto.class
        );

        return response;
    }

    public KakaoPayCancelResponseDto kakaoCancel(String tid) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        KakaoPayTid kakaoPayTid = kakaoPayRepository.findByTid(tid);

        // Request param
        KakaoPayCancelRequestDto kakaoPayCancelRequestDto = KakaoPayCancelRequestDto.builder()
                .cid(kakaoPayTid.getCid())//가맹점 코드, 10자
                .tid(tid)//결제 고유번호, 20자
                .cancel_amount(kakaoPayTid.getAuction().getBidPrice())//취소 금액
                .cancel_tax_free_amount(0)//취소 비과세 금액
                .cancel_vat_amount(0)//취소 부가세 금액
                .build();

        // Send Request
        HttpEntity<KakaoPayCancelRequestDto> entityMap = new HttpEntity<>(kakaoPayCancelRequestDto,
                headers);

        KakaoPayCancelResponseDto response = new RestTemplate().postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/cancel",
                entityMap,
                KakaoPayCancelResponseDto.class);

        kakaoPayRepository.delete(kakaoPayTid);
        return response;
    }

}
