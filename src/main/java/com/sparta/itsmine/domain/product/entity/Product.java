package com.sparta.itsmine.domain.product.entity;

import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.dto.ProductCreateDto;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import com.sparta.itsmine.domain.productImages.entity.ProductImages;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.TimeStamp;
import jakarta.persistence.*;
import java.util.ArrayList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sparta.itsmine.domain.product.utils.ProductStatus.SAVED;


@Entity
@Getter
@Table(name = "product")
@NoArgsConstructor
public class Product extends TimeStamp {

    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    @Column(nullable = false, unique = true)
    private String productName;
    @Column(nullable = false)
    private String description;
    // TODO : 시작 가격을 명시해주고 추적하자
    @Column(nullable = false)
    private Integer currentPrice;
    @Column(nullable = false)
    private Integer auctionNowPrice;
    // 입찰, 낙찰, 유찰
    // 상품이 등록이 되는 즉시 입찰 시작이기 때문에 바로 입찰로 상태를 변경한다.
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    // 시작 날짜와 끝 날짜를 혹시 모르니 생성해 둡니다.
    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime dueDate;
    private LocalDateTime deletedAt;

    @Column(name = "`like`", nullable = false)  // Use backticks in the annotation
    private Boolean like;

    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductImages> productImagesList;
    @OneToMany(mappedBy="product",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Auction> auction;


    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    @Builder
    public Product(String productName, String description, Integer currentPrice,
            Integer auctionNowPrice, LocalDateTime dueDate, Category category) {
        this.productName = productName;
        this.description = description;
        this.currentPrice = currentPrice;
        this.auctionNowPrice = auctionNowPrice;
        this.dueDate = dueDate;
        this.category = category;

        // set up initialized values
        this.status = SAVED;
        this.startDate = LocalDateTime.now();
        this.like = false;
    }

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */

    public void connectUser(User user) {
        this.user = user;
    }

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */

    public void updateProduct(Product product, ProductCreateDto createDto, Integer hour) {
        this.productName = Optional.ofNullable(createDto.getProductName())
                .orElse(product.getProductName());
        this.description = Optional.ofNullable(createDto.getDescription())
                .orElse(product.getDescription());
        this.auctionNowPrice = Optional.ofNullable(createDto.getAuctionNowPrice())
                .orElse(product.getAuctionNowPrice());
        this.currentPrice = Optional.ofNullable(createDto.getCurrentPrice())
                .orElse(product.getCurrentPrice());
        if (hour != null) {
            this.dueDate = product.getDueDate().plusSeconds(hour);
        } else {
            this.dueDate = product.getDueDate();
        }
    }

    public void setDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }

    public void turnStatus(ProductStatus status) {
        this.status = status;
    }

    public Boolean toggleLike() {
        this.like = !this.like; // 현재 값의 반대로 설정
        return this.like;
    }

    public void setDueDateBid(Integer hours) {
        this.dueDate = this.getDueDate().plusSeconds(hours);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<String> getImageUrls() {
        return productImagesList.stream()
                .flatMap(productImage -> productImage.getImagesUrl().stream())
                .collect(Collectors.toList());
    }
}
