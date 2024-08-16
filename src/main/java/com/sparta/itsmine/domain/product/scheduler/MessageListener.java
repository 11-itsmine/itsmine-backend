package com.sparta.itsmine.domain.product.scheduler;

import static com.sparta.itsmine.domain.product.utils.ProductStatus.FAIL_BID;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.NEED_PAY;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.SUCCESS_BID;
import static com.sparta.itsmine.global.common.config.RabbitConfig.DELAYED_QUEUE_NAME;

import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.kakaopay.service.KakaoPayService;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

    private final ProductRepository productRepository;
    private final AuctionRepository auctionRepository;
    private final KakaoPayService kakaoPayService;

    @RabbitListener(queues = DELAYED_QUEUE_NAME)
    public void receiveMessage(Long productId) {
        log.error("productId : {} ", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean hasNoAuctions = auctionRepository.findAllByProductId(productId).isEmpty();
        List<Auction> auctions = auctionRepository.findAllByProductId(productId);

        LocalDateTime now = LocalDateTime.now();

        //DueDate가 다 되고 NEED_PAY 상태의 입찰만 있거나 입찰 기록이 아얘 없으면 유찰
        //NEED_PAY 상태의 입찰만 있으면 삭제하고 유찰 처리
        if (product.getDueDate().isBefore(now)) {
            //뭐가 됐든 DueDate가 다 됐으면 NEED_PAY 상태의 입찰은 다 제거해줘야함
            for (Auction auction : auctions) {
                if (auction.getStatus().equals(NEED_PAY)) {
                    auctionRepository.delete(auction);
                }
            }
            //즉시 구매가와 상품 현재가가 같으면 상품 상태를 SUCCESS_BID로 바꿔준다
            if(product.getAuctionNowPrice().equals(product.getCurrentPrice())){
                product.updateStatus(SUCCESS_BID);//(사실 DueDate 마감으로 낙찰이 되면 추가 결제로 마저 결제하게끔 만들어야 하는데 그건 너무 큰 작업이라 일단 미루겠습니다)
                productRepository.save(product);
                kakaoPayService.deleteWithOutSuccessfulAuction(product.getId());
                Auction auction=auctionRepository.findByProductId(product.getId());
                auction.updateStatus(SUCCESS_BID);
                auctionRepository.save(auction);
            }
            else if(hasNoAuctions){
                product.updateStatus(FAIL_BID);
                productRepository.save(product);
            }
            log.info("Product with ID {} has been marked as {}.", productId, product.getStatus());
        }
        else {
            log.info("Product with ID {} is still valid.", productId);
        }
    }
}