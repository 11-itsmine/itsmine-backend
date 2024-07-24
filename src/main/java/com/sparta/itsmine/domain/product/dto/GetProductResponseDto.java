package com.sparta.itsmine.domain.product.dto;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.entity.Product;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetProductResponseDto {

    private final Long id;
    private final String productName;
    private final String description;
    private final Integer auctionNowPrice;
    private final Integer currentPrice;
    private final LocalDateTime dueDate;
    private final Category category;

    public GetProductResponseDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.auctionNowPrice = product.getAuctionNowPrice();
        this.currentPrice = product.getCurrentPrice();
        this.dueDate = product.getDueDate();
        this.category = product.getCategory();
    }

}
