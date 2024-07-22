package com.sparta.itsmine.domain.category.dto;

import com.sparta.itsmine.domain.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryDto {

    @NotBlank
    private String categoryName;

    public Category toEntity() {
        return Category.builder()
                .categoryName(categoryName).build();
    }
}
