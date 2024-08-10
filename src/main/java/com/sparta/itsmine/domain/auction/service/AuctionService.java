package com.sparta.itsmine.domain.auction.service;

import static com.sparta.itsmine.domain.product.utils.ProductStatus.BID;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.SUCCESS_BID;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.AUCTION_DENIED_BID;

import com.sparta.itsmine.domain.auction.dto.AuctionProductResponseDto;
import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.auction.repository.AuctionAdapter;
import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.kakaopay.entity.KakaoPayTid;
import com.sparta.itsmine.domain.kakaopay.repository.KakaoPayRepository;
import com.sparta.itsmine.domain.kakaopay.service.KakaoPayService;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductAdapter;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.product.scheduler.MessageSenderService;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.exception.DataDuplicatedException;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final MessageSenderService messageSenderService;
    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final AuctionAdapter adapter;
    private final ProductAdapter productAdapter;
    private final RedissonClient redissonClient;
    private static final String LOCK_KEY = "auctionLock";

    public AuctionResponseDto createAuction(User user, Long productId,
            AuctionRequestDto requestDto, Integer totalAmount) {
        RLock lock = redissonClient.getFairLock(LOCK_KEY);
        Auction auction = null;
        try {
            boolean isLocked = lock.tryLock(10, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new IllegalArgumentException("입찰 생성 대기시간 초과");
            }

            try {
                Product product = productAdapter.getProduct(productId);
                Integer bidPrice = requestDto.getBidPrice();
                ProductStatus status = ProductStatus.NEED_PAY;

                auction = createAuctionEntity(user, product, bidPrice, status, totalAmount);

                checkAuctionValidity(auction, product, bidPrice, user);
                auctionRepository.save(auction);
            } finally {
                lock.unlock();
            }
        } catch (
                InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new AuctionResponseDto(auction);
    }

    private Auction createAuctionEntity(User user, Product product, Integer bidPrice,
            ProductStatus status, Integer totalAmount) {
        return new Auction(user, product, bidPrice, status, totalAmount);
    }

    private void checkAuctionValidity(Auction auction, Product product, Integer bidPrice,
            User user) {
        auction.checkUser(user, product);
        auction.checkStatus(product.getStatus());
        auction.checkBidPrice(bidPrice, product);
        auction.checkCurrentPrice(bidPrice, product.getCurrentPrice());
    }

    public void scheduleMessage(Long productId, LocalDateTime dueDate) {
        long delayMillis = calculateDelay(dueDate);
        messageSenderService.sendMessage(productId, delayMillis);
    }

    private long calculateDelay(LocalDateTime dueDate) {
        return dueDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                - System.currentTimeMillis();
    }

    public void allDeleteBid(Long productId) {
        auctionRepository.deleteAllByProductId(productId);
    }

    @Transactional
    public void avoidedAuction(Long productId) {
        allDeleteBid(productId);
    }

    public void currentPriceUpdate(Integer bidPrice, Product product) {
        product.currentPriceUpdate(bidPrice);
        productRepository.save(product);
    }

    public Page<AuctionProductResponseDto> getAuctionByUser(User user, Pageable pageable) {
        return adapter.findAuctionAllByUserid(user.getId(), pageable);
    }

    public AuctionProductResponseDto getAuctionByProduct(User user, Long productId) {
        return adapter.findByUserIdAndProductId(user.getId(), productId);
    }
}
