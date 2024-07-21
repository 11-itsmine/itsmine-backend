package com.sparta.itsmine.domain.auction.repository;

import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByMaxedBidPriceResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByProductResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByUserResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import java.util.List;

public interface CustomAuctionRepository {

    List<Auction> findAuctionAllByUserid(Long userId);

    //자신이 고른 상품 전체 조회
    List<GetAuctionByUserResponseDto> findAuctionAllByUserid2(Long userId);

    //자신이 고른 상품 조회
    GetAuctionByProductResponseDto findByUserIdAndProductId(Long UserId, Long productId);

    //해당 상품에 대한 모든 입찰가를 찾기(최댓값 빼고)
    List<Auction> findAllByProductIdWithOutMaxPrice(Long productId);

    //해당 상품 최고가 찾기
    GetAuctionByMaxedBidPriceResponseDto findByProductBidPrice(Long productId);
}
