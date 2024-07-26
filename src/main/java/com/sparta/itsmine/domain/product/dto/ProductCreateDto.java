package com.sparta.itsmine.domain.product.dto;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.entity.Product;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProductCreateDto {

    private String productName;
    private String description;
    private Integer auctionNowPrice;
    private Integer currentPrice;
    private Integer dueDate;
    private String categoryName;

    public Product toEntity(Category category) {
        return Product.builder()
                .productName(productName)
                .description(description)
                .auctionNowPrice(auctionNowPrice)
                .currentPrice(currentPrice)
                .dueDate(LocalDateTime.now().plusHours(dueDate))
                .category(category).build();
    }
}
