package com.sparta.itsmine.domain.like.service;

import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_TO_LIKE;
import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.SUCCESS_TO_REMOVE_LIKE;

import com.sparta.itsmine.domain.like.entity.Like;
import com.sparta.itsmine.domain.like.repository.LikeAdapter;
import com.sparta.itsmine.domain.like.repository.LikeRepository;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductAdapter;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.response.ResponseCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ProductAdapter productAdapter;
    private final LikeAdapter likeAdapter;

    @Transactional
    public ResponseCodeEnum createLike(Long productId, User user) {

        Product product = productAdapter.getProduct(productId);

        return likeRepository.findByProductIdAndUserId(productId, user.getId())
                .map(like -> {
                    likeRepository.deleteByProductIdAndUserId(productId, user.getId());
                    return SUCCESS_TO_REMOVE_LIKE;
                })
                .orElseGet(() -> {
                    likeRepository.save(new Like(product, user));
                    return SUCCESS_TO_LIKE;
                });
    }
}
