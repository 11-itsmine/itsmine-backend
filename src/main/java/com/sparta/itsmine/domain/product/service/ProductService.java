package com.sparta.itsmine.domain.product.service;

import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_TO_LIKE;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_TO_REMOVE_LIKE;

import com.sparta.itsmine.domain.auction.service.AuctionService;
import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.dto.ProductCreateDto;
import com.sparta.itsmine.domain.product.dto.ProductResponseDto;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductAdapter;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import com.sparta.itsmine.domain.user.entity.User;
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
    private final AuctionService auctionService;

    @Transactional
    public ProductResponseDto createOrUpdateProduct(ProductCreateDto createDto, Long userId) {
        User user = adapter.findByIdAndDeletedAtIsNull(userId);
        Category category = adapter.findCategoryByCategoryName(createDto.getCategoryName());
        adapter.existActiveProductByUserAndName(userId, createDto.getCategoryName());

        Product product = createDto.toEntity(category);
        product.connectUser(user);
        product.setDueDateBid(createDto.getDueDate());
        product.setCategory(category);

        adapter.saveProduct(product);
        return new ProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(Long productId) {
        return adapter.verifyProduct(productId);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductsWithPage(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.ASC, "createdAt"));
        return adapter.findAllProducts(pageable, userId);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getLikeProductsWithPage(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.ASC, "createdAt"));
        return adapter.findAllLikeProduct(pageable, userId);
    }

    @Transactional
    public void updateProduct(ProductCreateDto createDto, Long productId) {
        Product product = adapter.getProduct(productId);
        product.updateProduct(product, createDto, createDto.getDueDate());
        adapter.saveProduct(product);
    }

    //상품 경매를 취소했을 때 유찰(상품 올린 걸 취소할 시 상태 변화는 유찰로 변해야 되지 않나)
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = adapter.getProduct(productId);
        product.setDeletedAt();
        product.turnStatus(ProductStatus.FAIL_BID);
        adapter.saveProduct(product);
        auctionService.avoidedAuction(productId);
    }

    @Transactional
    public ResponseCodeEnum toggleProductLike(Long productId) {
        Product product = adapter.getProduct(productId);
        boolean like = product.toggleLike();
        adapter.saveProduct(product);
        return like ? SUCCESS_TO_LIKE : SUCCESS_TO_REMOVE_LIKE;
    }
}
