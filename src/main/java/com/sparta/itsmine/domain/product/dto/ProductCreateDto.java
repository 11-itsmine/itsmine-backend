package com.sparta.itsmine.domain.product.dto;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.entity.Product;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
public class ProductCreateDto {

    @NotBlank(message = "Product name cannot be empty")
    private String productName;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Range(min = 0, message = "Auction now price must be non-negative")
    private Integer auctionNowPrice;

    @Range(min = 0, message = "Start price must be non-negative")
    private Integer startPrice;

    @Range(min = 1, message = "Due date must be non-negative")
    private Integer dueDate;

    @NotBlank(message = "Category name cannot be empty")
    private String categoryName;

    public Product toEntity(Category category) {
        return Product.builder()
                .productName(productName)
                .description(description)
                .auctionNowPrice(auctionNowPrice)
                .startPrice(startPrice)
                .dueDate(LocalDateTime.now().plusHours(dueDate))
                .category(category).build();
    }
}
