package com.ecommerce.api.domain.order.dto;

import com.ecommerce.api.domain.orderProduct.dto.OrderProductRequest;

public record OrderProductCommand(
        String productId,
        Integer quantity) {

    public static OrderProductCommand from(OrderProductRequest orderProductRequest) {
        return new OrderProductCommand(
                orderProductRequest.productId(),
                orderProductRequest.quantity()
        );
    }
}
