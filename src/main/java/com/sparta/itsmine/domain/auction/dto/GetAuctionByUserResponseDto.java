package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.itsmine.domain.auction.entity.Auction;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAuctionByUserResponseDto {

    private Long userId;
    private Long productId;
    private Integer bidPrice;

    @QueryProjection
    public GetAuctionByUserResponseDto(Long productId, Integer bidPrice, Long userId) {
        this.productId = productId;
        this.bidPrice = bidPrice;
        this.userId = userId;
    }
}
