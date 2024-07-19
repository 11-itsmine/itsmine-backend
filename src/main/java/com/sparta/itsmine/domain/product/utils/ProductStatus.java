package com.sparta.itsmine.domain.product.utils;

public enum ProductStatus {
    DID("DID"),
    SUCCESS_DID("SUCCESS_DID"),
    AUCTION("AUCTION");

    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }
}
