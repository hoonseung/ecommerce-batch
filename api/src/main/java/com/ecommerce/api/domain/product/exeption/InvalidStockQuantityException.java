package com.ecommerce.api.domain.product.exeption;

public class InvalidStockQuantityException extends RuntimeException {

    public InvalidStockQuantityException() {
        super("stockQuantity cannot be negative");
    }
}
