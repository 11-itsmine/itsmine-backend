package com.sparta.itsmine.domain.product.scheduler;

import static com.sparta.itsmine.domain.product.utils.ProductStatus.FAIL_BID;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.SUCCESS_BID;
import static com.sparta.itsmine.global.common.config.RabbitConfig.PRODUCT_QUEUE_NAME;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.PRODUCT_NOT_FOUND;

import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.global.exception.DataNotFoundException;
import java.time.LocalDateTime;
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

    @RabbitListener(queues = PRODUCT_QUEUE_NAME)
    public void receiveMessage(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(PRODUCT_NOT_FOUND));

        boolean hasNoAuctions = auctionRepository.findAllByProductId(productId).isEmpty();

        if (product.getDueDate().isBefore(LocalDateTime.now()) || product.getAuctionNowPrice()
                .equals(product.getCurrentPrice())) {
            product.updateStatus(hasNoAuctions ? FAIL_BID : SUCCESS_BID);
            productRepository.save(product);
            log.info("Product with ID {} has been marked as {}.", productId, product.getStatus());
        } else {
            log.info("Product with ID {} is still valid.", productId);
        }
    }
}