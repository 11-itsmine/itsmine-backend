package com.sparta.itsmine.domain.category.dto;

import com.sparta.itsmine.domain.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {

    @NotBlank
    private String categoryName;

    public CategoryDto addCategoryName(@NotBlank String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public Category toEntity() {
        return Category.builder()
                .categoryName(categoryName).build();
    }
}
