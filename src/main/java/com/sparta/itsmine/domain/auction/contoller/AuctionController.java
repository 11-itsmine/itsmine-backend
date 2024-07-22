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
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.entity.ProductResponseDto;
import com.sparta.itsmine.domain.security.UserDetailsImpl;
import com.sparta.itsmine.global.common.HttpResponseDto;
import com.sparta.itsmine.global.common.ResponseCodeEnum;
import com.sparta.itsmine.global.common.ResponseUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("/product/{product_id}/auctions")
    public ResponseEntity<HttpResponseDto> createAuction(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long product_id,
            @RequestBody AuctionRequestDto requestDto) {
        AuctionResponseDto auction = auctionService.createAuction(userDetails.getUser(), product_id,
                requestDto);
        return ResponseUtils.of(AUCTION_SUCCESS_CREATE, auction);
    }

    //유저(구매자(본인)) 입찰 조회(stream)
    @GetMapping("/auctions")
    public ResponseEntity<HttpResponseDto> getAuctionByUserToList(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AuctionResponseDto> auctions = auctionService.getAuctionByUser(userDetails.getUser());
        return ResponseUtils.of(AUCTION_SUCCESS_GET, auctions);
    }

    //유저(구매자(본인)) 입찰 조회2(QueryDSL)
    @GetMapping("/auctions2")
    public ResponseEntity<HttpResponseDto> getAuctionByUserToList2(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<GetAuctionByUserResponseDto> auctions = auctionService.getAuctionByUser2(
                userDetails.getUser());
        return ResponseUtils.of(AUCTION_SUCCESS_GET, auctions);
    }

    //유저(구매자(본인)) 상품 입찰 조회
    @GetMapping("/product/{product_id}/auctions")
    public ResponseEntity<HttpResponseDto> getAuctionByProduct(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long product_id) {
        GetAuctionByProductResponseDto auction = auctionService.getAuctionByProduct(
                userDetails.getUser(), product_id);
        return ResponseUtils.of(AUCTION_SUCCESS_GET, auction);
    }

    //낙찰(테스트용으로 서비스의 기능 자체는 어디로 가야할지 고민해봐야함)
    @DeleteMapping("/product/{product_id}/auction/successful")
    public ResponseEntity<HttpResponseDto> successfulAuction(@PathVariable Long product_id) {
        AuctionResponseDto auction = auctionService.successfulAuction(product_id);
        return ResponseUtils.of(AUCTION_SUCCESS_DELETE_SUCCESSFULAUCTION, auction);
    }

    //유찰(테스트용으로 서비스의 기능 자체는 어디로 가야할지 고민해봐야함)
    @DeleteMapping("/product/{product_id}/auction/avoided")
    public ResponseEntity<HttpResponseDto> avoidedAuction(@PathVariable Long product_id) {
        ProductResponseDto Product = auctionService.avoidedAuction(product_id);
        return ResponseUtils.of(AUCTION_SUCCESS_DELETE_AVOIDEDAUCTION, Product);
    }
}
