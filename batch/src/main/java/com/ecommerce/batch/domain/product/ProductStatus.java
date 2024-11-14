package com.ecommerce.batch.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {

    AVAILABLE("판매 중"),
    OUT_OF_STOCK("품절"),
    DISCONTINUED("판매 종료"),

    ;

    private final String description;
}
