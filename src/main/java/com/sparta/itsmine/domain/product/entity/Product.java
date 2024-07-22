package com.sparta.itsmine.domain.product.entity;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.TimeStamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends TimeStamp {

    // 시작 날짜와 끝 날짜를 혹시 모르니 생성해 둡니다.
    private LocalDateTime startDate;
    // 입찰, 낙찰, 유찰
    // 상품이 등록이 되는 즉시 입찰 시작이기 때문에 바로 입찰로 상태를 변경한다.
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer currentPrice;
    @Column(nullable = false)
    private Integer AuctionNowPrice;
    private LocalDateTime dueDate;
    private LocalDateTime deletedAt;


    private boolean like;

    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    @Builder
    public Product(String productName, String description, Integer currentPrice,
            Integer auctionNowPrice, LocalDateTime dueDate, Category category) {
        this.productName = productName;
        this.description = description;
        this.currentPrice = currentPrice;
        AuctionNowPrice = auctionNowPrice;
        this.dueDate = dueDate;
        this.category = category;

        // set up initialized values
        this.status = ProductStatus.BID;
        this.startDate = LocalDateTime.now();
        this.like = false;
    }

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */

    public User connectUser(User user) {
        this.user = user;
        return user;
    }

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
    public void setDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }

    public ProductStatus turnStatus(ProductStatus status) {
        this.status = status;
        return this.getStatus();
    }

    public boolean toggleLike() {
        this.like = !this.like; // 현재 값의 반대로 설정
        return this.like;
    }
}