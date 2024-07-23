package com.sparta.itsmine.domain.auction.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuctionRequestDto {
    private Long bidPrice;
}
