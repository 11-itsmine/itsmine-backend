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
        // 자기 물건에 좋아요 가능하게 만든다.
        Product product = productAdapter.getProduct(productId); // 좋아요할 상품을 찾는다.
        boolean likeExists = likeAdapter.existsLike(product.getId(),
                user.getId()); // 해당 상품에 내가 좋아요를 했는지 찾는다. Empty, true

        if (!product.getLike() && likeExists) {
            product.toggleLike(); // Set like to true
            likeRepository.save(new Like(product, user));
            return SUCCESS_TO_LIKE;
        } else {
            product.toggleLike(); // Set like to false
            likeRepository.deleteByProductIdAndUserId(product.getId(), user.getId());
            return SUCCESS_TO_REMOVE_LIKE;
        }
    }
}
