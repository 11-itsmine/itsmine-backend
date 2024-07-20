package com.sparta.itsmine.domain.auction.contoller;


import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.service.AuctionService;
import com.sparta.itsmine.domain.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping("/auctions")
    public AuctionResponseDto createAuction(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long productId,@RequestBody AuctionRequestDto requestDto){
        return auctionService.createAuction(userDetails.getUser(),productId,requestDto);
    }
}
