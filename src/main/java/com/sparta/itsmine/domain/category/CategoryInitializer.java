package com.sparta.itsmine.domain.category;

import com.sparta.itsmine.domain.category.dto.CategoryDto;
import com.sparta.itsmine.domain.category.repository.CategoryRepository;
import com.sparta.itsmine.domain.category.service.CategoryService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryInitializer implements CommandLineRunner {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
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
        categories.forEach(categoryName -> {
            try {
                // 중복 생성 방지 로직 추가
                if (!categoryRepository.existsByCategoryName(categoryName)) {
                    categoryService.createCategory(categoryDto.addCategoryName(categoryName));
                }
            } catch (Exception e) {
                log.error("Failed to create category '{}': {}", categoryName, e.getMessage());
            }
        });
    }
}