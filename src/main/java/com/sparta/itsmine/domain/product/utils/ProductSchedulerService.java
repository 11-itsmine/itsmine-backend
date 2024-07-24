package com.sparta.itsmine.domain.product.utils;

import static com.sparta.itsmine.domain.product.utils.ProductStatus.FAIL_BID;
import static com.sparta.itsmine.domain.product.utils.ProductStatus.SUCCESS_BID;

import com.sparta.itsmine.domain.auction.entity.Auction;
import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.auction.service.AuctionService;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ProductSchedulerService {

    private final ProductRepository productRepository;
    private final TaskScheduler taskScheduler;
    private final AuctionService auctionService;
    private final AuctionRepository auctionRepository;

    @Scheduled(fixedRate = 1000)  // Schedule this method to run every second
    public void updateProductStatuses() {
        long updatedCount = productRepository.updateProductsToFailBid();
        if (updatedCount != 0L) {
            System.out.println(
                    "Updated " + updatedCount + " product(s) at " + java.time.LocalDateTime.now());
        }
    }

    //낙찰은 SUCCESS_BID로 상태를 바꿔주는 메소드가 필요한데 없는 거 같음(입찰 생성할 때 상품 즉시구매가를 넣으면 바꿔주는게 좋다고 생각함(Auction에서(했음)))
    //유찰은 낙찰자가 없을 때(dueDate가 마감 될 때까지 입찰자가 아무도 없거나(했음),낙찰자가 낙찰을 취소했을 때(이건 고민해봐야함),상품 경매를 취소했을 때(ProductService에서(했음)))
    /*@Transactional
    public void updateProductStatusFailBid(Product product) {
        if (LocalDateTime.now().isAfter(product.getDueDate()) && product.getStatus() != FAIL_BID) {
            product.turnStatus(FAIL_BID);
            productRepository.save(product);
        }
    }

    //낙찰은 dueDate가 마감 될 때 입찰 기록이 존재하면 낙찰
    @Transactional
    public void updateProductStatusSuccessBid(Product product) {
        if (LocalDateTime.now().isAfter(product.getDueDate()) && product.getStatus() != SUCCESS_BID) {
            product.turnStatus(SUCCESS_BID);
            productRepository.save(product);
            auctionService.successfulAuction(product.getId());
        }
    }*/
}
