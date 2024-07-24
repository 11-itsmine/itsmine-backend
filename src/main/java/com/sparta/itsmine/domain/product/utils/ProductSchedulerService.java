package com.sparta.itsmine.domain.product.utils;

import com.sparta.itsmine.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ProductSchedulerService {

    private final ProductRepository productRepository;

    @Scheduled(fixedRate = 2000)
    public void updateProductStatuses() {
        long updatedCount = productRepository.updateProductsToFailBid();
        if (updatedCount != 0L) {
            System.out.println(
                    "Updated " + updatedCount + " product(s) at " + java.time.LocalDateTime.now());
        }

    }
}
