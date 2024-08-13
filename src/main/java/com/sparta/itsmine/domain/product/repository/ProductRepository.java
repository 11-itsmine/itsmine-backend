package com.sparta.itsmine.domain.product.repository;

import com.sparta.itsmine.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {

    Product findByProductName(String productName);

    List<Product> findAllByUserId(Long userId);
}
