package com.sparta.itsmine.domain.auction.service;


import com.sparta.itsmine.domain.auction.dto.AuctionProductImageResponseDto;
import com.sparta.itsmine.domain.auction.dto.AuctionProductResponseDto;
import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.auction.repository.AuctionAdapter;
import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductAdapter;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.product.scheduler.MessageSenderService;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import com.sparta.itsmine.domain.redis.RedisService;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.lock.DistributedLock;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

import lombok.RequiredArgsConstructor;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
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

	private final String LOCK_KEY_PREFIX = "auctionLock";
	private final RedisTemplate<String, Integer> redisTemplate;

	@DistributedLock(prefix = LOCK_KEY_PREFIX, key = "productId")
	public AuctionResponseDto createAuction(User user, Long productId,
		AuctionRequestDto requestDto, Integer totalAmount) {
		Integer redisPrice = redisTemplate.opsForValue().get("bidprice" + productId);  // 입찰 가격 동시성 제어
		if (redisPrice != null && requestDto.getBidPrice() <= redisPrice) {
			throw new IllegalArgumentException("이미 더 높은 가격에 입찰중입니다.");
		}
		Product product = productAdapter.getProduct(productId);
		Integer bidPrice = requestDto.getBidPrice();
		ProductStatus status = ProductStatus.NEED_PAY;

		Auction auction = createAuctionEntity(user, product, bidPrice, status, totalAmount);

		checkAuctionValidity(auction, product, bidPrice, user);
		redisTemplate.opsForValue().set("bidprice:" + productId, requestDto.getBidPrice()); // 입찰 가격 동시성 제어
		auctionRepository.save(auction);
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

	public Page<AuctionProductImageResponseDto> getAuctionByUser(User user, Pageable pageable) {
		return adapter.findAuctionAllByUserid(user.getId(), pageable);
	}

/*	public AuctionProductResponseDto getAuctionByProduct(User user, Long productId) {
		return adapter.findByUserIdAndProductId(user.getId(), productId);
	}*/
}
