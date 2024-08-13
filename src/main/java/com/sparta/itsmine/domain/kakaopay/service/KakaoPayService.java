package com.sparta.itsmine.domain.kakaopay.service;

import static com.sparta.itsmine.domain.product.utils.ProductStatus.BID;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.NEED_PAY;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.SUCCESS_BID;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.auction.repository.AuctionAdapter;
import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.auction.service.AuctionService;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayApproveRequestDto;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayApproveResponseDto;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayCancelRequestDto;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayCancelResponseDto;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayGetTidRequestDto;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayGetTidResponseDto;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayReadyRequestDtd;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayReadyResponseDto;
import com.sparta.itsmine.domain.kakaopay.entity.KakaoPayTid;
import com.sparta.itsmine.domain.kakaopay.repository.KakaoPayRepository;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductAdapter;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.product.scheduler.MessageSenderService;
import com.sparta.itsmine.domain.redis.RedisService;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.repository.UserAdapter;
import com.sparta.itsmine.domain.user.repository.UserRepository;
import com.sparta.itsmine.domain.user.utils.UserRole;
import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.DataDuplicatedException;

import lombok.RequiredArgsConstructor;

/**
 * Created by kakaopay
 */
@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final RedisTemplate redisTemplate;
    private final RedisService redisService;
    @Value("${kakaopay.api.secret.key}")
    private String kakaopaySecretKey;

    @Value("${cid}")
    private String cid;

    @Value("${sample.host}")
    private String kakaopayHost;

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final KakaoPayRepository kakaoPayRepository;
    private final AuctionService auctionService;
    private final MessageSenderService messageSenderService;
    private final ProductAdapter productAdapter;
    private final AuctionAdapter auctionAdapter;
    private final UserAdapter userAdapter;


    public KakaoPayReadyResponseDto ready(Long productId, User user, AuctionRequestDto requestDto) {

        //ban 상태의 유저는 입찰 못하게 차단
        user.checkBlock();

        // Request header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Product product = productAdapter.getProduct(productId);
        Integer bidPrice = requestDto.getBidPrice();
        //즉시구매가와 같이 않으면 1/10만 보증금으로 결제
        if (!bidPrice.equals(product.getAuctionNowPrice())) {
            bidPrice = requestDto.getBidPrice() / 10;
        }

        AuctionResponseDto createAuction = auctionService.createAuction(user, productId,
                requestDto, bidPrice);
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
                .approvalUrl(kakaopayHost + "/v1/kakaopay/approve/pc/layer/" + product.getId() + "/"
                        + user.getId() + "/"
                        + createAuction.getId())//결제 성공 시 redirect url, 최대 255자 ,
                .cancelUrl(kakaopayHost
                        + "/v1/kakaopay/cancel/pc/layer")//결제 취소 시 redirect url, 최대 255자
                .failUrl(kakaopayHost + "/v1/kakaopay/fail/pc/layer")//결제 실패 시 redirect url, 최대 255자
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
        // this.tid = kakaoPayReadyResponseDto.getTid();
        redisService.saveKakaoTid(user.getUsername(), kakaoPayReadyResponseDto.getTid());
        return kakaoPayReadyResponseDto;
    }

    public KakaoPayApproveResponseDto approve(String pgToken, Long productId,
            Long userId, Long auctionId) {
        // ready할 때 저장해놓은 TID로 승인 요청
        // Call “Execute approved payment” API by pg_token, TID mapping to the current payment transaction and other parameters.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Auction auction = auctionAdapter.getAuction(auctionId);
        Product product = productAdapter.getProduct(productId);
        User user = userAdapter.findById(userId);
        String tid = (String)redisTemplate.opsForValue().get(user.getUsername()+":tid");
        // Request param
        KakaoPayApproveRequestDto kakaoPayApproveRequestDto = KakaoPayApproveRequestDto.builder()
                .cid(cid)//가맹점 코드, 10자
                .tid(tid)//결제 고유번호, 결제 준비 API 응답에 포함
                .partnerOrderId(product.getId())//가맹점 주문번호, 결제 준비 API 요청과 일치해야 함
                .partnerUserId(user.getUsername())//가맹점 회원 id, 결제 준비 API 요청과 일치해야 함
                .pgToken(
                        pgToken)//결제승인 요청을 인증하는 토큰 사용자 결제 수단 선택 완료 시, approval_url로 redirection 해줄 때 pg_token을 query string으로 전달
                .build();

        KakaoPayTid KakaoPayTid = new KakaoPayTid(cid, tid,
                product.getId(), user.getUsername(), pgToken, auction);
        kakaoPayRepository.save(KakaoPayTid);

        // 동시성 제어 시작 부분. 현재 가격 확인 로직도 들어가야함.
        if (auction.getBidPrice().equals(product.getAuctionNowPrice())) {
            auction.updateStatus(SUCCESS_BID);
            auctionRepository.save(auction);
            auctionService.currentPriceUpdate(auction.getBidPrice(), product);
            deleteWithOutSuccessfulAuction(productId);
        } else {
            auction.updateStatus(BID);
            auctionRepository.save(auction);
            auctionService.currentPriceUpdate(auction.getBidPrice(), product);
            auctionService.scheduleMessage(productId, product.getDueDate());
        }

        // Send Request
        HttpEntity<KakaoPayApproveRequestDto> entityMap = new HttpEntity<>(
                kakaoPayApproveRequestDto, headers);

        KakaoPayApproveResponseDto response = new RestTemplate().postForObject(
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
        Auction auction = auctionAdapter.getAuction(kakaoPayTid.getAuction().getId());
        Product product = productAdapter.getProduct(auction.getProduct().getId());

        // Request param
        KakaoPayCancelRequestDto kakaoPayCancelRequestDto = KakaoPayCancelRequestDto.builder()
                .cid(kakaoPayTid.getCid())//가맹점 코드, 10자
                .tid(tid)//결제 고유번호, 20자
                .cancel_amount(kakaoPayTid.getAuction().getTotalAmount())//취소 금액
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

        auction.updateStatus(NEED_PAY);
        auctionRepository.save(auction);

        bidCancelAndSetCurrentPrice(product);

        kakaoPayRepository.delete(kakaoPayTid);
        return response;
    }

    public void kakaoCancelForSuccessBid(String tid) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        KakaoPayTid kakaoPayTid = kakaoPayRepository.findByTid(tid);

        // Request param
        KakaoPayCancelRequestDto kakaoPayCancelRequestDto = KakaoPayCancelRequestDto.builder()
                .cid(kakaoPayTid.getCid())//가맹점 코드, 10자
                .tid(tid)//결제 고유번호, 20자
                .cancel_amount(kakaoPayTid.getAuction().getTotalAmount())//취소 금액
                .cancel_tax_free_amount(0)//취소 비과세 금액
                .cancel_vat_amount(0)//취소 부가세 금액
                .build();

        // Send Request
        HttpEntity<KakaoPayCancelRequestDto> entityMap = new HttpEntity<>(kakaoPayCancelRequestDto,
                headers);

        new RestTemplate().postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/cancel",
                entityMap,
                KakaoPayCancelResponseDto.class);

        kakaoPayRepository.delete(kakaoPayTid);
    }

    public void bidCancel(String tid) {

        KakaoPayTid kakaoPayTid = kakaoPayRepository.findByTid(tid);
        Auction auction = auctionAdapter.getAuction(kakaoPayTid.getAuction().getId());
        Product product = productAdapter.getProduct(auction.getProduct().getId());

        auction.updateStatus(NEED_PAY);
        auctionRepository.save(auction);

        bidCancelAndSetCurrentPrice(product);

        kakaoPayRepository.delete(kakaoPayTid);

    }

    public KakaoPayGetTidResponseDto getTid(KakaoPayGetTidRequestDto kakaoPayGetTidRequestDto,
            User admin) {

//        reportService.checkUserRole(admin);
        if (!admin.getUserRole().equals(UserRole.MANAGER)) {
            throw new DataDuplicatedException(ResponseExceptionEnum.REPORT_MANAGER_STATUS);
        }

        Optional<User> user = userRepository.findByUsername(kakaoPayGetTidRequestDto.getUsername());
        Product product = productRepository.findByProductName(
                kakaoPayGetTidRequestDto.getProductName());
        Auction auction = auctionRepository.findByBidPriceAndUserAndProduct(
                user.orElseThrow().getId(), product.getId(),
                kakaoPayGetTidRequestDto.getBidPrice());
        KakaoPayTid kakaoPayTid = kakaoPayRepository.findByAuctionId(auction.getId());

        return new KakaoPayGetTidResponseDto(kakaoPayTid.getTid());

    }

    public void deleteWithOutSuccessfulAuction(Long productId) {
        List<Auction> auctions = auctionRepository.findAllByProductIdWithOutMaxPrice(productId);
        //왜냐하면 맥스값이 곧 낙찰가니까

        for (Auction auction : auctions) {
            if (auction.getStatus().equals(BID)) {
                KakaoPayTid kakaoPayTid = kakaoPayRepository.findByAuctionId(auction.getId());
                kakaoCancelForSuccessBid(kakaoPayTid.getTid());
                auctionRepository.delete(auction);
            } else {
                auctionRepository.delete(auction);
            }
        }

        messageSenderService.sendMessage(productId, 0); // 즉시 메시지 전송
    }

    public void deleteProductWithAuction(Long productId) {
        List<Auction> auctions = auctionRepository.findAllByProductId(productId);

        for (Auction auction : auctions) {
            if (auction.getStatus().equals(BID)) {
                KakaoPayTid kakaoPayTid = kakaoPayRepository.findByAuctionId(auction.getId());
                kakaoCancel(kakaoPayTid.getTid());
                auctionRepository.delete(auction);
            } else if(auction.getStatus().equals(NEED_PAY)) {
                auctionRepository.delete(auction);
            }
        }
    }

    public void bidCancelAndSetCurrentPrice(Product product) {

        Auction maxBid = auctionRepository.findByProductIdAndMaxBid(product.getId());

        if (maxBid == null) {
            product.currentPriceUpdate(product.getStartPrice());
            productRepository.save(product);
        } else {
            product.currentPriceUpdate(maxBid.getBidPrice());
            productRepository.save(product);
        }

    }

}
