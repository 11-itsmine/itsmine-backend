package com.sparta.itsmine.global.exception.Auction;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class AuctionNotFoundException extends AuctionException {

    public AuctionNotFoundException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
