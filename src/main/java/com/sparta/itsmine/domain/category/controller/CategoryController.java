package com.sparta.itsmine.domain.category.controller;


import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_TO_MAKE_NEW_CATEGORY;
import static com.sparta.itsmine.global.common.response.ResponseUtils.of;

import com.sparta.itsmine.domain.category.dto.CategoryDto;
import com.sparta.itsmine.domain.category.service.CategoryService;
import com.sparta.itsmine.global.common.response.HttpResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<HttpResponseDto> createCategory(
            @RequestBody CategoryDto categoryName
    ) {
        categoryService.createCategory(categoryName);
        return of(SUCCESS_TO_MAKE_NEW_CATEGORY);
    }
}
