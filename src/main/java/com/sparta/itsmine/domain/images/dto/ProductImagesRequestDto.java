package com.sparta.itsmine.domain.images.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductImagesRequestDto {

    private final List<String> imagesUrl;

    @JsonCreator
    public ProductImagesRequestDto(@JsonProperty("imagesUrl") List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

}
