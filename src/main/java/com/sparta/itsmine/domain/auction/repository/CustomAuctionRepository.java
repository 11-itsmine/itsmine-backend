package com.sparta.itsmine.domain.auction.repository;

import com.sparta.itsmine.domain.auction.dto.AuctionProductResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomAuctionRepository {

    //자신이 고른 상품 전체 조회
    Page<AuctionProductResponseDto> findAuctionAllByUserid(Long userId, Pageable pageable);

    //자신이 고른 상품 조회
    Optional<AuctionProductResponseDto> findByUserIdAndProductId(Long UserId, Long productId);
}
