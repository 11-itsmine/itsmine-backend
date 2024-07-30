package com.sparta.itsmine.domain.auction.repository;

import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.AUCTION_NOT_FOUND;

import com.sparta.itsmine.domain.auction.dto.AuctionProductResponseDto;
import com.sparta.itsmine.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionAdapter {

    private final AuctionRepository auctionRepository;

    public Page<AuctionProductResponseDto> findAuctionAllByUserid(Long userId, Pageable pageable) {
        Page<AuctionProductResponseDto> auctions = auctionRepository.findAuctionAllByUserid(userId,
                pageable);

        if (auctions.isEmpty()) {
            throw new DataNotFoundException(AUCTION_NOT_FOUND);
        }

        return auctions;
    }

    public AuctionProductResponseDto findByUserIdAndProductId(Long userId, Long productId) {
        return auctionRepository.findByUserIdAndProductId(
                        userId, productId)
                .orElseThrow(() -> new DataNotFoundException(AUCTION_NOT_FOUND));
    }

}
