package com.ecommerce.api.domain.product.exeption;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String productId) {
        super(productId + " product not founded");
    }
}
