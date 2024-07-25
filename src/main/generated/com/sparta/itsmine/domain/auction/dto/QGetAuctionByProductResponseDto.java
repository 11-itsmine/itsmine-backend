package com.sparta.itsmine.domain.auction.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.sparta.itsmine.domain.auction.dto.QGetAuctionByProductResponseDto is a Querydsl Projection type for GetAuctionByProductResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QGetAuctionByProductResponseDto extends ConstructorExpression<GetAuctionByProductResponseDto> {

    private static final long serialVersionUID = -1651925122L;

    public QGetAuctionByProductResponseDto(com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<Integer> bidPrice, com.querydsl.core.types.Expression<Long> userId) {
        super(GetAuctionByProductResponseDto.class, new Class<?>[]{long.class, int.class, long.class}, productId, bidPrice, userId);
    }

}

