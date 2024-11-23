package com.ecommerce.api.domain.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {

    CREDIT_CARD("신용 카드"),
    DEBIT_CARD("직불 카드"),
    PAYPAL("페이팔"),
    BANK_TRANSFER("게좌이체"),

    ;


    private final String description;
}
