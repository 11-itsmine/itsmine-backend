package com.sparta.itsmine.domain.auction.dto;

import com.sparta.itsmine.domain.auction.entity.Auction;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAuctionByUserResponseDto {

    private Long userId;
    private Long productId;
    private Long bidPrice;

    public GetAuctionByUserResponseDto(Long productId, Long bidPrice, Long userId) {
        this.productId = productId;
        this.bidPrice = bidPrice;
        this.userId = userId;
    }
}
