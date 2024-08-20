package com.sparta.itsmine.domain.product.scheduler;

import static com.sparta.itsmine.domain.product.utils.ProductStatus.BID;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.FAIL_BID;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.NEED_PAYMENT_FOR_SUCCESS_BID;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.SUCCESS_BID;
import static com.sparta.itsmine.global.common.config.RabbitConfig.DELAYED_QUEUE_NAME;

import com.rabbitmq.client.Channel;
import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.auction.service.AuctionService;
import com.sparta.itsmine.domain.kakaopay.service.KakaoPayService;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

    private final ProductRepository productRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionService auctionService;
    private final KakaoPayService kakaoPayService;

    @RabbitListener(queues = DELAYED_QUEUE_NAME)
    public void receiveMessage(Long productId, Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            log.error("productId : {} ", productId);
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            boolean hasNoAuctions = auctionRepository.findAllByProductId(productId).isEmpty();
            List<Auction> auctions = auctionRepository.findAllByProductId(productId);

            LocalDateTime now = LocalDateTime.now();

            //DueDate가 다 되고 NEED_PAYMENT 상태의 입찰만 있거나 입찰 기록이 아얘 없으면 유찰
            //NEED_PAYMENT 상태의 입찰만 있으면 삭제하고 유찰 처리
            if (product.getDueDate().isBefore(now) && product.getStatus().equals(BID)) {
                //뭐가 됐든 DueDate가 다 됐으면 NEED_PAYMENT 상태의 입찰은 다 제거해줘야함
                auctionService.deleteAllNeedPay(auctions);

                if (hasNoAuctions) {
                    product.updateStatus(FAIL_BID);
                    productRepository.save(product);
                } else {
                    product.updateStatus(NEED_PAYMENT_FOR_SUCCESS_BID);
                    productRepository.save(product);

                    Auction auction = auctionRepository.findByProductIdAndMaxBid(product.getId());
                    auction.updateStatus(NEED_PAYMENT_FOR_SUCCESS_BID);//마감 시간 다 되서 낙찰된 건 10%로 밖에 결제를 안 했으니 나머지를 결제해야함
                    auctionRepository.save(auction);
                    kakaoPayService.deleteWithOutSuccessfulAuction(product.getId());
                }
                log.info("Product with ID {} has been marked as {}.", productId,
                        product.getStatus());
            } else {
                log.info("Product with ID {} is still valid.", productId);
            }
            channel.basicNack(tag, false, false);
        } catch (Exception e) {
            log.error("Error processing message with productId {}: {}", productId, e.getMessage());
            try {
                // 메시지 재처리 없이 삭제
                channel.basicNack(tag, false, false);
            } catch (IOException ioException) {
                log.error("Error acknowledging message: {}", ioException.getMessage());
            }
        }
    }
}