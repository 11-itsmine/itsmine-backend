package com.sparta.itsmine.domain.product.service;

import static com.sparta.itsmine.global.common.ResponseCodeEnum.SUCCESS_TO_LIKE;
import static com.sparta.itsmine.global.common.ResponseCodeEnum.SUCCESS_TO_REMOVE_LIKE;

import com.sparta.itsmine.domain.product.dto.GetProductResponseDto;
import com.sparta.itsmine.domain.product.dto.ProductCreateDto;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductAdapter;
import com.sparta.itsmine.global.common.ResponseCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductAdapter adapter;

    @Transactional
    public GetProductResponseDto createOrUpdateProduct(ProductCreateDto createDto, Long userId) {
        return adapter.createOrUpdateProduct(createDto, userId);
    }

    @Transactional(readOnly = true)
    public GetProductResponseDto getProduct(Long productId) {
        return adapter.verifyProduct(productId);
    }

    @Transactional(readOnly = true)
    public Page<GetProductResponseDto> getProductsWithPage(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.ASC, "createdAt"));
        return adapter.findAllProducts(pageable, userId);
    }

    @Transactional(readOnly = true)
    public Page<GetProductResponseDto> getLikeProductsWithPage(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.ASC, "createdAt"));
        return adapter.findAllLikeProduct(pageable, userId);
    }

    @Transactional
    public void updateProduct(ProductCreateDto createDto, Long productId) {
        Product product = adapter.getProduct(productId);
        product.updateProduct(product, createDto, createDto.getDueDate());
        adapter.saveProduct(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = adapter.getProduct(productId);
        product.setDeletedAt();
        adapter.saveProduct(product);
    }

    @Transactional
    public ResponseCodeEnum toggleProductLike(Long productId) {
        Product product = adapter.getProduct(productId);
        boolean like = product.toggleLike();
        adapter.saveProduct(product);
        return like ? SUCCESS_TO_LIKE : SUCCESS_TO_REMOVE_LIKE;
    }
}
