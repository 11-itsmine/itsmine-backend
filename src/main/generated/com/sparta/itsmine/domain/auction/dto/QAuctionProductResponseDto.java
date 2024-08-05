package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.sparta.itsmine.domain.auction.dto.QAuctionProductResponseDto is a Querydsl Projection type for AuctionProductResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAuctionProductResponseDto extends ConstructorExpression<AuctionProductResponseDto> {

    private static final long serialVersionUID = -17399161L;

    public QAuctionProductResponseDto(com.querydsl.core.types.Expression<String> username, com.querydsl.core.types.Expression<String> productName, com.querydsl.core.types.Expression<Integer> bidPrice) {
        super(AuctionProductResponseDto.class, new Class<?>[]{String.class, String.class, int.class}, username, productName, bidPrice);
    }

}

