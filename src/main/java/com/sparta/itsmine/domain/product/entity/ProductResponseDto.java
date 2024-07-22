package com.sparta.itsmine.domain.product.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {

    private Long id;
    private String productName;
    private String description;
    private Integer buyNowPrice;
    private Integer AuctionNowPrice;

    public ProductResponseDto(Product product) {
        this.id=product.getId();
        this.productName=product.getProductName();
        this.description=product.getDescription();
        this.buyNowPrice=product.getBuyNowPrice();
        this.AuctionNowPrice=product.getAuctionNowPrice();
    }
}
