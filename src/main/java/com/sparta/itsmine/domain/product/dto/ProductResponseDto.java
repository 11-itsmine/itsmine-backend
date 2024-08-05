package com.sparta.itsmine.domain.product.dto;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.images.dto.ProductImagesRequestDto;
import com.sparta.itsmine.domain.product.entity.Product;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class ProductResponseDto {

    private final Long id;
    private final Long userId;
    private final String productName;
    private final String description;
    private final Integer auctionNowPrice;
    private final Integer startPrice;//수정부분
    private final Integer currentPrice;
    private final LocalDateTime dueDate;
    private final Category category;
    private final List<String> imagesUrl;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.userId = product.getUser().getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.auctionNowPrice = product.getAuctionNowPrice();
        this.startPrice = product.getStartPrice();//수정부분
        this.currentPrice = product.getCurrentPrice();
        this.dueDate = product.getDueDate();
        this.category = product.getCategory();
        this.imagesUrl = product.getImageUrls();
    }

    public ProductResponseDto(Product product, ProductImagesRequestDto productImagesRequestDto) {
        this.id = product.getId();
        this.userId = product.getUser().getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.auctionNowPrice = product.getAuctionNowPrice();
        this.startPrice = product.getStartPrice();//수정부분
        this.currentPrice = product.getCurrentPrice();
        this.dueDate = product.getDueDate();
        this.category = product.getCategory();
        this.imagesUrl = productImagesRequestDto.getImagesUrl();
    }
}
