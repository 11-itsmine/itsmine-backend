package com.sparta.itsmine.domain.auction.entity;


import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.TimeStamp;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;


    @Builder
    public Auction(User user, Product product, Integer bidPrice ,ProductStatus status) {
        this.user = user;
        this.product = product;
        this.bidPrice = bidPrice;
        this.status=status;
    }

    public ProductStatus turnStatus(ProductStatus status) {
        this.status=status;
        return this.getStatus();
    }
}
