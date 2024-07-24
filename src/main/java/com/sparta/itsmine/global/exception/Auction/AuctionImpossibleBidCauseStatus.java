package com.sparta.itsmine.global.exception.Auction;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class AuctionImpossibleBidCauseStatus extends AuctionException {

    public AuctionImpossibleBidCauseStatus(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }

}
