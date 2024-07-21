package com.sparta.itsmine.domain.auction.contoller;


import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByUserResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
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

    @PostMapping("product/{product_id}/auctions")
    public AuctionResponseDto createAuction(@AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long product_id,
            @RequestBody AuctionRequestDto requestDto) {
        return auctionService.createAuction(userDetails.getUser(), product_id, requestDto);
    }

    @GetMapping("/auctions")
    public List<AuctionResponseDto> getAuctionByUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return auctionService.getAuctionByUser(userDetails.getUser());
    }

    @GetMapping("/auctions2")
    public List<GetAuctionByUserResponseDto> getAuctionByUser2(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return auctionService.getAuctionByUser2(userDetails.getUser());
    }
}
