package com.ecommerce.api.domain.order.exception;

public class IllegalOrderStatusException extends RuntimeException {

    public IllegalOrderStatusException(String message) {
        super(message);
    }
}
