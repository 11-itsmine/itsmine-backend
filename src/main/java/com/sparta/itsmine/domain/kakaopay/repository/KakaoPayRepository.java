package com.sparta.itsmine.domain.kakaopay.repository;

import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.kakaopay.entity.KakaoPayTid;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KakaoPayRepository extends JpaRepository<KakaoPayTid, Long> {
    KakaoPayTid findByTid(String tid);

    Optional<KakaoPayTid> findByAuctionId(Long AuctionId);
}
