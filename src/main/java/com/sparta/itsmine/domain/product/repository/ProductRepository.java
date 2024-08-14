package com.sparta.itsmine.domain.product.repository;

import com.sparta.itsmine.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {

    Product findByProductName(String productName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Product> findAllByUserId(Long userId);
}
