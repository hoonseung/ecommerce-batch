package com.ecommerce.api.domain.payment.exception;

public class IllegalPaymentStatusException extends RuntimeException {

    public IllegalPaymentStatusException(String message) {
        super(message);
    }
}
