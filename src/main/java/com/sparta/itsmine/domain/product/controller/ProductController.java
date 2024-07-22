package com.sparta.itsmine.domain.product.controller;


import static com.sparta.itsmine.global.common.ResponseCodeEnum.SUCCESS_DELETE_PRODUCT;
import static com.sparta.itsmine.global.common.ResponseCodeEnum.SUCCESS_SAVE_PRODUCT;
import static com.sparta.itsmine.global.common.ResponseCodeEnum.SUCCESS_TO_SEARCH_PRODUCTS;
import static com.sparta.itsmine.global.common.ResponseCodeEnum.SUCCESS_TO_UPDATE;

import com.sparta.itsmine.domain.product.dto.GetProductResponseDto;
import com.sparta.itsmine.domain.product.dto.ProductCreateDto;
import com.sparta.itsmine.domain.product.service.ProductService;
import com.sparta.itsmine.global.common.HttpResponseDto;
import com.sparta.itsmine.global.common.PageableResponse;
import com.sparta.itsmine.global.common.ResponseUtils;
import com.sparta.itsmine.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<HttpResponseDto> createProduct(
            @RequestBody ProductCreateDto createDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseUtils.of(SUCCESS_SAVE_PRODUCT,
                productService.createProduct(createDto, userDetails.getUser()));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<HttpResponseDto> getProduct(
            @PathVariable Long productId
    ) {
        return ResponseUtils.of(SUCCESS_TO_SEARCH_PRODUCTS, productService.getProduct(productId));
    }

    @GetMapping
    public ResponseEntity<HttpResponseDto> getAllProductsWithPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<GetProductResponseDto> responseDto = productService.getProductsWithPage(page, size,
                userDetails.getUser());
        PageableResponse<GetProductResponseDto> responseEntity = new PageableResponse<>(
                responseDto);
        return ResponseUtils.of(SUCCESS_TO_SEARCH_PRODUCTS, responseEntity);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<HttpResponseDto> updateProduct(
            @RequestBody ProductCreateDto createDto,
            @PathVariable Long productId
    ) {
        productService.updateProduct(createDto, productId);
        return ResponseUtils.of(SUCCESS_TO_UPDATE);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<HttpResponseDto> deleteProduct(
            @PathVariable Long productId
    ) {
        productService.deleteProduct(productId);
        return ResponseUtils.of(SUCCESS_DELETE_PRODUCT);
    }

    @PostMapping("/{productId}/likes")
    public ResponseEntity<HttpResponseDto> addLikes(
            @PathVariable Long productId
    ) {
        return ResponseUtils.of(productService.addLikes(productId));
    }
}
