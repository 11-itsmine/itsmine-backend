package com.sparta.itsmine.domain.auction.service;


import static com.sparta.itsmine.domain.product.utils.ProductStatus.SUCCESS_BID;

import com.sparta.itsmine.domain.auction.dto.AuctionProductResponseDto;
import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.auction.repository.AuctionAdapter;
import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductAdapter;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import com.sparta.itsmine.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final AuctionAdapter adapter;
    private final ProductAdapter productAdapter;


    @Transactional
    public AuctionResponseDto createAuction(User user, Long productId,
            AuctionRequestDto requestDto) {
        Product product = productAdapter.getProduct(productId);
        Integer bidPrice = requestDto.getBidPrice();
        ProductStatus status = product.getStatus();

        Auction auction = new Auction(user, product, bidPrice, status);
        //현재 상품 상태가 입찰 중이 아니면 예외처리
        auction.checkStatus(status);

        //현재 입찰가(고른 상품에서 가장 높은 입찰가 or 상품 처음 입찰가) 이하이거나 즉시구매가를 넘어서 입찰하려하면 예외처리
        auction.checkBidPrice(bidPrice);

        //현 최대 입찰가보다 낮으면 예외처리
        auction.checkCurrentPrice(bidPrice, product.getCurrentPrice());

        //현재 구매가 갱신
        currentPriceUpdate(bidPrice, product);

        //입찰가를 즉시구매가 만큼 썼으면 즉시 낙찰
        if (bidPrice.equals(product.getAuctionNowPrice())) {
            successfulAuction(auction, productId);
        }
        auctionRepository.save(auction);
        return new AuctionResponseDto(auction);
    }

    public Page<AuctionProductResponseDto> getAuctionByUser(User user, Pageable pageable) {
        return adapter.findAuctionAllByUserid(user.getId(), pageable);
    }

    //상품 입찰 조회(자신이 입찰한 상품의 자신의 최대입찰가만 나오게끔)
    public AuctionProductResponseDto getAuctionByProduct(User user, Long productId) {
        return adapter.findByUserIdAndProductId(user.getId(), productId);
    }

    @Transactional
    public void successfulAuction(Auction auction, Long productId) {
        allDeleteBid(productId);
        auction.updateStatus(SUCCESS_BID);
        turnToSuccessBidProduct(productId);
    }

    public void turnToSuccessBidProduct(Long productId) {
        Product product = productAdapter.getProduct(productId);
        product.updateStatus(SUCCESS_BID);
        productRepository.save(product);
    }

    //유찰(상품ID로 조회해서 다 삭제)
    @Transactional
    public void avoidedAuction(Long productId) {
        allDeleteBid(productId);
    }


    public void allDeleteBid(Long productId) {
        auctionRepository.deleteAllByProductId(productId);
    }

    public void currentPriceUpdate(Integer bidPrice, Product product) {
        product.currentPriceUpdate(bidPrice);
        productRepository.save(product);
    }
}

