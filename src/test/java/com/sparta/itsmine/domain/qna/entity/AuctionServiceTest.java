package com.sparta.itsmine.domain.qna.entity;

import static com.sparta.itsmine.domain.user.utils.UserRole.USER;
import static com.sparta.itsmine.global.common.ResponseExceptionEnum.AUCTION_NOT_FOUND;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sparta.itsmine.domain.auction.dto.GetAuctionByUserResponseDto;
import com.sparta.itsmine.domain.auction.repository.AuctionRepository;
import com.sparta.itsmine.domain.auction.service.AuctionService;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.exception.Auction.AuctionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)//@ExtendWith(MockitoExtension.class): Mockito 확장을 사용하여 Mockito 주입을 활성화합니다
public class AuctionServiceTest {

    @Mock//@Mock과 @InjectMocks를 사용하여 목 객체와 주입 객체를 설정합니다
    private AuctionRepository auctionRepository;

    @InjectMocks//@Mock과 @InjectMocks를 사용하여 목 객체와 주입 객체를 설정합니다
    private AuctionService auctionService;

    private User testUser;
    private Pageable pageable;
    private Page<GetAuctionByUserResponseDto> auctionPage;

    @BeforeEach//@BeforeEach 메서드에서 테스트에 필요한 사용자, 페이징 정보, 그리고 경매 목록을 설정합니다.
    public void setUp() {
        testUser = new User("aaa","Xv40101392.","K","KKK","aaaa@aaaa.com",USER,"인천");
        testUser.setId(1L);

        pageable = PageRequest.of(0, 10);

        //GetAuctionByUserResponseDto 객체를 생성하여 테스트 데이터를 만듭니다.

        GetAuctionByUserResponseDto auctionDto = new GetAuctionByUserResponseDto(1L, 200, 1L);
        List<GetAuctionByUserResponseDto> auctionList = Collections.singletonList(auctionDto);

        auctionPage = new PageImpl<>(auctionList, pageable, auctionList.size());
    }

    @Test//testGetAuctionByUserSuccess(): 경매 목록을 성공적으로 반환하는 경우를 테스트합니다.
    public void testGetAuctionByUserSuccess() {
        //when: 목 객체의 메서드 호출 시 반환 값을 지정합니다.
        when(auctionRepository.findAuctionAllByUserid(testUser.getId(), pageable))
                .thenReturn(auctionPage);

        Page<GetAuctionByUserResponseDto> result = auctionService.getAuctionByUser(testUser, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().get(0).getProductId());
        assertEquals(200, result.getContent().get(0).getBidPrice());
        assertEquals(1, result.getContent().get(0).getUserId());
        //verify: 특정 메서드가 호출되었는지 검증합니다.
        verify(auctionRepository, times(1)).findAuctionAllByUserid(testUser.getId(), pageable);
    }

    @Test//testGetAuctionByUserAuctionNotFound(): 경매 목록이 존재하지 않는 경우를 테스트하고 예외가 발생하는지 검증합니다.
    public void testGetAuctionByUserAuctionNotFound() {
        //when: 목 객체의 메서드 호출 시 반환 값을 지정합니다.
        when(auctionRepository.findAuctionAllByUserid(testUser.getId(), pageable))
                .thenReturn(null);

        //assertThrows: 특정 예외가 발생하는지 검증합니다.
        AuctionNotFoundException exception = assertThrows(AuctionNotFoundException.class, () -> {
            auctionService.getAuctionByUser(testUser, pageable);
        });

        assertEquals(AUCTION_NOT_FOUND, exception.getMessage());
        //verify: 특정 메서드가 호출되었는지 검증합니다.
        verify(auctionRepository, times(1)).findAuctionAllByUserid(testUser.getId(), pageable);
    }
}
