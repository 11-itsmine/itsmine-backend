package com.sparta.itsmine.domain.auction.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionRequestDto {
    private Integer bidPrice;
}
