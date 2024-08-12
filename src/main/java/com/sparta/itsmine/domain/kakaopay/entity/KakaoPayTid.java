package com.sparta.itsmine.domain.kakaopay.entity;


import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.kakaopay.dto.KakaoPayApproveRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "KakaoPayTid")
@NoArgsConstructor
//입찰과 연관관계를 만들어야겠다 어차피 입찰 보증금을 환불해주려고 하는건데 다른 연관관계는 필요 없을 거 같다
public class KakaoPayTid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cid;//가맹점 코드, 10자

    @Column(nullable = false, unique = true)
    private String tid;//결제 고유번호, 결제 준비 API 응답에 포함

    @Column(nullable = false)
    private Long partnerOrderId;//가맹점 주문번호, 결제 준비 API 요청과 일치해야 함

    @Column(nullable = false)
    private String partnerUserId;//가맹점 회원 id, 결제 준비 API 요청과 일치해야 함

    @Column(nullable = false)
    private String pgToken;//결제승인 요청을 인증하는 토큰 사용자 결제 수단 선택 완료 시, approval_url로 redirection 해줄 때 pg_token을 query string으로 전달

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;


    @Builder
    public KakaoPayTid(String cid, String tid, Long partnerOrderId, String partnerUserId,
            String pgToken, Auction auction) {
        this.cid = cid;
        this.tid = tid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.pgToken = pgToken;
        this.auction = auction;
    }
}
