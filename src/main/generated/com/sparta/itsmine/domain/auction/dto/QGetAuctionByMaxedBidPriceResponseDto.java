package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.sparta.itsmine.domain.auction.dto.QGetAuctionByMaxedBidPriceResponseDto is a Querydsl Projection type for GetAuctionByMaxedBidPriceResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QGetAuctionByMaxedBidPriceResponseDto extends ConstructorExpression<GetAuctionByMaxedBidPriceResponseDto> {

    private static final long serialVersionUID = -2074197826L;

    public QGetAuctionByMaxedBidPriceResponseDto(com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<Integer> bidPrice) {
        super(GetAuctionByMaxedBidPriceResponseDto.class, new Class<?>[]{long.class, int.class}, productId, bidPrice);
    }

}

