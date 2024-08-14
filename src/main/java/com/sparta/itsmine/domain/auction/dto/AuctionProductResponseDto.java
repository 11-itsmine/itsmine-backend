package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuctionProductResponseDto {

    private Long productId;
    private String username;
    private String productName;
    private Integer bidPrice;
    private ProductStatus status;

    @QueryProjection
    public AuctionProductResponseDto(Long productId, String username, String productName, Integer bidPrice,
            ProductStatus status) {
        this.productId = productId;
        this.username = username;
        this.productName = productName;
        this.bidPrice = bidPrice;
        this.status = status;
    }

}
