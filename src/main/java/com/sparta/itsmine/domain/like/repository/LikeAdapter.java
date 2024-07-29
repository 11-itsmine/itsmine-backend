package com.sparta.itsmine.domain.like.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeAdapter {

    private final LikeRepository likeRepository;

    public boolean existsLike(Long productId, Long userId) {
        return likeRepository.findByProductIdAndUserId(productId, userId).isEmpty();
    }
}
