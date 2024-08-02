package com.sparta.itsmine.domain.product.repository;

import com.sparta.itsmine.domain.product.entity.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface CustomProductRepository {

    Page<Product> findProducts(Pageable pageable, String category, String price, String search,
            String sort);

    Optional<Product> existActiveProductByUserAndName(@Param("userId") Long userId,
            @Param("productName") String productName);

    Optional<Product> findActiveProductById(@Param("productId") Long productId);

    Page<Product> findAllByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);

    Page<Product> findAllByUserIdAndLikeTrueAndDeletedAtIsNull(Long userId, Pageable pageable);

}
