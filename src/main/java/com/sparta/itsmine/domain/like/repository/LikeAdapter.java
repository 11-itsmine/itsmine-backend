package com.sparta.itsmine.domain.like.repository;

import com.sparta.itsmine.domain.like.entity.Like;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeAdapter {

    private final LikeRepository likeRepository;

    public boolean existsLike(Long userId, Long productId) {
        return likeRepository.findByProductIdAndUserId(userId, productId).isPresent();
    }

    public Like getLike(Long productId, Long userId) {
        return likeRepository.findByProductIdAndUserId(productId, userId).orElseThrow(() ->
                new DataNotFoundException(ResponseExceptionEnum.LIKE_NOT_FOUND));
    }

    public boolean isUserLikedProduct(Long productId, User user) {
        return likeRepository.existsByProductIdAndUserId(productId, user.getId());
    }

}
