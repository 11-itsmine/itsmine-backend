package com.sparta.itsmine.domain.category.service;

import com.sparta.itsmine.domain.category.dto.CategoryDto;
import com.sparta.itsmine.domain.category.dto.CategoryResponseDto;
import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.category.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void createCategory(CategoryDto categoryName) {
        categoryRepository.save(categoryName.toEntity());
    }

    public List<CategoryResponseDto> getCategory(){
        List<Category> category=categoryRepository.findAll();
        return category.stream().map(CategoryResponseDto::new).toList();
    }
}
