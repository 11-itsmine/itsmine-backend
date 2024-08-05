package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuctionProductResponseDto {

    private String username;
    private String productName;
    private Integer bidPrice;

    @QueryProjection
    public AuctionProductResponseDto(String username, String productName, Integer bidPrice) {
        this.username = username;
        this.productName = productName;
        this.bidPrice = bidPrice;
    }

}
