package com.sparta.itsmine.domain.auction.entity;


import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.AUCTION_DENIED_BID;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.AUCTION_IMPOSSIBLE_BID;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.AUCTION_IMPOSSIBLE_BID_CAUSE_STATUS;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.TimeStamp;
import com.sparta.itsmine.global.exception.DataDuplicatedException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "auctions")//bidPrice로 주로 조회하는데 카디널리티가 낮아 인덱싱을 안함
public class Auction extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer bidPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(nullable = false)
    private Integer totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    @Builder
    public Auction(User user, Product product, Integer bidPrice, ProductStatus status,Integer totalAmount) {
        this.user = user;
        this.product = product;
        this.bidPrice = bidPrice;
        this.status = status;
        this.totalAmount=totalAmount;
    }

    public void updateStatus(ProductStatus status) {
        this.status = status;
    }

    public void checkUser(User user, Product product) {
        if (user.getId().equals(product.getUser().getId())) {
            throw new DataDuplicatedException(AUCTION_DENIED_BID);
        }
    }

    public void checkBidPrice(Integer bidPrice,Product product) {
        //현재 입찰가(고른 상품에서 가장 높은 입찰가 or 상품 처음 입찰가) 이하이거나 즉시구매가를 넘어서 입찰하려하면 예외처리
        if (bidPrice <= product.getCurrentPrice()
                || bidPrice > product.getAuctionNowPrice()) {
            throw new DataDuplicatedException(AUCTION_IMPOSSIBLE_BID);
        }
    }

    public void checkStatus(ProductStatus status) {
        if (!status.equals(ProductStatus.BID)) {
            throw new DataDuplicatedException(AUCTION_IMPOSSIBLE_BID_CAUSE_STATUS);
        }
    }

    public void checkCurrentPrice(Integer bidPrice, Integer currentPrice) {
        if (bidPrice <= currentPrice) {
            throw new DataDuplicatedException(AUCTION_IMPOSSIBLE_BID);
        }
    }
}
