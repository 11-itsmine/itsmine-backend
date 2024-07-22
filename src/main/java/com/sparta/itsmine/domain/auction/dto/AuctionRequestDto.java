package com.sparta.itsmine.domain.auction.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuctionRequestDto {
    @NotBlank(message = "가격을 입력해 주세요.")
    private Long bidPrice;
}
