package com.sparta.itsmine.domain.product.dto;

import org.joda.time.DateTime;

public class ProductCreateDto {

    String productName;
    String description;
    Integer auctionNowPrice;
    Integer currentPrice;
    DateTime dueDate;
    String imageUrl;

}
