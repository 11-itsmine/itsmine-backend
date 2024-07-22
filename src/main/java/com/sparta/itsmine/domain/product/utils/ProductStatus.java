package com.sparta.itsmine.domain.product.utils;

public enum ProductStatus {
    SAVED,
    BID, // 입찰
    SUCCESS_BID, // 낙찰
    FAIL_BID, // 유찰, 무효화
    ;
}
