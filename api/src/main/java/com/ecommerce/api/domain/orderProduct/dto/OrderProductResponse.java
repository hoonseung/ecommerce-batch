package com.ecommerce.api.domain.orderProduct.dto;

public record OrderProductResponse(
        Long orderProductId,
        String productId,
        Integer itemPrice,
        Integer quantity
) {

    public static OrderProductResponse from(OrderProductResult orderProductResult) {
        return new OrderProductResponse(
                orderProductResult.orderProductId(),
                orderProductResult.productId(),
                orderProductResult.itemPrice(),
                orderProductResult.quantity()
        );
    }
}
