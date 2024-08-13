package com.sparta.itsmine.domain.auction.repository;

import static com.sparta.itsmine.domain.auction.entity.QAuction.auction;
import static com.sparta.itsmine.domain.product.entity.QProduct.product;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.BID;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.NEED_PAY;
import static com.sparta.itsmine.domain.user.entity.QUser.user;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.itsmine.domain.auction.dto.AuctionProductImageResponseDto;
import com.sparta.itsmine.domain.auction.dto.AuctionProductResponseDto;
import com.sparta.itsmine.domain.auction.dto.QAuctionProductResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.entity.QProduct;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final ProductRepository productRepository;


    /*
    select u.username,p.product_name,max(a.bid_price),a.status
    from auctions a,user u,product p
    where u.id=2 and u.id=a.user_id and p.id=a.product_id and a.status = p.status and a.status != 'NEED_PAY'
    group by p.id;
    */
    //자신이 고른 상품 전체 조회
    @Cacheable("AuctionAllByUser")
    public Page<AuctionProductImageResponseDto> findAuctionAllByUserid(Long userId,
            Pageable pageable) {
        List<AuctionProductResponseDto> auctionProductResponseDtoList = jpaQueryFactory
                .select(new QAuctionProductResponseDto(user.username, product.productName,
                        auction.bidPrice.max(), auction.status))
                .from(auction)
                .innerJoin(auction.product, product)
                .innerJoin(auction.user, user)
                .where(user.id.eq(userId).and(auction.status.eq(product.status))
                        .and(auction.status.ne(NEED_PAY)))
                .groupBy(product.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<AuctionProductImageResponseDto> auctionProductImageResponseDtoList = auctionProductResponseDtoList.stream()
                .map(dto -> {
                    Product product = productRepository.findByProductName(dto.getProductName());
                    return new AuctionProductImageResponseDto(dto, product);
                })
                .toList();

        Long count = jpaQueryFactory
                .select(auction.id.countDistinct())
                .from(auction)
                .innerJoin(auction.product, product)
                .innerJoin(auction.user, user)
                .where(user.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(auctionProductImageResponseDtoList, pageable, count);
    }

    //자신이 고른 상품 조회
    @Cacheable("ByUserAndProduct")
    public Optional<AuctionProductResponseDto> findByUserIdAndProductId(Long UserId,
            Long productId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(new QAuctionProductResponseDto(user.username, product.productName,
                        auction.bidPrice.max(), auction.status))
                .from(auction)
                .innerJoin(auction.product, product)
                .innerJoin(auction.user, user)
                .where(user.id.eq(UserId).and(product.id.eq(productId)))
                .fetchOne());
    }

    /*
    select *
    from auctions
    where product_id=2 and bid_price != (select max(bid_price) from auctions where product_id=2);
    */
    //해당 상품에 대한 모든 입찰가를 찾기(최댓값 빼고)
    @Cacheable("WithOutSuccessfulAuction")
    public List<Auction> findAllByProductIdWithOutMaxPrice(Long productId) {

        JPQLQuery<Integer> maxBidPriceSubQuery = JPAExpressions
                .select(auction.bidPrice.max())
                .from(auction)
                .innerJoin(auction.product, product)
                .where(product.id.eq(productId));

        return jpaQueryFactory
                .select(auction)
                .from(auction)
                .innerJoin(auction.product, product)
                .where(product.id.eq(productId)
                        .and(auction.bidPrice.ne(maxBidPriceSubQuery)))
                .fetch();
    }

    /*
    select *
    from auctions
    where product_id=3 and status='BID' and bid_price=(select max(bid_price) from auctions where product_id=3 and status='BID');
    */
    @Cacheable("ByProductAndMaxBid")
    public Auction findByProductIdAndMaxBid(Long productId) {

        JPQLQuery<Integer> biddingMaxBidPriceSubQuery = JPAExpressions
                .select(auction.bidPrice.max())
                .from(auction)
                .innerJoin(auction.product, product)
                .where(product.id.eq(productId).and(auction.status.eq(BID)));

        return jpaQueryFactory
                .select(auction)
                .from(auction)
                .where(product.id.eq(productId).and(auction.status.eq(BID))
                        .and(auction.bidPrice.eq(biddingMaxBidPriceSubQuery)))
                .fetchOne();
    }

    /*
    select *
    from auctions
    where user_id=2 and product_id=4 and auctions.bid_price=50000 and status != 'NEED_PAY';
    */
    @Cacheable("ByBidPriceAndUserAndProduct")
    public Auction findByBidPriceAndUserAndProduct(Long userId, Long productId, Integer bidPrice) {
        return jpaQueryFactory
                .select(auction)
                .from(auction)
                .innerJoin(auction.user, user)
                .innerJoin(auction.product, product)
                .where(user.id.eq(userId).and(product.id.eq(productId))
                        .and(auction.bidPrice.eq(bidPrice)).and(auction.status.ne(NEED_PAY)))
                .fetchOne();
    }

}
