package com.sparta.itsmine.domain.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.product.utils.ProductSchedulerService;
import com.sparta.itsmine.domain.product.utils.ProductStatus;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;

/**
 * 스프링 부트와 Mockito를 사용한 JUnit 테스트 클래스입니다. 이 클래스는 제품의 상태가 스케줄된 시간에 따라 올바르게 업데이트되는지 테스트합니다.
 */
@ExtendWith(MockitoExtension.class)
public class ProductSchedulerServiceTest {

    /**
     * 제품 정보를 데이터베이스에서 관리하는 레포지토리를 모의 객체로 생성합니다.
     */
    @Mock
    private ProductRepository productRepository;

    /**
     * 작업 스케줄링을 담당하는 TaskScheduler를 모의 객체로 생성합니다.
     */
    @Mock
    private TaskScheduler taskScheduler;

    /**
     * 의존성 주입을 통해 ProductSchedulerService의 인스턴스를 생성합니다. 이 클래스는 실제 비즈니스 로직을 테스트합니다.
     */
    @InjectMocks
    private ProductSchedulerService productSchedulerService;

    /**
     * 스케줄된 작업을 캡처하기 위한 ArgumentCaptor를 선언합니다.
     */
    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    private Product product;

    /**
     * 제품의 카테고리 정보를 관리하는 Category 엔티티의 모의 객체를 생성합니다.
     */
    @Mock
    private Category category;

    /**
     * 제품 객체를 캡처하기 위한 ArgumentCaptor를 선언합니다.
     */
    @Captor
    private ArgumentCaptor<Product> productArgumentCaptor;

    /**
     * 각 테스트 실행 전에 공통 설정을 초기화하는 메서드입니다. 여기서는 제품 객체를 초기화하고, 필요한 Mockito 설정을 수행합니다.
     */
    @BeforeEach
    public void setup() {
        product = Product.builder()
                .productName("JakeShoes")
                .description("this is shoes Jake wore")
                .currentPrice(1000000)
                .auctionNowPrice(2100000)
                .dueDate(LocalDateTime.now().minusSeconds(10)) // 과거로 설정하여 실패 조건을 트리거합니다.
                .category(Category.builder().categoryName("Boom").build())
                .build();
        product.turnStatus(ProductStatus.BID); // SAVED -> BID 입찰중 으로 바꾼다.

        System.out.println(product.getStatus());

        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
    }

    /**
     * ProductSchedulerService의 스케줄링 로직을 테스트하는 메서드입니다. 스케줄러가 제대로 설정되고, 기준 시간에 따라 제품 상태가 FAIL_BID로
     * 변경되는지 확인합니다.
     *
     * @throws Exception 리플렉션을 사용할 때 발생할 수 있는 예외를 처리합니다.
     */
    @Test
    public void testScheduleProductUpdates() throws Exception {
        productSchedulerService.scheduleProductUpdates();

        // 리플렉션을 사용하여 private 메서드를 호출하고 직접 업데이트 로직을 테스트합니다.
        Method updateMethod = ProductSchedulerService.class.getDeclaredMethod("updateProductStatus",
                Product.class);
        updateMethod.setAccessible(true);
        updateMethod.invoke(productSchedulerService, product);

        // productRepository.save(...)가 호출되었는지 확인하고, 제품 상태가 올바르게 변경되었는지 검증합니다.
        verify(productRepository).save(productArgumentCaptor.capture());
        Product savedProduct = productArgumentCaptor.getValue();

        System.out.println(savedProduct.getStatus());

        assertEquals(ProductStatus.FAIL_BID, savedProduct.getStatus(),
                "제품 상태는 FAIL_BID로 업데이트되어야 합니다.");
    }
}


