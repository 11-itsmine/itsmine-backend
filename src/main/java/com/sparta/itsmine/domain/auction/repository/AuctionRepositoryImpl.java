package com.sparta.itsmine.domain.auction.repository;

import static com.sparta.itsmine.domain.auction.entity.QAuction.auction;
import static com.sparta.itsmine.domain.product.entity.QProduct.product;
import static com.sparta.itsmine.domain.user.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByUserResponseDto;
import com.sparta.itsmine.domain.auction.dto.QGetAuctionByUserResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionRepositoryImpl implements CustomAuctionRepository {

    private final JPAQueryFactory jpaQueryFactory;


    public List<Auction> findAuctionAllByUserid(Long userId) {
        return jpaQueryFactory
                .select(auction)
                .from(auction)
                .innerJoin(auction.user, user)
                .where(user.id.eq(userId))
                .fetch();
    }


    /*
        select product_id,max(bid_price) as bid_price,user_id
        from auctions
        where user_id=user_id
        group by product_id;
    */
    public List<GetAuctionByUserResponseDto> findAuctionAllByUserid2(Long userId) {
        /*return jpaQueryFactory
                .select(Projections.constructor(GetAuctionByUserResponseDto.class,
                        product.id,
                        auction.bidPrice.max(),
                        user.id))
                .from(auction)
                .innerJoin(auction.product, product)
                .innerJoin(auction.user, user)
                .where(user.id.eq(userId))
                .groupBy(product.id)
                .fetch();*/

        return jpaQueryFactory
                .select(new QGetAuctionByUserResponseDto(product.id, auction.bidPrice.max(), user.id))
                .from(auction)
                .innerJoin(auction.product, product)
                .innerJoin(auction.user, user)
                .where(user.id.eq(userId))
                .groupBy(product.id)
                .fetch();
    }

    public Auction findByUserIdAndProductId(Long UserId, Long productId) {
        return jpaQueryFactory
                .select(auction)
                .from(auction)
                .innerJoin(auction.product, product)
                .innerJoin(auction.user, user)
                .where(user.id.eq(UserId).and(product.id.eq(productId)))
                .fetchOne();
    }
}
