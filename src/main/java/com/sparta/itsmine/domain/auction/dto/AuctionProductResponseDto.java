package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuctionProductResponseDto {

    private Long userId;
    private Long productId;
    private Integer bidPrice;

    @QueryProjection
    public AuctionProductResponseDto(Long productId, Integer bidPrice, Long userId) {
        this.productId = productId;
        this.bidPrice = bidPrice;
        this.userId = userId;
    }

}
