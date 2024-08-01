package com.sparta.itsmine.domain.category.dto;

import com.sparta.itsmine.domain.category.entity.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponseDto {
    private String categoryName;

    public CategoryResponseDto(Category category){
        this.categoryName=category.getCategoryName();
    }
}
