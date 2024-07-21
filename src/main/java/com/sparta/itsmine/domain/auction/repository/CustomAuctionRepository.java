package com.sparta.itsmine.domain.auction.repository;

import com.sparta.itsmine.domain.auction.dto.GetAuctionByProductResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByUserResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import java.util.List;

public interface CustomAuctionRepository {
    List<Auction> findAuctionAllByUserid(Long userId);

    List<GetAuctionByUserResponseDto> findAuctionAllByUserid2(Long userId);

    GetAuctionByProductResponseDto findByUserIdAndProductId(Long UserId, Long productId);
}
