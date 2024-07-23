package com.sparta.itsmine.domain.product.repository;

import static com.sparta.itsmine.domain.product.entity.QProduct.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.itsmine.domain.product.entity.Product;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements CustomProductRepository {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Product> findActiveProductByUserAndName(Long userId, String productName) {
        Product foundProduct = queryFactory
                .selectFrom(product)
                .where(product.user.id.eq(userId)
                        .and(product.productName.eq(productName))
                        .and(product.deletedAt.isNull()))
                .fetchOne();
        return Optional.ofNullable(foundProduct);
    }

    @Override
    public Optional<Product> findActiveProductById(Long productId) {
        Product foundProduct = queryFactory
                .selectFrom(product)
                .where(product.id.eq(productId)
                        .and(product.deletedAt.isNull()))
                .fetchOne();
        return Optional.ofNullable(foundProduct);
    }

    private long fetchCount(JPAQueryFactory queryFactory, Long userId) {
        return queryFactory
                .selectFrom(product)
                .where(product.user.id.eq(userId)
                        .and(product.deletedAt.isNull()))
                .fetch().size();
    }
}
