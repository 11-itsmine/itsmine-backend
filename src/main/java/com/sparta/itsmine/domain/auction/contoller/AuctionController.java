package com.sparta.itsmine.domain.auction.contoller;


import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByProductResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByUserResponseDto;
import com.sparta.itsmine.domain.auction.service.AuctionService;
import com.sparta.itsmine.domain.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public AuctionResponseDto createAuction(@AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long product_id,
            @RequestBody AuctionRequestDto requestDto) {
        return auctionService.createAuction(userDetails.getUser(), product_id, requestDto);
    }

    //유저(구매자(본인)) 입찰 조회(stream)
    @GetMapping("/auctions")
    public List<AuctionResponseDto> getAuctionByUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return auctionService.getAuctionByUser(userDetails.getUser());
    }

    //유저(구매자(본인)) 입찰 조회2(QueryDSL)
    @GetMapping("/auctions2")
    public List<GetAuctionByUserResponseDto> getAuctionByUser2(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return auctionService.getAuctionByUser2(userDetails.getUser());
    }

    //유저(구매자(본인)) 상품 입찰 조회
    @GetMapping("/product/{product_id}/auctions")
    public GetAuctionByProductResponseDto getAuctionByProduct(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long product_id){
        return auctionService.getAuctionByProduct(userDetails.getUser(),product_id);
    }
}
