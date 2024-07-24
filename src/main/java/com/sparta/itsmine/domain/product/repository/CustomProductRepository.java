package com.sparta.itsmine.domain.product.repository;

import com.sparta.itsmine.domain.product.entity.Product;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface CustomProductRepository {

    long updateProductsToFailBid();

    Optional<Product> existActiveProductByUserAndName(@Param("userId") Long userId,
            @Param("productName") String productName);

    Optional<Product> findActiveProductById(@Param("productId") Long productId);

}
