package com.sparta.itsmine.domain.product.utils;

public enum ProductStatus {
    BID, // 입찰(경매에선 입찰,낙찰 두개만 씁니다)
    SUCCESS_BID, // 낙찰(경매에선 입찰,낙찰 두개만 씁니다)
    FAIL_BID, // 유찰, 무효화
    ;
}
