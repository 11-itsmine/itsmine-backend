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
        Product product = productAdapter.getProduct(productId); // 좋아요할 상품을 찾는다.
        boolean likeExists = likeAdapter.existsLike(product.getId(),
                user.getId()); // 해당 상품에 내가 좋아요를 했는지 찾는다.

        if (!likeExists) {
            // 사용자가 해당 상품에 좋아요를 누르지 않은 경우
            Like newLike = new Like(product, user);
            likeRepository.save(newLike);
            return SUCCESS_TO_LIKE;
        } else {
            // 사용자가 해당 상품에 이미 좋아요를 누른 경우
            Like existingLike = likeAdapter.getLike(product.getId(), user.getId());
            likeRepository.delete(existingLike);
            return SUCCESS_TO_REMOVE_LIKE;
        }
    }
}
