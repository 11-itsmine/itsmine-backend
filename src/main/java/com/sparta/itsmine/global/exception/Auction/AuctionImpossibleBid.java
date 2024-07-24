package com.sparta.itsmine.global.exception.Auction;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class AuctionImpossibleBid extends AuctionException {

    public AuctionImpossibleBid(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }

}
