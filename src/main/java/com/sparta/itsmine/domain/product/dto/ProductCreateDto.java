package com.sparta.itsmine.domain.product.dto;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.entity.Product;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Getter;

@Getter
public class ProductCreateDto {

    String productName;
    String description;
    Integer auctionNowPrice;
    Integer currentPrice;
    Integer dueDate;
    String categoryName;
    String imageUrl;

    public Product toEntity(Category category) {
        return Product.builder()
                .productName(productName)
                .description(description)
                .auctionNowPrice(auctionNowPrice)
                .currentPrice(currentPrice)
                .dueDate(LocalDateTime.now().plusHours(dueDate))
                .category(category).build();
    }

    public void updateProduct(Product product) {
        this.productName = Optional.ofNullable(productName).orElse(product.getProductName());
        this.description = Optional.ofNullable(description).orElse(product.getDescription());
        this.auctionNowPrice = Optional.ofNullable(auctionNowPrice)
                .orElse(product.getAuctionNowPrice());
        this.currentPrice = Optional.ofNullable(currentPrice).orElse(product.getCurrentPrice());
        if (dueDate != null) {
            product.setDueDateBid(LocalDateTime.now().plusHours(dueDate));
        }
    }
}
