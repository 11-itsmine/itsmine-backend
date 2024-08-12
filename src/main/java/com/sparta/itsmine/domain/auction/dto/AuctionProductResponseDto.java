package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuctionProductResponseDto {

    private String username;
    private String productName;
    private Integer bidPrice;
    private ProductStatus status;

    @QueryProjection
    public AuctionProductResponseDto(String username, String productName, Integer bidPrice,
            ProductStatus status) {
        this.username = username;
        this.productName = productName;
        this.bidPrice = bidPrice;
        this.status = status;
    }

}
