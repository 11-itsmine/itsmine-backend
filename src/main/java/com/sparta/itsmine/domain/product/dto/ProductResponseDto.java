package com.sparta.itsmine.domain.product.dto;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.entity.Product;
import java.time.LocalDateTime;
import java.util.List;

import com.sparta.itsmine.domain.productImages.dto.ProductImagesRequestDto;
import lombok.Getter;

@Getter
public class ProductResponseDto {

    private final Long id;
    private final String productName;
    private final String description;
    private final Integer auctionNowPrice;
    private final Integer currentPrice;
    private final LocalDateTime dueDate;
    private final Category category;
    private final List<String> imagesUrl;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.auctionNowPrice = product.getAuctionNowPrice();
        this.currentPrice = product.getCurrentPrice();
        this.dueDate = product.getDueDate();
        this.category = product.getCategory();
        this.imagesUrl = null;
    }

    public ProductResponseDto(Product product, ProductImagesRequestDto productImagesRequestDto) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.auctionNowPrice = product.getAuctionNowPrice();
        this.currentPrice = product.getCurrentPrice();
        this.dueDate = product.getDueDate();
        this.category = product.getCategory();
        this.imagesUrl = productImagesRequestDto.getImagesUrl();
    }

}
