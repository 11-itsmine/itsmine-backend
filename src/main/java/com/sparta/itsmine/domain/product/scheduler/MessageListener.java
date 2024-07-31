package com.sparta.itsmine.domain.product.scheduler;

import static com.sparta.itsmine.domain.product.utils.ProductStatus.FAIL_BID;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.SUCCESS_BID;
import static com.sparta.itsmine.global.common.config.RabbitConfig.PRODUCT_QUEUE_NAME;

import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
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
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 입찰 기록 확인 - ProductId를 통해서 입찰 테이블에 기록된것을 찾습니다.
        boolean existAutction = auctionRepository.findAllByProductId(productId).isEmpty();
        // 상태가 만료 상태인지 확인
        if (product.getDueDate().isBefore(LocalDateTime.now())) {
            product.updateStatus(SUCCESS_BID);
            productRepository.save(product);
            log.info("Product with ID {} has been succeed as {}.", productId, SUCCESS_BID);
        } else if (product.getDueDate().isBefore(LocalDateTime.now()) && existAutction) {
            product.updateStatus(FAIL_BID);
            productRepository.save(product);
            log.info("Product with ID {} has been marked as {}.", productId, FAIL_BID);
        } else {
            log.info("Product with ID {} is still valid.", productId);
        }
    }
}