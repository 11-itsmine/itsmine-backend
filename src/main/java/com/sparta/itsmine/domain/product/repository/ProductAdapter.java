package com.sparta.itsmine.domain.product.repository;

import static com.sparta.itsmine.global.common.ResponseExceptionEnum.CATEGORY_NOT_FOUND;
import static com.sparta.itsmine.global.common.ResponseExceptionEnum.PRODUCT_IN_DATE;
import static com.sparta.itsmine.global.common.ResponseExceptionEnum.PRODUCT_NOT_FOUND;
import static com.sparta.itsmine.global.common.ResponseExceptionEnum.USER_NOT_FOUND;

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

    public GetProductResponseDto createOrUpdateProduct(ProductCreateDto createDto, Long userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Category category = categoryRepository.findCategoryByCategoryName(
                        createDto.getCategoryName())
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));

        Optional<Product> existingProduct = productRepository.findActiveProductByUserAndName(userId,
                createDto.getProductName());

        if (existingProduct.isPresent()) {
            throw new ProductInDateException(PRODUCT_IN_DATE);
        }

        Product newProduct = createProduct(createDto, user, category);
        productRepository.save(newProduct);
        return new GetProductResponseDto(newProduct);
    }

    private Product createProduct(ProductCreateDto createDto, User user, Category category) {
        Product newProduct = createDto.toEntity(category);
        newProduct.connectUser(user);
        newProduct.setDueDateBid(createDto.getDueDate());
        newProduct.setCategory(category);
        return newProduct;
    }

    public GetProductResponseDto verifyProduct(Long productId) {
        Product product = productRepository.findActiveProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
        return new GetProductResponseDto(product);
    }

    public Page<GetProductResponseDto> findAllProducts(Pageable pageable, Long userId) {
        return productRepository.findAllByUserIdAndDeletedAtIsNull(userId, pageable)
                .map(GetProductResponseDto::new);
    }

    public Product getProduct(Long productId) {
        return productRepository.findActiveProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }
}
