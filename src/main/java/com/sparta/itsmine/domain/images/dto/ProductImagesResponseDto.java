package com.sparta.itsmine.domain.images.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ProductImagesResponseDto {

    private final List<String> imagesUrl;

    public ProductImagesResponseDto(List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }
}
