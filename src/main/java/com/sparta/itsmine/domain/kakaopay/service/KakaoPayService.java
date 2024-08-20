package com.sparta.itsmine.domain.kakaopay.service;

import static com.sparta.itsmine.domain.product.utils.ProductStatus.BID;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.FAIL_BID;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.NEED_PAYMENT;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.NEED_PAYMENT_FOR_SUCCESS_BID;
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
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayReadyRequestDto;
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
import com.sparta.itsmine.global.lock.DistributedLock;

import lombok.RequiredArgsConstructor;

/**
 * Created by kakaopay
 */
@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final RedisTemplate<String, String> redisTemplate;
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
    private final String KAKAOPAY_PREFIX = "kakaopay";


    public KakaoPayReadyResponseDto ready(Long productId, User user, AuctionRequestDto requestDto) {

        //ban 상태의 유저는 입찰 못하게 차단
        user.checkBlock();

        // Request header
        //카카오 어플리케이션 Secret key dev버전을 header에 추가한다
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
        KakaoPayReadyRequestDto kakaoPayReadyRequestDto = KakaoPayReadyRequestDto.builder()
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
        //요청을 카카오페이 API에 요청을 보낼 때 HttpEntity 객체로 헤더와 요청 바디를 묶어서 전달
        HttpEntity<KakaoPayReadyRequestDto> entityMap = new HttpEntity<>(kakaoPayReadyRequestDto,
                headers);
        //응답을 HTTP 응답을 담는 객체로 만들고 RestTemplate으로 카카오페이 API 서버에 POST 요청을 보내고 응답을 받는다
        ResponseEntity<KakaoPayReadyResponseDto> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
//카카오페이 API의 결제 준비를 위한 엔드포인트 URL
                entityMap,
                KakaoPayReadyResponseDto.class//응답을 받을 때 KakaoPayReadyResponseDto 형태로 변환
        );
        KakaoPayReadyResponseDto kakaoPayReadyResponseDto = response.getBody();

        // 주문번호와 TID를 매핑해서 저장해놓는다.
        // Mapping TID with partner_order_id then save it to use for approval request.
        // this.tid = kakaoPayReadyResponseDto.getTid();
        redisService.saveKakaoTid(user.getUsername(), kakaoPayReadyResponseDto.getTid());
        return kakaoPayReadyResponseDto;
    }

    @DistributedLock(prefix = KAKAOPAY_PREFIX, key = "auctionId")
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

        String tid = redisTemplate.opsForValue().get(user.getUsername() + ":tid");

        if (auction.getBidPrice().equals(product.getAuctionNowPrice()) ||
                auction.getStatus().equals(NEED_PAYMENT_FOR_SUCCESS_BID)) {
            product.updateStatus(SUCCESS_BID);
            productRepository.save(product);

            auction.updateStatus(SUCCESS_BID);
            auctionRepository.save(auction);
            auctionService.currentPriceUpdate(auction.getBidPrice(), product);
            deleteWithOutSuccessfulAuction(product.getId());
        } else {
            if (auction.getBidPrice() <= product.getCurrentPrice()) {
                throw new IllegalArgumentException("이미 더 높은 가격에 입찰되었습니다.");
            }
            auction.updateStatus(BID);
            auctionRepository.save(auction);
            auctionService.currentPriceUpdate(auction.getBidPrice(), product);
            auctionService.scheduleMessage(product.getId(), product.getDueDate());
        }

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

        auction.updateStatus(NEED_PAYMENT);
        auctionRepository.save(auction);
        //낙찰을 취소 및 환불해주는 경우는 사이트 측 잘못으로 환불이 이루어진다 만약 NEED_PAYMENT_FOR_SUCCESS_BID 상태에서 환불을 원한다면 낙찰 결제 마저 하고 낙찰 상태에서 환불을 받는게 나을 거 같다 굳이 NEED_PAYMENT_FOR_SUCCESS_BID 상태에서의 환불을 만들 필요가 없는 거 같다
        if(product.getStatus().equals(SUCCESS_BID)){
            product.updateStatus(FAIL_BID);
            product.markAsDeleted();
            productRepository.save(product);
        }//NEED_PAYMENT_FOR_SUCCESS_BID 상태는 마감 시간이 다 되서 생기는 상태 즉 마감 시간이 다 지났기에 환불해준들 가격 반영이 필요가 없다 어차피 못 팔테니까
        else if(!product.getStatus().equals(NEED_PAYMENT_FOR_SUCCESS_BID)){
            bidCancelAndSetCurrentPrice(product);
        }


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

    //자의적인 입찰 취소로 낙찰은 무를 수 없다
    public void bidCancel(String tid) {

        KakaoPayTid kakaoPayTid = kakaoPayRepository.findByTid(tid);
        Auction auction = auctionAdapter.getAuction(kakaoPayTid.getAuction().getId());
        Product product = productAdapter.getProduct(auction.getProduct().getId());

        auction.updateStatus(NEED_PAYMENT);
        auctionRepository.save(auction);
        //이 경우는 좀 다르다 위에선 낙찰 결제 전이나 후나 전액 환불을 해주니까 괜찮지만 여기선 보즘금을 환불해주지 않는다 그래서 낙찰 뒤 취소가 아닌 낙찰 전 취소로 추가결제를 하기 전에 취소를 한다 추가결제까지 해놓고 취소해달라 해봐야 늦었다
        if(product.getStatus().equals(NEED_PAYMENT_FOR_SUCCESS_BID)){
            product.updateStatus(FAIL_BID);
            product.markAsDeleted();
            productRepository.save(product);
        }//NEED_PAYMENT_FOR_SUCCESS_BID 상태 이외의 다른 상태에선 입찰이 취소 되면 현재가에도 반영 시킨다 왜냐하면 어차피 BID 상태 이외의 상태에선 입찰 못 하기 때문에 이 else는 사실상 BID 상태일때만 가능하다는 말이랑 똑같다
        else{
            bidCancelAndSetCurrentPrice(product);
        }

        kakaoPayRepository.delete(kakaoPayTid);

    }

    public KakaoPayGetTidResponseDto getTid(KakaoPayGetTidRequestDto kakaoPayGetTidRequestDto,
            User admin) {

        if (!admin.getUserRole().equals(UserRole.MANAGER)) {
            throw new DataDuplicatedException(ResponseExceptionEnum.REPORT_MANAGER_STATUS);
        }

        Optional<User> user = userRepository.findByUsername(kakaoPayGetTidRequestDto.getUsername());
        Product product = productRepository.findByProductName(
                kakaoPayGetTidRequestDto.getProductName());
        Auction auction = auctionRepository.findByBidPriceAndUserAndProduct(
                user.orElseThrow().getId(), product.getId(),
                kakaoPayGetTidRequestDto.getBidPrice());
        Optional<KakaoPayTid> kakaoPayTid = kakaoPayRepository.findByAuctionId(auction.getId());

        return new KakaoPayGetTidResponseDto(kakaoPayTid.orElseThrow().getTid());

    }

    public KakaoPayReadyResponseDto additionalReady(Long productId, User user) {

        //ban 상태의 유저는 입찰 못하게 차단
        user.checkBlock();

        // Request header
        //카카오 어플리케이션 Secret key dev버전을 header에 추가한다
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Product product = productAdapter.getProduct(productId);

        AuctionResponseDto createAuction = auctionService.createAuctionForSuccessBid(user, productId, product.getCurrentPrice());
        // Request param
        KakaoPayReadyRequestDto kakaoPayReadyRequestDto = KakaoPayReadyRequestDto.builder()
                .cid(cid)//가맹점 코드, 10자
                .partnerOrderId(product.getId())//가맹점 주문번호, 최대 100자
                .partnerUserId(user.getUsername())//가맹점 회원 id, 최대 100자
                .itemName(product.getProductName())//상품명, 최대 100자
                .quantity(1)//상품 수량
                .totalAmount(product.getCurrentPrice())//상품 총액
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
        //요청을 카카오페이 API에 요청을 보낼 때 HttpEntity 객체로 헤더와 요청 바디를 묶어서 전달
        HttpEntity<KakaoPayReadyRequestDto> entityMap = new HttpEntity<>(kakaoPayReadyRequestDto,
                headers);
        //응답을 HTTP 응답을 담는 객체로 만들고 RestTemplate으로 카카오페이 API 서버에 POST 요청을 보내고 응답을 받는다
        ResponseEntity<KakaoPayReadyResponseDto> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
//카카오페이 API의 결제 준비를 위한 엔드포인트 URL
                entityMap,
                KakaoPayReadyResponseDto.class//응답을 받을 때 KakaoPayReadyResponseDto 형태로 변환
        );
        KakaoPayReadyResponseDto kakaoPayReadyResponseDto = response.getBody();

        // 주문번호와 TID를 매핑해서 저장해놓는다.
        // Mapping TID with partner_order_id then save it to use for approval request.
        // this.tid = kakaoPayReadyResponseDto.getTid();
        redisService.saveKakaoTid(user.getUsername(), kakaoPayReadyResponseDto.getTid());
        return kakaoPayReadyResponseDto;
    }

    public void deleteWithOutSuccessfulAuction(Long productId) {
        List<Auction> auctions = auctionRepository.findAllByProductId(productId);

        for (Auction auction : auctions) {
            if (auction.getStatus().equals(BID)) {
                Optional<KakaoPayTid> kakaoPayTid = kakaoPayRepository.findByAuctionId(auction.getId());
                kakaoCancelForSuccessBid(kakaoPayTid.orElseThrow().getTid());
                auctionRepository.delete(auction);
            } else if (auction.getStatus().equals(NEED_PAYMENT)) {
                auctionRepository.delete(auction);
            }
        }
        messageSenderService.sendMessage(productId, 0); // 즉시 메시지 전송
    }

    public void deleteProductWithAuction(Long productId) {
        List<Auction> auctions = auctionRepository.findAllByProductId(productId);

        for (Auction auction : auctions) {
            if (auction.getStatus().equals(BID)) {
                Optional<KakaoPayTid> kakaoPayTid = kakaoPayRepository.findByAuctionId(auction.getId());
                kakaoCancel(kakaoPayTid.orElseThrow().getTid());
                auctionRepository.delete(auction);
            } else if (auction.getStatus().equals(NEED_PAYMENT)) {
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

    public String FindTidByProductId(Long productId){
        Product product=productAdapter.getProduct(productId);
        Auction auction=auctionRepository.findByProductIdForAdditionalPayment(product.getId());
        Optional<KakaoPayTid> KakaoPayTid=kakaoPayRepository.findByAuctionId(auction.getId());

        if(KakaoPayTid.isPresent()){
           return KakaoPayTid.orElseThrow().getTid();
        }
        else{
            auctionRepository.delete(auction);
            return null;
        }
    }
}
