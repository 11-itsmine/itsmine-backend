package com.sparta.itsmine.global.exception.Auction;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class AuctionException extends RuntimeException {

    private final ResponseExceptionEnum responseExceptionEnum;

    public AuctionException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}
