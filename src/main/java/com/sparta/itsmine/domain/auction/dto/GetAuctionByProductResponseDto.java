package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GetAuctionByProductResponseDto {

    private Long userId;
    private Long productId;
    private Integer bidPrice;

    @QueryProjection
    public GetAuctionByProductResponseDto(Long productId, Integer bidPrice, Long userId) {
        this.productId = productId;
        this.bidPrice = bidPrice;
        this.userId = userId;
    }

}