package com.sparta.itsmine.domain.auction.repository;

import static com.sparta.itsmine.domain.product.utils.ProductStatus.BID;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.AUCTION_NOT_FOUND;

import com.sparta.itsmine.domain.auction.dto.AuctionProductImageResponseDto;
import com.sparta.itsmine.domain.auction.dto.AuctionProductResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.kakaopay.entity.KakaoPayTid;
import com.sparta.itsmine.domain.kakaopay.repository.KakaoPayRepository;
import com.sparta.itsmine.domain.kakaopay.service.KakaoPayService;
import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.DataNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionAdapter {

    private final AuctionRepository auctionRepository;

    public Page<AuctionProductImageResponseDto> findAuctionAllByUserid(Long userId, Pageable pageable) {

        return auctionRepository.findAuctionAllByUserid(userId, pageable);
    }

    public Auction getAuction(Long id){
        return auctionRepository.findById(id).orElseThrow(()->new DataNotFoundException(AUCTION_NOT_FOUND));
    }

}
