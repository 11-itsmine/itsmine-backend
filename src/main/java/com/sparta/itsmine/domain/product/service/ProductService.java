package com.sparta.itsmine.domain.product.service;

import static com.sparta.itsmine.domain.product.utils.ProductStatus.FAIL_BID;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.images.dto.ProductImagesRequestDto;
import com.sparta.itsmine.domain.images.service.ImagesService;
import com.sparta.itsmine.domain.kakaopay.service.KakaoPayService;
import com.sparta.itsmine.domain.like.entity.Like;
import com.sparta.itsmine.domain.like.repository.LikeRepository;
import com.sparta.itsmine.domain.product.dto.ProductCreateDto;
import com.sparta.itsmine.domain.product.dto.ProductResponseDto;
import com.sparta.itsmine.domain.product.dto.ProductUpdateRequestDto;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductAdapter;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.product.scheduler.MessageSenderService;
import com.sparta.itsmine.domain.user.entity.User;
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
    private final ImagesService imagesService;
    private final KakaoPayService kakaoPayService;
    private final MessageSenderService messageSenderService;
    private final LikeRepository likeRepository;
    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductCreateDto createDto,
            ProductImagesRequestDto imagesRequestDto, Long userId) {

        User user = adapter.findByIdAndDeletedAtIsNull(userId);
        adapter.verifyInputPrices(createDto);
        user.checkBlock();
        Category category = adapter.findCategoryByCategoryName(createDto.getCategoryName());
        adapter.existActiveProductByUserAndName(userId, createDto.getCategoryName());

        Product product = createDto.toEntity(category);
        product.assignUser(user);
//        unnecessary lines
//        product.extendDueDateByHours(createDto.getDueDate());
//        product.setCategory(category);

        Product newProduct = adapter.saveProduct(product);
        imagesService.createProductImages(imagesRequestDto, product);
        scheduleProductUpdate(newProduct);
        return new ProductResponseDto(newProduct, imagesRequestDto);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(Long productId) {
        return adapter.verifyProduct(productId);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProductsWithPage(int page, int size, Long category,
            String price, String search, String sort) {

        if (sort == null) {
            sort = "createdAt"; // 기본 정렬 필드 설정
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> products = productRepository.findProducts(pageRequest, category, price,
                search, sort);
        return products.map(ProductResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getUserProductsWithPage(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.ASC, "createdAt"));
        return adapter.findAllProducts(pageable, userId);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getLikeProductsWithPage(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.ASC, "createdAt"));
        Page<Like> likes = likeRepository.findAllByUserIdAndDeletedIsNull(userId, pageable);
        return likes.map(like -> new ProductResponseDto(like.getProduct()));
    }

    @Transactional
    public void updateProduct(ProductUpdateRequestDto updateDto, Long productId) {
        Product product = adapter.getProduct(productId);

        // 기존 상품 정보 업데이트
        product.updateProduct(product, updateDto.getProductCreateDto(),
                updateDto.getProductCreateDto().getDueDate());

        // 이미지 업데이트 (추가 및 삭제)
        imagesService.updateProductImages(product, updateDto.getProductImagesRequestDto(),
                updateDto.getImagesToDelete());

        adapter.saveProduct(product);
    }

    //상품 경매를 취소했을 때 유찰(상품 올린 걸 취소할 시 상태 변화는 유찰로 변해야 되지 않나)
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = adapter.getProduct(productId);
        kakaoPayService.deleteProductWithAuction(product.getId());
        product.markAsDeleted();
        product.updateStatus(FAIL_BID);
        adapter.saveProduct(product);
    }


    private void scheduleProductUpdate(Product product) {
        // 현재 시간
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDate = product.getDueDate();

        // 종료 시간이 이미 지났는지 확인
        if (now.isAfter(dueDate)) {
            // 종료 시간이 지났다면 최소 1초 안에 상태를 처리하도록 설정
            messageSenderService.sendMessage(product.getId(), 100);
            return;
        }

        // 현재 시간과 종료 시간 사이의 차이를 계산
        long delayMillis = Duration.between(now, dueDate).toMillis();

        // 최소 0.5초 후에 상태를 처리하도록 설정
        messageSenderService.sendMessage(product.getId(), Math.max(delayMillis, 500));
    }

}
