package com.sparta.itsmine.domain.product;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.product.utils.ProductSchedulerService;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductSchedulerServiceTest {

    @InjectMocks
    private ProductSchedulerService productSchedulerService;

    @Mock
    private ProductRepository productRepository;

    private Product createTestProduct(LocalDateTime dueDate, ProductStatus status, String name) {
        Product product1 = Product.builder()
                .productName(name)
                .description("Description for " + name)
                .currentPrice(100000)
                .auctionNowPrice(200000)
                .dueDate(dueDate)
                .category(new Category("TestCategory"))
                .build();
        product1.turnStatus(status);
        return product1;
    }

    @BeforeEach
    public void setup() {
        Product dueProduct = createTestProduct(LocalDateTime.now().minusDays(1), ProductStatus.BID,
                "DueProduct");
        Product notDueProduct = createTestProduct(LocalDateTime.now().plusDays(1),
                ProductStatus.BID, "NotDueProduct");
        when(productRepository.findAll()).thenReturn(Arrays.asList(dueProduct, notDueProduct));
    }

    private void logStatusChange(Product product, ProductStatus newStatus) {
        ProductStatus oldStatus = product.getStatus();
        product.turnStatus(newStatus);
        System.out.println(
                "Product " + product.getProductName() + " status changed from " + oldStatus + " to "
                        + newStatus);
    }

    private void simulateStatusUpdate(List<Product> products, ProductStatus newStatus) {
        products.forEach(product -> {
            if (product.getDueDate().isBefore(LocalDateTime.now())
                    && product.getStatus() != newStatus) {
                logStatusChange(product, newStatus);
            }
        });
    }

    @Test
    void testUpdateProductStatusesWithStatusChangeLogging() {
        doAnswer((InvocationOnMock invocation) -> {
            List<Product> products = productRepository.findAll(); // Assuming this is where products would be fetched in real use
            simulateStatusUpdate(products, ProductStatus.FAIL_BID);
            return 1L;  // Mimicking that one product was updated
        }).when(productRepository).updateProductsToFailBid();

        // Act
        productSchedulerService.updateProductStatuses();

        // Assert
        verify(productRepository, times(1)).updateProductsToFailBid();
    }
}
