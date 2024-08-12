package com.sparta.itsmine.domain.auction.contoller;

import static com.sparta.itsmine.domain.product.utils.ProductStatus.SUCCESS_BID;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.AUCTION_SUCCESSFUL_BID;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.AUCTION_SUCCESS_CREATE;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.AUCTION_SUCCESS_GET;

import com.sparta.itsmine.domain.auction.dto.AuctionProductImageResponseDto;
import com.sparta.itsmine.domain.auction.dto.AuctionProductResponseDto;
import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.service.AuctionService;
import com.sparta.itsmine.global.common.response.HttpResponseDto;
import com.sparta.itsmine.global.common.response.ResponseUtils;
import com.sparta.itsmine.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    //유저(구매자(본인)) 입찰 조회(QueryDSL)
    @GetMapping("/auctions")
    public ResponseEntity<HttpResponseDto> getAuctionByUserToPage(
            @AuthenticationPrincipal UserDetailsImpl userDetails, Pageable pageable) {
        Page<AuctionProductImageResponseDto> responseDto = auctionService.getAuctionByUser(
                userDetails.getUser(), pageable);
        return ResponseUtils.of(AUCTION_SUCCESS_GET, responseDto);
    }

    //유저(구매자(본인)) 상품 입찰 조회
    @GetMapping("/products/{productId}/auctions")
    public ResponseEntity<HttpResponseDto> getAuctionByProduct(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long productId) {
        AuctionProductResponseDto responseDto = auctionService.getAuctionByProduct(
                userDetails.getUser(), productId);
        return ResponseUtils.of(AUCTION_SUCCESS_GET, responseDto);
    }

}
