package com.ecommerce.api.domain.orderProduct.dto;

public record OrderProductRequest(
        String productId,
        Integer quantity
) {

}
