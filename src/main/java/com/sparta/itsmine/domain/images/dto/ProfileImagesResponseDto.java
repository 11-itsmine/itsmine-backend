package com.sparta.itsmine.domain.images.dto;

import lombok.Getter;

@Getter
public class ProfileImagesResponseDto {

    private final String imageUrl;

    public ProfileImagesResponseDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
