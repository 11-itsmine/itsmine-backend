package com.sparta.itsmine.domain.auction.repository;

import com.sparta.itsmine.domain.auction.dto.AuctionProductImageResponseDto;
import com.sparta.itsmine.domain.auction.dto.AuctionProductResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomAuctionRepository {

    //자신이 고른 상품 전체 조회
    Page<AuctionProductImageResponseDto> findAuctionAllByUserid(Long userId, Pageable pageable);

    Auction findByProductIdAndMaxBid(Long productId);

    Auction findByBidPriceAndUserAndProduct(Long userId,Long productId,Integer bidPrice);

    Auction findByProductIdForAdditionalPayment(Long productId);
}
