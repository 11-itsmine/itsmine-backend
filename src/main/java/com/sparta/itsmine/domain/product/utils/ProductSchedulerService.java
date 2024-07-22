package com.sparta.itsmine.domain.product.utils;

import static com.sparta.itsmine.domain.product.utils.ProductStatus.FAIL_BID;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSchedulerService {

    private final ProductRepository productRepository;
    private final TaskScheduler taskScheduler;

    @PostConstruct
    public void scheduleProductUpdates() {
        List<Product> products = productRepository.findAll();
        products.forEach(this::scheduleProductUpdate);
    }

    private void scheduleProductUpdate(Product product) {
        if (product.getStartDate() != null && product.getDueDate() != null) {
            Duration delay = Duration.between(LocalDateTime.now(), product.getDueDate());
            if (!delay.isNegative()) {
                taskScheduler.schedule(() -> updateProductStatus(product),
                        product.getDueDate().toInstant(
                                ZoneOffset.UTC));
            }
        }
    }

    private void updateProductStatus(Product product) {
        if (LocalDateTime.now().isAfter(product.getDueDate())
                && product.getStatus() != FAIL_BID) {
            product.turnStatus(FAIL_BID);
            productRepository.save(product);
        }
    }
}
