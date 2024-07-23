package com.sparta.itsmine.domain.auction.contoller;

import static com.sparta.itsmine.global.common.ResponseCodeEnum.AUCTION_SUCCESS_CREATE;
import static com.sparta.itsmine.global.common.ResponseCodeEnum.AUCTION_SUCCESS_DELETE_AVOIDEDAUCTION;
import static com.sparta.itsmine.global.common.ResponseCodeEnum.AUCTION_SUCCESS_DELETE_SUCCESSFULAUCTION;
import static com.sparta.itsmine.global.common.ResponseCodeEnum.AUCTION_SUCCESS_GET;

import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByProductResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByUserResponseDto;
import com.sparta.itsmine.domain.auction.service.AuctionService;
import com.sparta.itsmine.domain.product.dto.ProductResponseDto;
import com.sparta.itsmine.global.common.HttpResponseDto;
import com.sparta.itsmine.global.common.ResponseUtils;
import com.sparta.itsmine.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    //구매자 입찰 생성
    @PostMapping("/product/{productId}/auctions")
    public ResponseEntity<HttpResponseDto> createAuction(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long productId,
            @Valid @RequestBody AuctionRequestDto requestDto) {
        AuctionResponseDto responseDto = auctionService.createAuction(userDetails.getUser(),
                productId,
                requestDto);
        return ResponseUtils.of(AUCTION_SUCCESS_CREATE, responseDto);
    }

/*    //유저(구매자(본인)) 입찰 조회(stream)
    @GetMapping("/auctions")
    public ResponseEntity<HttpResponseDto> getAuctionByUserToList(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AuctionResponseDto> responseDto = auctionService.getAuctionByUser(
                userDetails.getUser());
        return ResponseUtils.of(AUCTION_SUCCESS_GET, responseDto);
    }*/

    //유저(구매자(본인)) 입찰 조회(QueryDSL)
    @GetMapping("/auctions")
    public ResponseEntity<HttpResponseDto> getAuctionByUserToList(
            @AuthenticationPrincipal UserDetailsImpl userDetails, Pageable pageable) {
        Page<GetAuctionByUserResponseDto> responseDto = auctionService.getAuctionByUser(
                userDetails.getUser(),pageable);
        return ResponseUtils.of(AUCTION_SUCCESS_GET, responseDto);
    }

    //유저(구매자(본인)) 상품 입찰 조회
    @GetMapping("/product/{productId}/auctions")
    public ResponseEntity<HttpResponseDto> getAuctionByProduct(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long productId) {
        GetAuctionByProductResponseDto responseDto = auctionService.getAuctionByProduct(
                userDetails.getUser(), productId);
        return ResponseUtils.of(AUCTION_SUCCESS_GET, responseDto);
    }

    //낙찰(테스트용으로 서비스의 기능 자체는 어디로 가야할지 고민해봐야함)
    @DeleteMapping("/product/{productId}/auction/successful")
    public ResponseEntity<HttpResponseDto> successfulAuction(@PathVariable Long productId) {
        AuctionResponseDto responseDto = auctionService.successfulAuction(productId);
        return ResponseUtils.of(AUCTION_SUCCESS_DELETE_SUCCESSFULAUCTION, responseDto);
    }

    //유찰(테스트용으로 서비스의 기능 자체는 어디로 가야할지 고민해봐야함)
    @DeleteMapping("/product/{productId}/auction/avoided")
    public ResponseEntity<HttpResponseDto> avoidedAuction(@PathVariable Long productId) {
        ProductResponseDto responseDto = auctionService.avoidedAuction(productId);
        return ResponseUtils.of(AUCTION_SUCCESS_DELETE_AVOIDEDAUCTION, responseDto);
    }
}
