package com.sparta.itsmine.domain.product.repository;

import com.sparta.itsmine.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {

    /*
    CREATE INDEX idx_user_product_name ON Product(user_id, productName);
    CREATE INDEX idx_product_deleted ON Product(deletedAt);
    * */

//    @Cacheable("productsByUser")
//    Page<Product> findAllByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);
//
//    Page<Product> findAllByUserIdAndLikeTrueAndDeletedAtIsNull(Long userId, Pageable pageable);
}
