package com.sparta.itsmine.domain.productImages.entity;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.global.common.TimeStamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class ProductImages extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(name = "imageUrl", nullable = false)
    private String imagesUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductImages(String imagesUrl, Product product) {
        this.imagesUrl = imagesUrl;
        this.product = product;
    }
}
