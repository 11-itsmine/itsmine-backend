package com.sparta.itsmine.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import com.sparta.itsmine.domain.qna.entity.Qna;
import com.sparta.itsmine.global.common.TimeStamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends TimeStamp {

    @Id
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "category_name")
    private Category category;

    @Column(name = "product_name")
    private String productName;

    private String description;

    @Column(name = "buy_now_price")
    private Long buyNowPrice;

    @Column(name = "auction_now_price")
    private Long auctionNowPrice;

    @Column(name = "dueDate")
    private LocalDateTime dueDate;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Qna> qnaList = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private ProductStatus status;

}
