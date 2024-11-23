package com.ecommerce.api.domain.orderProduct.dto;

import com.ecommerce.api.domain.orderProduct.entity.OrderProduct;

public record OrderProductResult(
        Long orderProductId,
        String productId,
        Integer itemPrice,
        Integer quantity
) {

    public static OrderProductResult from(OrderProduct orderProduct) {
        return new OrderProductResult(
                orderProduct.getOrderProductId(),
                orderProduct.getProductId(),
                orderProduct.getItemPrice(),
                orderProduct.getQuantity()
        );
    }
}
