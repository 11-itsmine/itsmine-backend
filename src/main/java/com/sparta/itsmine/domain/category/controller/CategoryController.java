package com.sparta.itsmine.domain.category.controller;


import com.sparta.itsmine.domain.category.dto.CategoryDto;
import com.sparta.itsmine.domain.category.service.CategoryService;
import com.sparta.itsmine.global.common.HttpResponseDto;
import com.sparta.itsmine.global.common.ResponseCodeEnum;
import com.sparta.itsmine.global.common.ResponseUtils;
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
        return ResponseUtils.of(ResponseCodeEnum.SUCCESS_TO_MAKE_NEW_CATEGORY);
    }
}
