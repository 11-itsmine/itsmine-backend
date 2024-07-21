package com.sparta.itsmine.domain.auction.repository;

import com.sparta.itsmine.domain.auction.entity.Auction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuctionRepository extends JpaRepository<Auction, Long>, CustomAuctionRepository {

    void deleteAllByProductId(Long productId);

    Auction findByProductId(Long productId);

    boolean existsByProductId(Long productId);
}
