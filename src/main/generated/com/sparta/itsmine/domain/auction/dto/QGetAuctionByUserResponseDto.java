package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.sparta.itsmine.domain.auction.dto.QGetAuctionByUserResponseDto is a Querydsl Projection type for GetAuctionByUserResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QGetAuctionByUserResponseDto extends ConstructorExpression<GetAuctionByUserResponseDto> {

    private static final long serialVersionUID = -1684800156L;

    public QGetAuctionByUserResponseDto(com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<Integer> bidPrice, com.querydsl.core.types.Expression<Long> userId) {
        super(GetAuctionByUserResponseDto.class, new Class<?>[]{long.class, int.class, long.class}, productId, bidPrice, userId);
    }

}

