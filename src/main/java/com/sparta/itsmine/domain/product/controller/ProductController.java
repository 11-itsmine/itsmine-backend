package com.sparta.itsmine.domain.product.controller;


import com.sparta.itsmine.domain.product.dto.ProductCreateDto;
import com.sparta.itsmine.domain.product.dto.ProductCreateRequestDto;
import com.sparta.itsmine.domain.product.dto.ProductResponseDto;
import com.sparta.itsmine.domain.product.service.ProductService;
import com.sparta.itsmine.global.common.response.HttpResponseDto;
import com.sparta.itsmine.global.common.response.PageableResponse;
import com.sparta.itsmine.global.common.response.ResponseUtils;
import com.sparta.itsmine.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<HttpResponseDto> createProduct(
            @RequestBody ProductCreateRequestDto productCreateRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ProductResponseDto product = productService.createProduct(
                productCreateRequestDto.getProductCreateDto(),
                productCreateRequestDto.getProductImagesRequestDto(),
                userDetails.getUser().getId());
        return ResponseUtils.of(SUCCESS_SAVE_PRODUCT, product);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<HttpResponseDto> getProduct(
            @PathVariable("productId") Long productId
    ) {
        return ResponseUtils.of(SUCCESS_TO_SEARCH_PRODUCTS, productService.getProduct(productId));
    }

    @GetMapping
    public ResponseEntity<HttpResponseDto> getAllProductsWithPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<ProductResponseDto> responseDto = productService.getProductsWithPage(page, size,
                userDetails.getUser().getId());
        PageableResponse<ProductResponseDto> responseEntity = new PageableResponse<>(
                responseDto);
        return ResponseUtils.of(SUCCESS_TO_SEARCH_PRODUCTS, responseEntity);
    }

    @GetMapping("/likes")
    public ResponseEntity<HttpResponseDto> getAllLikeProductsWithPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<ProductResponseDto> responseDto = productService.getLikeProductsWithPage(page, size,
                userDetails.getUser().getId());
        PageableResponse<ProductResponseDto> responseEntity = new PageableResponse<>(
                responseDto);
        return ResponseUtils.of(SUCCESS_TO_SEARCH_PRODUCTS, responseEntity);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<HttpResponseDto> updateProduct(
            @RequestBody ProductCreateDto createDto,
            @PathVariable("productId") Long productId
    ) {
        productService.updateProduct(createDto, productId);
        return ResponseUtils.of(SUCCESS_TO_UPDATE);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<HttpResponseDto> deleteProduct(
            @PathVariable("productId") Long productId
    ) {
        productService.deleteProduct(productId);
        return ResponseUtils.of(SUCCESS_DELETE_PRODUCT);
    }
}
