package com.sparta.itsmine.domain.auction.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuctionRequestForSuccessBidDto {
    private Integer bidPrice;
}
