package com.sparta.itsmine.domain.product.dto;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.entity.Product;
import java.time.LocalDateTime;

public class GetProductResponseDto {

    String productName;
    String description;
    Integer auctionNowPrice;
    Integer currentPrice;
    LocalDateTime dueDate;
    Category category;

    public GetProductResponseDto(Product product) {
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.auctionNowPrice = product.getAuctionNowPrice();
        this.currentPrice = product.getCurrentPrice();
        this.dueDate = product.getDueDate();
        this.category = product.getCategory();
    }

}
