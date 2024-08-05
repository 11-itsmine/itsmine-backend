package com.sparta.itsmine.domain.like.dto;

import com.sparta.itsmine.domain.like.entity.Like;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeResponseDto {

    private Long userId;
    private Long productId;
    private boolean isLiked; // 사용자가 좋아요를 눌렀는지 여부 추가

    public LikeResponseDto(Like like, boolean isLiked) {
        this.userId = like.getUser().getId();
        this.productId = like.getProduct().getId();
        this.isLiked = isLiked;
    }
}
