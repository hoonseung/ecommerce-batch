package com.ecommerce.api.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    PENDING_PAYMENT("결제 대기"),
    PROCESSING("처리 중"),
    COMPLETED("완료됨"),
    CANCELED("취소됨");

    private final String description;
}
