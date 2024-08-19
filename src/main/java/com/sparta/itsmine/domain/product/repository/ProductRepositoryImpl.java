package com.sparta.itsmine.domain.product.repository;

import static com.sparta.itsmine.domain.product.entity.QProduct.product;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.BID;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.itsmine.domain.product.entity.Product;
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
                                .and(product.status.eq(BID))
                )
                .fetch().size();

        return new PageImpl<>(products, pageable, total);
    }

    @Cacheable("productsMain")
    @Override
    public Page<Product> findProducts(Pageable pageable, Long category, String priceRange,
            String search, String sort) {

        if (sort == null) {
            sort = "createdAt"; // 기본 정렬 필드 설정
        }

        // Parse the price range
        Long minPrice = null;
        Long maxPrice = null;
        if (priceRange != null && !priceRange.isEmpty()) {
            String[] priceParts = priceRange.split("-");
            if (priceParts.length == 2) {
                minPrice = Long.parseLong(priceParts[0]);
                maxPrice = Long.parseLong(priceParts[1]);
            }
        }

        // 정렬 기준 설정
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sort);

        // Construct the query with filters
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(
                        product.status.eq(BID),
                        categoryEq(category),
                        priceBetween(minPrice, maxPrice),
                        productNameContains(search)
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(product)
                .where(
                        product.status.eq(BID),
                        categoryEq(category),
                        priceBetween(minPrice, maxPrice),
                        productNameContains(search)
                )
                .fetch().size();

        return new PageImpl<>(products, pageable, total);
    }

    // 정렬 기준을 동적으로 설정하는 메서드
    private OrderSpecifier<?> getOrderSpecifier(String sort) {
        return switch (sort) {
            case "auctionNowPrice" -> product.auctionNowPrice.asc();
            case "currentPrice" -> product.currentPrice.desc();
            case "likeCount" -> product.likeCount.desc();
            default -> product.createdAt.desc();
        };
    }

    // 카테고리 필터 메서드
    private BooleanExpression categoryEq(Long category) {
        return category != null ? product.category.id.eq(category) : null;
    }

    private BooleanExpression priceBetween(Long minPrice, Long maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return product.currentPrice.between(minPrice, maxPrice);
        }
        return null;
    }

    private BooleanExpression productNameContains(String search) {
        return search != null ? product.productName.containsIgnoreCase(search) : null;
    }
}
