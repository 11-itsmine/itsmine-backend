package com.sparta.itsmine.domain.auction.service;


import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByMaxedBidPriceResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByProductResponseDto;
import com.sparta.itsmine.domain.auction.dto.GetAuctionByUserResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.entity.ProductRepository;
import com.sparta.itsmine.domain.product.entity.ProductResponseDto;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.Auction.AuctionNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    //입찰 생성(현재 입찰가(고른 상품에서 가장 높은 입찰가 or 상품 처음 입찰가) 이하이거나 즉시구매가를 넘어서 입찰하려하면 예외처리를 해줘야함,(조건은 나중에))
    @Transactional
    public AuctionResponseDto createAuction(User user, Long productId,
            AuctionRequestDto requestDto) {
        Product product = productRepository.findById(productId).orElseThrow();
        Long auctionPrice = requestDto.getBidPrice();
        GetAuctionByMaxedBidPriceResponseDto maxedBidPrice = auctionRepository.findByProductBidPrice(
                productId);

        //현재 입찰가(고른 상품에서 가장 높은 입찰가 or 상품 처음 입찰가) 이하이거나 즉시구매가를 넘어서 입찰하려하면 예외처리
        if (auctionPrice > product.getBuyNowPrice()
                || auctionPrice < product.getAuctionNowPrice()) {
            throw new IllegalArgumentException();
        }

        if (auctionRepository.existsByProductId(productId)) {
            if (auctionPrice <= maxedBidPrice.getBidPrice()) {
                throw new IllegalArgumentException();
            }
        }

        Auction auction = new Auction(user, product, auctionPrice);

        auctionRepository.save(auction);
        return new AuctionResponseDto(auction);
    }

    //유저 입찰 조회(queryDSL,stream 조회)(각각 입찰한 상품 당 자신의 최대입찰가만 나오게끔)
    public List<AuctionResponseDto> getAuctionByUser(User user) {

        List<Auction> auctions = auctionRepository.findAuctionAllByUserid(user.getId());
        if (auctions == null) {
            throw new AuctionNotFoundException(ResponseExceptionEnum.AUCTION_NOT_FOUND);
        }

        Map<Long, Auction> maxBidAuctions = auctions.stream()
                .collect(Collectors.toMap(auction -> auction.getProduct().getId(),//key로 사용
                        Function.identity(),//객체 자체를 값으로 사용
                        BinaryOperator.maxBy(Comparator.comparingLong(
                                Auction::getBidPrice))));//동일한 key에 대해 중복값이 있을 때 최대값을 선택

        return maxBidAuctions.values().stream()
                .map(AuctionResponseDto::new)
                .collect(Collectors.toList());

    }

    //유저 입찰 조회(queryDSL 조회)(각각 입찰한 상품 당 자신의 최대입찰가만 나오게끔)
    public List<GetAuctionByUserResponseDto> getAuctionByUser2(User user) {
        List<GetAuctionByUserResponseDto> auctions = auctionRepository.findAuctionAllByUserid2(
                user.getId());
        if (auctions == null) {
            throw new AuctionNotFoundException(ResponseExceptionEnum.AUCTION_NOT_FOUND);
        }

        return auctions.stream().toList();
    }

    //상품 입찰 조회(자신이 입찰한 상품의 자신의 최대입찰가만 나오게끔)
    public GetAuctionByProductResponseDto getAuctionByProduct(User user, Long productId) {
        GetAuctionByProductResponseDto productAuctions = auctionRepository.findByUserIdAndProductId(
                user.getId(), productId);
        if (productAuctions == null) {
            throw new AuctionNotFoundException(ResponseExceptionEnum.AUCTION_NOT_FOUND);
        }

        return productAuctions;
    }

    /*    낙찰 or 유찰은 상품 상태 확인하고 상품 관련된 입찰정보 삭제
                (낙찰은 가격에 MAX 함수 이용해서 최대값만 남겨둠(이런 방식으로 패찰도 거름),(최대값이 여러개면 남은 레코드 중 가장 나중에 생긴 시간 것만 갖고오기))
                (유찰은 걍 다 삭제)
                */
    //낙찰(유저 ID상관없이 최대 가격만 남기고 다 삭제하고 남은 것만 출력(이론상 1개가 남긴 하는데 동시에 추가될 가능성이 있을 수 있으니 그에 대한 예외처리가 필요함(입찰 생성에서 해결함)),(조건은 나중에))
    @Transactional
    public AuctionResponseDto successfulAuction(Long productId) {
        List<Auction> auctions = auctionRepository.findAllByProductIdWithOutMaxPrice(productId);
        if (auctions == null) {
            throw new AuctionNotFoundException(ResponseExceptionEnum.AUCTION_NOT_FOUND);
        }

        auctionRepository.deleteAll(auctions);

        Auction successfulBid = auctionRepository.findByProductId(productId);

        return new AuctionResponseDto(successfulBid);
    }

    //유찰(상품ID로 조회해서 다 삭제(조건은 나중에))
    @Transactional
    public ProductResponseDto avoidedAuction(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        auctionRepository.deleteAllByProductId(productId);
        return new ProductResponseDto(product);
    }

}
