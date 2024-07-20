package com.sparta.itsmine.domain.auction.dto;

import com.sparta.itsmine.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionResponseDto {

    private Long id;
    private Long auctionPrice;
    private LocalDateTime createdAt;
    private LocalDateTime auctionedAt;

    public AuctionResponseDto(AuctionRequestDto requestDto) {
        this.auctionPrice=requestDto.getAuctionPrice();
    }
}
