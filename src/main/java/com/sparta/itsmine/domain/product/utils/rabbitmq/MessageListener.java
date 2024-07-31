package com.sparta.itsmine.domain.product.utils.rabbitmq;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import java.time.LocalDateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private final ProductRepository productRepository;

    public MessageListener(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RabbitListener(queues = RabbitScheduleConfig.PRODUCT_QUEUE_NAME)
    public void receiveMessage(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 상태가 만료 상태인지 확인
        if (product.getDueDate().isBefore(LocalDateTime.now())) {
            product.updateStatus(ProductStatus.FAIL_BID);
            productRepository.save(product);
            System.out.println("Product with ID " + productId + " has been marked as expired.");
        } else {
            System.out.println("Product with ID " + productId + " is still valid.");
        }
    }
}