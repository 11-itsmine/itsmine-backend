package com.sparta.itsmine.domain.like.repository;

import static com.sparta.itsmine.domain.like.entity.QLike.like;
import static com.sparta.itsmine.domain.product.entity.QProduct.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.itsmine.domain.like.entity.Like;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepositoryImpl implements CustomLikeRepository {

    private final JPAQueryFactory queryFactory;

    public LikeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Like> findAllByUserIdAndDeletedIsNull(Long userId, Pageable pageable) {
        List<Like> likes = queryFactory.selectFrom(like)
                .join(like.product, product).fetchJoin()
                .where(like.user.id.eq(userId)
                        .and(product.deletedAt.isNull()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(like)
                .where(like.user.id.eq(userId)
                        .and(product.deletedAt.isNull()))
                .fetch().size();

        return new PageImpl<>(likes, pageable, total);
    }
}
