package com.sparta.itsmine.domain.product.utils;

public enum ProductStatus {
    BID,
    SUCCESS_BID,
    FAIL_BID,
    NEED_PAYMENT,//결제가 되지 않은 상태
    NEED_PAYMENT_FOR_SUCCESS_BID//경매 시간 마감 뒤 입찰 필요
    ;
}
