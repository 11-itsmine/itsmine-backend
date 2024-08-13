package com.sparta.itsmine.domain.category;

import com.sparta.itsmine.domain.category.dto.CategoryDto;
import com.sparta.itsmine.domain.category.service.CategoryService;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CategoryInitializer implements CommandLineRunner {

    private final CategoryService categoryService;

    @Autowired
    public CategoryInitializer(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        // 추가하고자 하는 카테고리 목록을 리스트로 정의
        List<String> categories = Arrays.asList(
                "주방용품",
                "가구",
                "뷰티",
                "전자기기",
                "가전제품",
                "유아용품",
                "의류",
                "신발",
                "기타"
        );

        CategoryDto categoryDto = new CategoryDto();

        // 반복문을 통해 카테고리 생성
        categories.forEach(categoryName ->
                categoryService.createCategory(categoryDto.addCategoryName(categoryName))
        );
    }
}