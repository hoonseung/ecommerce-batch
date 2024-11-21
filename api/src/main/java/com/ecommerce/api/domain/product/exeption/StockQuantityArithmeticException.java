package com.ecommerce.api.domain.product.exeption;

public class StockQuantityArithmeticException extends RuntimeException {

    public StockQuantityArithmeticException() {
        super("The result of stockQuantity an arithmetic cannot be negative");
    }
}
