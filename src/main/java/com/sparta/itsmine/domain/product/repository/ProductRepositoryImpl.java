package com.sparta.itsmine.domain.product.repository;

import static com.sparta.itsmine.domain.product.entity.QProduct.product;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements CustomProductRepository {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Cacheable("products")
    @Override
    public Optional<Product> existActiveProductByUserAndName(Long userId, String productName) {
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

    @Cacheable("productsByUser")
    @Override
    public Page<Product> findAllByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable) {
        List<Product> products = queryFactory.selectFrom(product)
                .where(
                        product.user.id.eq(userId)
                                .and(product.deletedAt.isNull())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(product)
                .where(
                        product.user.id.eq(userId)
                                .and(product.deletedAt.isNull())
                                .and(product.status.eq(BID)) // Add condition for BID status
                )
                .fetch().size();

        return new PageImpl<>(products, pageable, total);
    }


    @Cacheable("productsMain")
    @Override
    public Page<Product> findProducts(Pageable pageable, Long category, String price, String search, String sort) {
        if (sort == null) {
            sort = "createdAt"; // 기본 정렬 필드 설정
        }

        List<Product> products = queryFactory
                .selectFrom(product)
                .where(
                        product.status.eq(BID), // Ensure only products with status BID are fetched
                        categoryEq(category),
                        priceEq(price),
                        productNameContains(search)
                )
                .orderBy(sort.equals("createdAt") ? product.createdAt.desc() : product.createdAt.asc()) // Sorting logic
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(product)
                .where(
                        product.status.eq(BID), // Ensure only products with status BID are counted
                        categoryEq(category),
                        priceEq(price),
                        productNameContains(search)
                )
                .fetch().size();

        return new PageImpl<>(products, pageable, total);
    }

    // 카테고리 필터 메서드
    private BooleanExpression categoryEq(Long category) {
        return category != null ? product.category.id.eq(category) : null;
    }

    private BooleanExpression priceEq(String price) {
        return price != null ? product.startPrice.eq(Integer.parseInt(price)) : null;
    }

    private BooleanExpression productNameContains(String search) {
        return search != null ? product.productName.containsIgnoreCase(search) : null;
    }
}
