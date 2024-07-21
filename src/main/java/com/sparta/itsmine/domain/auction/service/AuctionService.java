package com.sparta.itsmine.domain.auction.service;


import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByProductResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByUserResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.entity.ProductRepository;
import com.sparta.itsmine.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;

    //입찰 생성(현재 입찰가 미만이거나 즉시구매가를 넘어서 입찰하려하면 예외처리를 해줘야함)
    @Transactional
    public AuctionResponseDto createAuction(User user, Long productId,
            AuctionRequestDto requestDto) {
        Product product = productRepository.findById(productId).orElseThrow();
        Long auctionPrice = requestDto.getBidPrice();

        Auction auction = new Auction(user, product, auctionPrice);

        auctionRepository.save(auction);
        return new AuctionResponseDto(auction);
    }

    //유저 입찰 조회(queryDSL,stream 조회)(각각 입찰한 상품 당 자신의 최대입찰가만 나오게끔)
    public List<AuctionResponseDto> getAuctionByUser(User user) {

        List<Auction> auctions = auctionRepository.findAuctionAllByUserid(user.getId());

        Map<Long, Auction> maxBidAuctions = auctions.stream()
                .collect(Collectors.toMap(auction -> auction.getProduct().getId(),
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparingLong(Auction::getBidPrice))));

        return maxBidAuctions.values().stream()
                .map(AuctionResponseDto::new)
                .collect(Collectors.toList());

    }

    //유저 입찰 조회(queryDSL 조회)(각각 입찰한 상품 당 자신의 최대입찰가만 나오게끔)
    public List<GetAuctionByUserResponseDto> getAuctionByUser2(User user) {
        List<GetAuctionByUserResponseDto> auctions = auctionRepository.findAuctionAllByUserid2(user.getId());

        return auctions.stream().toList();
    }

    //상품 입찰 조회(자신이 입찰한 상품의 자신의 최대입찰가만 나오게끔)
    public GetAuctionByProductResponseDto getAuctionByProduct(User user, Long productId) {

        return auctionRepository.findByUserIdAndProductId(user.getId(), productId);
    }

}
