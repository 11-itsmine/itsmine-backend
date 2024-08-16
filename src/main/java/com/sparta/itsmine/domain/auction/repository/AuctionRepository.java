package com.sparta.itsmine.domain.auction.repository;

import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.user.entity.User;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuctionRepository extends JpaRepository<Auction, Long>, CustomAuctionRepository {

    void deleteAllByProductId(Long productId);

    List<Auction> findAllByProductId(Long productId);

    Auction findByProductId(Long productId);

    Auction findByBidPrice(Integer bidPrice);


}
