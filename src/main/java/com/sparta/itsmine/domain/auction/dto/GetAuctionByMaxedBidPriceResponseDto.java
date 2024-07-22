package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAuctionByMaxedBidPriceResponseDto {

    private Long productId;
    private Long bidPrice;

    @QueryProjection
    public GetAuctionByMaxedBidPriceResponseDto(Long productId, Long bidPrice) {
        this.productId = productId;
        this.bidPrice = bidPrice;
    }
}
