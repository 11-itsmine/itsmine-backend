package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAuctionByMaxedBidPriceResponseDto {

    private Long productId;
    private Integer bidPrice;

    @QueryProjection
    public GetAuctionByMaxedBidPriceResponseDto(Long productId, Integer bidPrice) {
        this.productId = productId;
        this.bidPrice = bidPrice;
    }
}
