package com.sparta.itsmine.domain.like.repository;

import com.sparta.itsmine.domain.like.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomLikeRepository {

    Page<Like> findAllByUserIdAndDeletedIsNull(Long userId, Pageable pageable);
}
