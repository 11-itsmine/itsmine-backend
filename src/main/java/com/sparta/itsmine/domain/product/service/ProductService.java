package com.sparta.itsmine.domain.product.service;

import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_TO_LIKE;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_TO_REMOVE_LIKE;

import com.sparta.itsmine.domain.product.dto.GetProductResponseDto;
import com.sparta.itsmine.domain.product.dto.ProductCreateDto;
import com.sparta.itsmine.domain.product.repository.ProductAdapter;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.response.ResponseCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductAdapter adapter;

    @Transactional
    public ProductCreateDto createProduct(ProductCreateDto createDto, User user) {
        adapter.findProductNameByUserId(createDto, user);
        return createDto;
    }

    @Transactional(readOnly = true)
    public GetProductResponseDto getProduct(Long productId) {
        return adapter.verifyProduct(productId);
    }

    @Transactional(readOnly = true)
    public Page<GetProductResponseDto> getProductsWithPage(int page, int size, User user) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return adapter.findAllProducts(pageable, user.getId());
    }

    @Transactional
    public void updateProduct(ProductCreateDto createDto, Long productId) {
        createDto.updateProduct(adapter.getProduct(productId));
    }

    @Transactional
    public void deleteProduct(Long productId) {
        adapter.getProduct(productId).setDeletedAt();
    }

    @Transactional
    public ResponseCodeEnum addLikes(Long productId) {
        boolean like = adapter.getProduct(productId).toggleLike();
        if (like) {
            return SUCCESS_TO_LIKE;
        }
        return SUCCESS_TO_REMOVE_LIKE;
    }
}
