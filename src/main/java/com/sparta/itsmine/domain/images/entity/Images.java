package com.sparta.itsmine.domain.images.entity;

import com.sparta.itsmine.domain.images.util.ImageType;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.TimeStamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Images extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(name = "imageUrl", nullable = false)
    private String imagesUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "contentType", nullable = true)
    private ImageType contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    public Images(String imagesUrl, ImageType contentType, Product product) {
        this.imagesUrl = imagesUrl;
        this.contentType = contentType;
        this.product = product;
    }

    public Images(String imagesUrl, ImageType contentType, User user) {
        this.imagesUrl = imagesUrl;
        this.contentType = contentType;
        this.user = user;
    }
}
