package com.sparta.itsmine.domain.product.service;

import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_TO_LIKE;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_TO_REMOVE_LIKE;

import com.sparta.itsmine.domain.auction.service.AuctionService;
import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.dto.ProductCreateDto;
import com.sparta.itsmine.domain.product.dto.ProductResponseDto;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductAdapter;
import com.sparta.itsmine.domain.product.scheduler.MessageSenderService;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import com.sparta.itsmine.domain.productImages.dto.ProductImagesRequestDto;
import com.sparta.itsmine.domain.productImages.service.ProductImagesService;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.response.ResponseCodeEnum;
import java.time.Duration;
import java.time.LocalDateTime;
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
    private final ProductImagesService productImagesService;
    private final MessageSenderService messageSenderService;

    @Transactional
    public ProductResponseDto createProduct(ProductCreateDto createDto,
            ProductImagesRequestDto imagesRequestDto, Long userId) {
        User user = adapter.findByIdAndDeletedAtIsNull(userId);
        Category category = adapter.findCategoryByCategoryName(createDto.getCategoryName());
        adapter.existActiveProductByUserAndName(userId, createDto.getCategoryName());

        Product product = createDto.toEntity(category);
        product.assignUser(user);
        product.extendDueDateByHours(createDto.getDueDate());
        product.setCategory(category);

        Product newProduct = adapter.saveProduct(product);
        productImagesService.createProductImages(imagesRequestDto, product, userId);
        scheduleProductUpdate(newProduct);
        return new ProductResponseDto(newProduct, imagesRequestDto);
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
        product.markAsDeleted();
        product.updateStatus(ProductStatus.FAIL_BID);
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

    private void scheduleProductUpdate(Product product) {
        // 현재 시간
        LocalDateTime now = LocalDateTime.now();

        // 시작 시간과 종료 시간
        LocalDateTime startDate = product.getCreatedAt();
        LocalDateTime dueDate = product.getDueDate();

        // 현재 시간과 시작 시간 사이의 차이를 계산
        long delayMillis = Duration.between(now, dueDate).toMillis();

        // delayMillis가 0 이하일 경우 바로 처리
        if (delayMillis <= 0) {
            delayMillis = 1000; // 최소 1초 후에 상태를 처리하도록 설정
        }

        messageSenderService.sendMessage(product.getId(), delayMillis);
    }
}
