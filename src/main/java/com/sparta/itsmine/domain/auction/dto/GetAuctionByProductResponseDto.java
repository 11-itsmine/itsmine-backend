package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GetAuctionByProductResponseDto {

    private Long userId;
    private Long productId;
    private Long bidPrice;

    @QueryProjection
    public GetAuctionByProductResponseDto(Long productId, Long bidPrice, Long userId) {
        this.productId = productId;
        this.bidPrice = bidPrice;
        this.userId = userId;
    }

}
