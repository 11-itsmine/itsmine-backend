package com.sparta.itsmine.domain.product.repository;

import static com.sparta.itsmine.domain.product.entity.QProduct.product;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.FAIL_BID;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.itsmine.domain.product.entity.Product;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ProductRepositoryImpl implements CustomProductRepository {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    @Transactional
    public long updateProductsToFailBid() {
        return queryFactory
                .update(product)
                .set(product.status, FAIL_BID)
                .where(product.dueDate.loe(java.time.LocalDateTime.now())
                        .and(product.status.ne(FAIL_BID)))
                .execute();
    }

    @Cacheable("products")
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

    @Cacheable("productById")
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
