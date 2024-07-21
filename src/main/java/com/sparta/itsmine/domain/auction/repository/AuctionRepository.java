package com.sparta.itsmine.domain.auction.repository;

import com.sparta.itsmine.domain.auction.entity.Auction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuctionRepository extends JpaRepository<Auction,Long>,CustomAuctionRepository {

    List<Auction> findAllByUserIdAndProductId(Long userId,Long productId);
}
