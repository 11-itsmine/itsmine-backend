package com.sparta.itsmine.domain.product.controller;

import com.sparta.itsmine.domain.product.dto.GetProductResponseDto;
import com.sparta.itsmine.domain.product.dto.ProductCreateDto;
import com.sparta.itsmine.domain.product.service.ProductService;
import com.sparta.itsmine.domain.productImages.dto.ProductImagesRequestDto;
import com.sparta.itsmine.domain.productImages.service.ProductImagesService;
import com.sparta.itsmine.global.common.response.HttpResponseDto;
import com.sparta.itsmine.global.common.response.PageableResponse;
import com.sparta.itsmine.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.*;
import static com.sparta.itsmine.global.common.response.ResponseUtils.of;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<HttpResponseDto> createProduct(
            @RequestBody ProductCreateDto createDto,
            @RequestBody ProductImagesRequestDto imagesRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return of(SUCCESS_SAVE_PRODUCT,
                productService.createOrUpdateProduct(createDto,imagesRequestDto ,userDetails.getUser().getId()));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<HttpResponseDto> getProduct(
            @PathVariable Long productId
    ) {
        return of(SUCCESS_TO_SEARCH_PRODUCTS, productService.getProduct(productId));
    }

    @GetMapping
    public ResponseEntity<HttpResponseDto> getAllProductsWithPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<GetProductResponseDto> responseDto = productService.getProductsWithPage(page, size,
                userDetails.getUser().getId());
        PageableResponse<GetProductResponseDto> responseEntity = new PageableResponse<>(
                responseDto);
        return of(SUCCESS_TO_SEARCH_PRODUCTS, responseEntity);
    }

    @GetMapping("/likes")
    public ResponseEntity<HttpResponseDto> getAllLikeProductsWithPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<GetProductResponseDto> responseDto = productService.getLikeProductsWithPage(page, size,
                userDetails.getUser().getId());
        PageableResponse<GetProductResponseDto> responseEntity = new PageableResponse<>(
                responseDto);
        return of(SUCCESS_TO_SEARCH_PRODUCTS, responseEntity);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<HttpResponseDto> updateProduct(
            @RequestBody ProductCreateDto createDto,
            @PathVariable Long productId
    ) {
        productService.updateProduct(createDto, productId);
        return of(SUCCESS_TO_UPDATE);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<HttpResponseDto> deleteProduct(
            @PathVariable Long productId
    ) {
        productService.deleteProduct(productId);
        return of(SUCCESS_DELETE_PRODUCT);
    }

    @PostMapping("/{productId}/likes")
    public ResponseEntity<HttpResponseDto> addLikes(
            @PathVariable Long productId
    ) {
        return of(productService.toggleProductLike(productId));
    }
}
