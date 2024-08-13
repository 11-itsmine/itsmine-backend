package com.sparta.itsmine.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.itsmine.domain.images.dto.ProductImagesRequestDto;
import java.util.List;
import lombok.Getter;

@Getter
public class ProductUpdateRequestDto {

    private final ProductCreateDto productCreateDto;
    private final ProductImagesRequestDto productImagesRequestDto;
    private final List<Long> imagesToDelete;

    @JsonCreator
    public ProductUpdateRequestDto(
            @JsonProperty("productCreateDto") ProductCreateDto productCreateDto,
            @JsonProperty("productImagesRequestDto") ProductImagesRequestDto productImagesRequestDto,
            @JsonProperty("imagesToDelete") List<Long> imagesToDelete
    ) {
        this.productCreateDto = productCreateDto;
        this.productImagesRequestDto = productImagesRequestDto;
        this.imagesToDelete = imagesToDelete;
    }
}
