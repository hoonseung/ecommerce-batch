package com.ecommerce.api.domain.product.exeption;

public class InsufficientStockQuantityException extends RuntimeException {

    public InsufficientStockQuantityException() {
        super("currently not enough stockQuantity");
    }
}
