package com.sparta.itsmine.domain.auction.dto;

import com.sparta.itsmine.domain.auction.entity.Auction;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionResponseDto {

    private Long id;
    private Long userId;
    private Long productId;
    private Integer bidPrice;
    private LocalDateTime createdAt;

    public AuctionResponseDto(Auction auction) {
        this.id = auction.getId();
        this.userId = auction.getUser().getId();
        this.productId = auction.getProduct().getId();
        this.bidPrice = auction.getBidPrice();
        this.createdAt = auction.getCreatedAt();
    }
}
