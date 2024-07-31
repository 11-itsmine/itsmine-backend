package com.sparta.itsmine.domain.auction.repository;

import static com.sparta.itsmine.domain.auction.entity.QAuction.auction;
import static com.sparta.itsmine.domain.product.entity.QProduct.product;
import static com.sparta.itsmine.domain.user.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.itsmine.domain.auction.dto.AuctionProductResponseDto;
import com.sparta.itsmine.domain.auction.dto.QAuctionProductResponseDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionRepositoryImpl implements CustomAuctionRepository {

    private final JPAQueryFactory jpaQueryFactory;

    //자신이 고른 상품 전체 조회
    @Cacheable("AuctionAllPage")
    public Page<AuctionProductResponseDto> findAuctionAllByUserid(Long userId, Pageable pageable) {
        List<AuctionProductResponseDto> content = jpaQueryFactory
                .select(new QAuctionProductResponseDto(product.id, auction.bidPrice.max(), user.id))
                .from(auction)
                .innerJoin(auction.product, product)
                .innerJoin(auction.user, user)
                .where(user.id.eq(userId))
                .groupBy(product.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(auction.id.countDistinct())
                .from(auction)
                .innerJoin(auction.product, product)
                .innerJoin(auction.user, user)
                .where(user.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    //자신이 고른 상품 조회
    @Cacheable("Auction")
    public Optional<AuctionProductResponseDto> findByUserIdAndProductId(Long UserId,
            Long productId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(new QAuctionProductResponseDto(product.id, auction.bidPrice.max(),
                        user.id))
                .from(auction)
                .innerJoin(auction.product, product)
                .innerJoin(auction.user, user)
                .where(user.id.eq(UserId).and(product.id.eq(productId)))
                .fetchOne());
    }
}
