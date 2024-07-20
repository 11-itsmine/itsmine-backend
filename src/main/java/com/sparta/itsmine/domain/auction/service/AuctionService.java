package com.sparta.itsmine.domain.auction.service;


import com.sparta.itsmine.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsmine.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.entity.ProductRepository;
import com.sparta.itsmine.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;

    @Transactional
    public AuctionResponseDto createAuction(User user, Long productId,AuctionRequestDto requestDto) {
        Product product=productRepository.findById(productId).orElseThrow();
        Long auctionPrice=requestDto.getAuctionPrice();

        Auction auction=new Auction(user,product,auctionPrice);

        auctionRepository.save(auction);
        return new AuctionResponseDto(requestDto);
    }

//    public AuctionResponseDto

}
