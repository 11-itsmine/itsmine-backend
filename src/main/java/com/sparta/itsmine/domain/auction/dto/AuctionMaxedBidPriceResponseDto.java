package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionMaxedBidPriceResponseDto {

    private Long productId;
    private Integer bidPrice;

    @QueryProjection
    public AuctionMaxedBidPriceResponseDto(Long productId, Integer bidPrice) {
        this.productId = productId;
        this.bidPrice = bidPrice;
    }
}
