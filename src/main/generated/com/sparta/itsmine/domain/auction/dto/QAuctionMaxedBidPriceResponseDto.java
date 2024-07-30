package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.sparta.itsmine.domain.auction.dto.QAuctionMaxedBidPriceResponseDto is a Querydsl Projection type for AuctionMaxedBidPriceResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAuctionMaxedBidPriceResponseDto extends ConstructorExpression<AuctionMaxedBidPriceResponseDto> {

    private static final long serialVersionUID = 661453063L;

    public QAuctionMaxedBidPriceResponseDto(com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<Integer> bidPrice) {
        super(AuctionMaxedBidPriceResponseDto.class, new Class<?>[]{long.class, int.class}, productId, bidPrice);
    }

}

