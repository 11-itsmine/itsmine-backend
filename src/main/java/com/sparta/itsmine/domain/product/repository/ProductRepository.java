package com.sparta.itsmine.domain.product.repository;

import com.sparta.itsmine.domain.product.entity.Product;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /*
    CREATE INDEX idx_user_product_name ON Product(user_id, productName);
    CREATE INDEX idx_product_deleted ON Product(deletedAt);
    * */

    @Cacheable("products")
    @Query("SELECT p FROM Product p WHERE p.user.id = :userId AND p.productName = :productName AND p.deletedAt IS NULL")
    Optional<Product> findActiveProductByUserAndName(@Param("userId") Long userId,
            @Param("productName") String productName);

    @Cacheable("productById")
    Optional<Product> findByIdAndDeletedAtIsNull(Long productId);

    @Cacheable("productsByUser")
    Page<Product> findAllByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);
}
