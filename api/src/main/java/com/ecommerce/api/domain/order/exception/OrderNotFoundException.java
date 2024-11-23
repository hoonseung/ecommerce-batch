package com.ecommerce.api.domain.order.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderId) {
        super(orderId + " order not founded");
    }
}
