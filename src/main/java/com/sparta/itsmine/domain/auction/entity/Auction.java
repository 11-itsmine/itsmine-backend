package com.sparta.itsmine.domain.auction.entity;


import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.AUCTION_IMPOSSIBLE_BID;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.AUCTION_IMPOSSIBLE_BID_CAUSE_STATUS;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.TimeStamp;
import com.sparta.itsmine.global.exception.Auction.AuctionImpossibleBid;
import com.sparta.itsmine.global.exception.Auction.AuctionImpossibleBidCauseStatus;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;



    @Builder
    public Auction(User user, Product product, Integer bidPrice ,ProductStatus status) {
        this.user = user;
        this.product = product;
        this.bidPrice = bidPrice;
        this.status=status;
    }

    public void turnStatus(ProductStatus status) {
        this.status=status;
    }

    public void checkBidPrice(Integer bidPrice){
        //현재 입찰가(고른 상품에서 가장 높은 입찰가 or 상품 처음 입찰가) 이하이거나 즉시구매가를 넘어서 입찰하려하면 예외처리
        if (bidPrice < product.getCurrentPrice()
                || bidPrice > product.getAuctionNowPrice()) {
            throw new AuctionImpossibleBid(AUCTION_IMPOSSIBLE_BID);
        }
    }

    public void checkStatus(ProductStatus status){
        if (!status.equals(ProductStatus.BID)) {
            throw new AuctionImpossibleBidCauseStatus(AUCTION_IMPOSSIBLE_BID_CAUSE_STATUS);
        }
    }

    public void checkCurrentPrice(Integer bidPrice,Integer currentPrice){
        if (bidPrice <= currentPrice) {
            throw new AuctionImpossibleBid(AUCTION_IMPOSSIBLE_BID);
        }
    }
}
