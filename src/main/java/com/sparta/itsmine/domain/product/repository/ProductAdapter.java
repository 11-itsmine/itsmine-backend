package com.sparta.itsmine.domain.product.repository;

import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.CATEGORY_NOT_FOUND;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.PRODUCT_IN_DATE;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.PRODUCT_NOT_FOUND;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.USER_NOT_FOUND;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.category.repository.CategoryRepository;
import com.sparta.itsmine.domain.product.dto.GetProductResponseDto;
import com.sparta.itsmine.domain.product.dto.ProductCreateDto;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.repository.UserRepository;
import com.sparta.itsmine.global.exception.category.CategoryNotFoundException;
import com.sparta.itsmine.global.exception.product.ProductInDateException;
import com.sparta.itsmine.global.exception.product.ProductNotFoundException;
import com.sparta.itsmine.global.exception.user.UserNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductAdapter {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public void findProductNameByUserId(ProductCreateDto createDto, User user) {
        // 상품 존재 검증 - 만료 기한을 확인합니다.
        User user1 = userRepository.findByIdAndDeletedAtIsNull(user.getId()).orElseThrow(
                () -> new UserNotFoundException(USER_NOT_FOUND)
        );

        Optional<Product> product = productRepository.findActiveProductByUserAndName(
                user.getId(), createDto.getProductName());

        Category category = categoryRepository.findCategoryByCategoryName(
                createDto.getCategoryName()).orElseThrow(
                () -> new CategoryNotFoundException(CATEGORY_NOT_FOUND)
        );

        Product product1 = createDto.toEntity(category);
        product1.connectUser(user);
        productRepository.save(product1);

        if (product.isEmpty()) {
            return;
        }

        // 이미 상품이 존재할 떄의 처리
        if (product.get().getDueDate().isBefore(LocalDateTime.now())) {
            throw new ProductInDateException(PRODUCT_IN_DATE);
        }
        product.get().connectUser(user);
    }

    public GetProductResponseDto verifyProduct(Long productId) {
        return new GetProductResponseDto(
                productRepository.findByIdAndDeletedAtIsNull(productId).orElseThrow(
                        () -> new ProductNotFoundException(PRODUCT_NOT_FOUND)
                ));
    }

    public Page<GetProductResponseDto> findAllProducts(Pageable pageable, Long userId) {
        return productRepository.findAllByUserIdAndDeletedAtIsNull(userId, pageable)
                .map(GetProductResponseDto::new);
    }

    public Product getProduct(Long productId) {
        return productRepository.findByIdAndDeletedAtIsNull(productId).orElseThrow(
                () -> new ProductNotFoundException(PRODUCT_NOT_FOUND)
        );
    }
}
